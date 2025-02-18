package aeb.proyecto.authentication

import aeb.proyecto.analytics.AnalyticsManagerInterface
import aeb.proyecto.analytics.events.AuthenticationEvents
import aeb.proyecto.authentication.utils.ERROR_NO_USER_FOUND
import aeb.proyecto.authentication.utils.ERROR_SEND_EMAIL
import aeb.proyecto.authentication.utils.ERROR_UNVERIFIED_EMAIL
import aeb.proyecto.authentication.utils.createNonce
import aeb.proyecto.authentication.utils.treatException
import android.content.Context
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

    override suspend fun createAccountWithEmail(email: String, password: String): AuthResponseAuthentication {
        try {
            val response = auth.createUserWithEmailAndPassword(email, password).await()

            response.user?.let {
                val profileUpdates = userProfileChangeRequest {
                    displayName = it.email
                }

                it.updateProfile(profileUpdates).await()
                it.sendEmailVerification().await()

                auth.signOut()

                analyticsManagerInterface.logEvent(AuthenticationEvents.createdAccount(it.uid))
                return AuthResponseAuthentication.Success
            }

            auth.signOut()
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(ERROR_SEND_EMAIL))
            return AuthResponseAuthentication.Error(ERROR_SEND_EMAIL)
        } catch (e: Exception) {
            val error = treatException(e)
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(error))
            return AuthResponseAuthentication.Error(error)
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): AuthResponseAuthentication {
        return try {
            val response = auth.signInWithEmailAndPassword(email, password).await()

            if(response.user?.isEmailVerified == true){
                analyticsManagerInterface.logEvent(AuthenticationEvents.logUserLogged(response.user!!.uid))
                AuthResponseAuthentication.Success
            }else{
                analyticsManagerInterface.logEvent(AuthenticationEvents.error(ERROR_UNVERIFIED_EMAIL))
                AuthResponseAuthentication.Error(ERROR_UNVERIFIED_EMAIL)
            }
        }catch (e:Exception){
            val error = treatException(e)
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(error))
            AuthResponseAuthentication.Error(error)
        }
    }

    override fun signInWithGoogle(): Flow<AuthResponseAuthentication> = callbackFlow {
        val googleValidation = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("")
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
                                    val error = treatException(it.exception!!)
                                    analyticsManagerInterface.logEvent(AuthenticationEvents.error(error))
                                    trySend(AuthResponseAuthentication.Error(error))
                                }
                            }

                    }catch (e:GoogleIdTokenParsingException){
                        val error = treatException(e)
                        analyticsManagerInterface.logEvent(AuthenticationEvents.error(error))
                        trySend(AuthResponseAuthentication.Error(error))
                    }
                }
            }
        }catch (e:Exception){
            val error = treatException(e)
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(error))
            trySend(AuthResponseAuthentication.Error(error))
        }


        awaitClose()
    }

    override suspend fun resendEmail(): AuthResponseAuthentication {
        try {
            auth.currentUser?.sendEmailVerification()?.await()
            analyticsManagerInterface.logEvent(AuthenticationEvents.resendEmail(auth.currentUser!!.email!!))
            return AuthResponseAuthentication.Success
        }catch (e:Exception){
            val error = treatException(e)
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(error))
            return AuthResponseAuthentication.Error(error)
        }
    }

    override suspend fun forgotPassword(email: String): AuthResponseAuthentication {
        try {
            auth.sendPasswordResetEmail(email).await()
            analyticsManagerInterface.logEvent(AuthenticationEvents.forgotPassword(email))
            return AuthResponseAuthentication.Success
        }catch (e:Exception){
            val error = treatException(e)
            analyticsManagerInterface.logEvent(AuthenticationEvents.error(error))
            return AuthResponseAuthentication.Error(error)
        }
    }

    override fun logOut() {
        analyticsManagerInterface.logEvent(AuthenticationEvents.logOut(auth.currentUser!!.uid))
        auth.signOut()
    }

    override fun currentUser(): AuthResponseAuthentication {
        auth.currentUser?.let{
            analyticsManagerInterface.logEvent(AuthenticationEvents.reconnected(it.uid))
            return AuthResponseAuthentication.Success
        } ?: return AuthResponseAuthentication.Error(ERROR_NO_USER_FOUND)
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
    data class Error(val message: String) : AuthResponseAuthentication
}