package aeb.proyecto.authentication

import aeb.proyecto.analytics.AnalyticsManager
import aeb.proyecto.analytics.TypeEvent
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
import com.google.firebase.auth.FirebaseAuthException
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
    private val analyticsManager: AnalyticsManager,
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
                return AuthResponseAuthentication.Success
            }

            auth.signOut()
            return AuthResponseAuthentication.Error(ERROR_SEND_EMAIL)
        } catch (e: Exception) {
            val error = treatException(e)
            return AuthResponseAuthentication.Error(error)
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): AuthResponseAuthentication {
        return try {
            val response = auth.signInWithEmailAndPassword(email, password).await()

            if(response.user?.isEmailVerified == true){
                AuthResponseAuthentication.Success
            }else{
                AuthResponseAuthentication.Error(ERROR_UNVERIFIED_EMAIL)
            }
        }catch (e:Exception){
            val error = treatException(e)
            AuthResponseAuthentication.Error(error)
        }
    }

    override fun signInWithGoogle(): Flow<AuthResponseAuthentication> = callbackFlow {
        val googleValidation = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setNonce(createNonce())
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleValidation)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)

                        val firebaseCredential = GoogleAuthProvider.getCredential(
                            googleIdTokenCredential.idToken,
                            null
                        )

                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    trySend(AuthResponseAuthentication.Success)
                                } else {
                                    trySend(AuthResponseAuthentication.Error(task.exception?.message.toString()))
                                }
                            }

                    } catch (e: GoogleIdTokenParsingException) {
                        trySend(AuthResponseAuthentication.Error(e.message.toString()))
                    }
                }
            }


        } catch (e: Exception) {
            trySend(AuthResponseAuthentication.Error(e.message.toString()))
            analyticsManager.logEvent(TypeEvent.error(e.message.toString()))
        }

        awaitClose()
    }

    override suspend fun resendEmail(): AuthResponseAuthentication {
        try {
            auth.currentUser?.sendEmailVerification()?.await()
            return AuthResponseAuthentication.Success
        }catch (e:Exception){
            return AuthResponseAuthentication.Error(e.message.toString())
        }
    }

    override suspend fun forgotPassword(email: String): AuthResponseAuthentication {
        try {
            auth.sendPasswordResetEmail(email).await()
            return AuthResponseAuthentication.Success
        }catch (e:Exception){
            return AuthResponseAuthentication.Error(e.message.toString())
        }
    }

    override fun logOut() {
        auth.signOut()
    }

    override fun currentUser(): AuthResponseAuthentication {
        auth.currentUser?.let{ return AuthResponseAuthentication.Success }
            ?: return AuthResponseAuthentication.Error("No user found")
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