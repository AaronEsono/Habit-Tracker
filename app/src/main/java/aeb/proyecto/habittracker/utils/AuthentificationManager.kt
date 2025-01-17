package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.utils.Constans.ERROR_EMAIL_SEND
import aeb.proyecto.habittracker.utils.Constans.ERROR_UNVERIFIED_EMAIL
import aeb.proyecto.habittracker.utils.Constans.ERROR_UPDATE_PROFILE
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
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class AuthenticationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analyticsManager: AnalyticsManager,
    private val auth: FirebaseAuth
) {

    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    //Cambiamos el perfil para mostrar el nombre en el correo
                    user?.let {
                        val profileUpdates = userProfileChangeRequest {
                            displayName = user.email
                        }

                        it.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    it.sendEmailVerification()
                                        .addOnCompleteListener { taskEmail ->
                                            if (taskEmail.isSuccessful) {
                                                trySend(AuthResponse.Success)
                                                analyticsManager.logEvent(
                                                    TypeEvent.createdAccount(
                                                        it.uid
                                                    )
                                                )
                                            } else {
                                                trySend(AuthResponse.Error(ERROR_EMAIL_SEND))
                                                analyticsManager.logEvent(
                                                    TypeEvent.error(
                                                        ERROR_EMAIL_SEND
                                                    )
                                                )
                                            }
                                        }
                                } else {
                                    trySend(AuthResponse.Error(ERROR_UPDATE_PROFILE))
                                    analyticsManager.logEvent(TypeEvent.error(ERROR_UPDATE_PROFILE))
                                }
                            }
                    }
                } else {
                    val error = if (task.exception is FirebaseAuthException)
                        (task.exception as FirebaseAuthException).errorCode
                    else ""

                    trySend(AuthResponse.Error(error))
                }
            }
        awaitClose()
    }

    fun signInWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    user?.let {
                        if (it.isEmailVerified) {
                            trySend(AuthResponse.Success)
                            analyticsManager.logEvent(TypeEvent.logUserLogged(it.uid))
                        } else {
                            trySend(AuthResponse.Error(ERROR_UNVERIFIED_EMAIL))
                            analyticsManager.logEvent(TypeEvent.error(ERROR_UNVERIFIED_EMAIL))
                        }
                    }
                } else {
                    val error = if (task.exception is FirebaseAuthException)
                        (task.exception as FirebaseAuthException).errorCode
                    else ""

                    trySend(AuthResponse.Error(error))
                }
            }
        awaitClose()
    }

    fun signInWithGoogle(): Flow<AuthResponse> = callbackFlow {
        val googleValidation = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
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
                                    trySend(AuthResponse.Success)
                                } else {
                                    trySend(AuthResponse.Error(task.exception?.message.toString()))
                                }
                            }

                    } catch (e: GoogleIdTokenParsingException) {
                        trySend(AuthResponse.Error(e.message.toString()))
                    }
                }
            }


        } catch (e: Exception) {
            trySend(AuthResponse.Error(e.message.toString()))
            analyticsManager.logEvent(TypeEvent.error(e.message.toString()))
        }

        awaitClose()
    }

    fun resendEmail(): Flow<AuthResponse> = callbackFlow {
        auth.currentUser?.let {
            it.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(AuthResponse.Success)
                        analyticsManager.logEvent(TypeEvent.resendEmail(it.email!!))
                    } else {
                        val error = if (task.exception is FirebaseAuthException)
                            (task.exception as FirebaseAuthException).errorCode
                        else ""

                        trySend(AuthResponse.Error(error))
                        analyticsManager.logEvent(TypeEvent.error(error))
                    }
                }

        }
        awaitClose()
    }

    fun forgotPassword(email: String): Flow<AuthResponse> = callbackFlow {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                    analyticsManager.logEvent(TypeEvent.forgotPassword(email))
                } else {
                    val error = if (task.exception is FirebaseAuthException)
                        (task.exception as FirebaseAuthException).errorCode
                    else ""

                    trySend(AuthResponse.Error(error))
                    analyticsManager.logEvent(TypeEvent.error(error))
                }
            }
        awaitClose()
    }

    fun currentUser(): Flow<AuthResponse> = callbackFlow {

        auth.currentUser?.let { user ->
            user.getIdToken(true)
                .addOnSuccessListener {
                    trySend(AuthResponse.Success)
                    analyticsManager.logEvent(TypeEvent.reconnected(user.uid))
                }
                .addOnFailureListener {
                    trySend(AuthResponse.Error("Expires"))
                    analyticsManager.logEvent(TypeEvent.error(it.message.toString()))
                }
        } ?: trySend(AuthResponse.Error("No user found"))

        awaitClose()
    }

    fun logOut() {
        analyticsManager.logEvent(TypeEvent.logOut(auth.currentUser!!.uid))
        auth.signOut()
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun getName(): String {
        return auth.currentUser?.email?.substringBefore("@") ?: ""
    }

    fun getCurrentId(): String {
        return auth.currentUser?.uid ?: ""
    }

}

interface AuthResponse {
    data object Success : AuthResponse
    data class Error(val message: String) : AuthResponse
}