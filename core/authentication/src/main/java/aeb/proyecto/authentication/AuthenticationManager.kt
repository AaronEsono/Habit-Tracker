package aeb.proyecto.authentication

import aeb.proyecto.analytics.AnalyticsManagerInterface
import aeb.proyecto.analytics.events.AuthenticationEvents
import aeb.proyecto.authentication.errors.treatError
import aeb.proyecto.authentication.utils.ERROR_EMAIL_EXISTS
import aeb.proyecto.authentication.utils.ERROR_UNVERIFIED_EMAIL
import aeb.proyecto.authentication.utils.createNonce
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analyticsManagerInterface: AnalyticsManagerInterface,
    private val auth: FirebaseAuth
):AuthenticationInterface {

    override suspend fun createAccountWithEmail(email: String, password: String): Flow<AuthResponseAuthentication> = callbackFlow {
        trySend(AuthResponseAuthentication.Loading)

        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        //Actualizacion del perfil y envio del email
                        auth.currentUser?.let {
                            val profileUpdates = userProfileChangeRequest {
                                displayName = it.email
                            }

                            it.updateProfile(profileUpdates)
                            it.sendEmailVerification()

                            auth.signOut()

                            analyticsManagerInterface.logEvent(AuthenticationEvents.createdAccount(it.uid))
                            trySend(AuthResponseAuthentication.Success)
                        }

                    }else{
                        auth.signOut()
                        analyticsManagerInterface.logEvent(AuthenticationEvents.error(ERROR_EMAIL_EXISTS))
                        trySend(AuthResponseAuthentication.Error(R.string.error_auth_email_exists))
                    }
                }
        } catch (e: Exception) {
            val error = treatError(e)
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(e.toString()))
            trySend(AuthResponseAuthentication.Error(error))
        }

        awaitClose()
    }

    override suspend fun signInWithEmail(email: String, password: String): Flow<AuthResponseAuthentication> = callbackFlow {
        trySend(AuthResponseAuthentication.Loading)

        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        if(auth.currentUser?.isEmailVerified == true){
                            analyticsManagerInterface.logEvent(AuthenticationEvents.logUserLogged(auth.currentUser?.uid ?: ""))
                            trySend(AuthResponseAuthentication.Success)
                        }else{
                            analyticsManagerInterface.logEvent(AuthenticationEvents.error(ERROR_UNVERIFIED_EMAIL))
                            auth.signOut()
                            trySend(AuthResponseAuthentication.UnverifiedEmail)
                        }
                    }else{
                        val error = treatError(task.exception!!)
                        analyticsManagerInterface.logEvent(AuthenticationEvents.error(task.exception.toString()))
                        trySend(AuthResponseAuthentication.Error(error))
                    }
                }
        }catch (e:Exception){
            val error = treatError(e)
            auth.signOut()
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(e.toString()))
            trySend(AuthResponseAuthentication.Error(error))
        }

        awaitClose()
    }

    override fun signInWithGoogle(): Flow<AuthResponseAuthentication> = callbackFlow {
        val googleValidation = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_id))
            .setNonce(createNonce())
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleValidation)
            .build()

        try{
            val credentialManager = CredentialManager.create(context)

            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential

            if(credential is CustomCredential){
                if(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        val firebaseCredential = GoogleAuthProvider.getCredential(
                            googleIdTokenCredential.idToken,
                            null
                        )

                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener {
                                if(it.isSuccessful){
                                    analyticsManagerInterface.logEvent(AuthenticationEvents.loggedWithGoogle(it.result.user!!.uid))
                                    trySend(AuthResponseAuthentication.Success)
                                }else{
                                    val error = treatError(it.exception!!)
                                    analyticsManagerInterface.logEvent(AuthenticationEvents.error(it.exception.toString()))
                                    trySend(AuthResponseAuthentication.Error(error))
                                }
                            }

                    }catch (e:GoogleIdTokenParsingException){
                        val error = treatError(e)
                        analyticsManagerInterface.logEvent(AuthenticationEvents.error(e.toString()))
                        trySend(AuthResponseAuthentication.Error(error))
                    }
                }
            }
        }catch (e:Exception){
            val error = treatError(e)
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(e.toString()))
            trySend(AuthResponseAuthentication.Error(error))
        }


        awaitClose()
    }

    override suspend fun resendEmail(email:String, password:String): Flow<AuthResponseAuthentication> = callbackFlow {
        trySend(AuthResponseAuthentication.Loading)

        try {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                            if(it.isSuccessful){
                                analyticsManagerInterface.logEvent(AuthenticationEvents.resendEmail(auth.currentUser!!.email!!))
                                auth.signOut()
                                trySend(AuthResponseAuthentication.Success)
                            }else{
                                val error = treatError(it.exception ?: Exception())
                                analyticsManagerInterface.logEvent(AuthenticationEvents.error(it.exception.toString()))
                                trySend(AuthResponseAuthentication.Error(error))
                            }
                        }
                    }
                }
        }catch (e:Exception){
            val error = treatError(e)
            auth.signOut()
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(e.toString()))
            trySend(AuthResponseAuthentication.Error(error))
        }

        awaitClose()
    }

    override suspend fun forgotPassword(email: String): Flow<AuthResponseAuthentication> = callbackFlow {
        trySend(AuthResponseAuthentication.Loading)

        try {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        analyticsManagerInterface.logEvent(AuthenticationEvents.forgotPassword(email))
                        trySend(AuthResponseAuthentication.Success)
                    }
                }
        }catch (e:Exception){
            val error = treatError(e)
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(e.toString()))
            trySend(AuthResponseAuthentication.Error(error))
        }

        awaitClose()
    }

    override fun logOut() {
        analyticsManagerInterface.logEvent(AuthenticationEvents.logOut(auth.currentUser!!.uid))
        auth.signOut()
    }

    override fun currentUser(): AuthResponseAuthentication {
        auth.currentUser?.let{
            analyticsManagerInterface.logEvent(AuthenticationEvents.reconnected(it.uid))
            return AuthResponseAuthentication.Success
        } ?: return AuthResponseAuthentication.Error(R.string.error_auth_no_user_found)
    }

    override fun getName(): String {
        return auth.currentUser?.email?.substringBefore("@") ?: ""
    }

    override fun getCurrentId(): String {
        return auth.currentUser?.uid ?: ""
    }
}

interface AuthResponseAuthentication {
    data object Success : AuthResponseAuthentication
    data class Error(val message: Int) : AuthResponseAuthentication
    data object UnverifiedEmail: AuthResponseAuthentication
    data object Loading: AuthResponseAuthentication
}