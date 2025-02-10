package aeb.proyecto.authentication

import kotlinx.coroutines.flow.Flow

interface AuthenticationInterface {

    fun signInWithGoogle(): Flow<AuthResponseAuthentication>

    suspend fun signInWithEmail(email: String, password: String): AuthResponseAuthentication

    suspend fun createAccountWithEmail(email: String, password: String): AuthResponseAuthentication

    suspend fun resendEmail(): AuthResponseAuthentication

    suspend fun forgotPassword(email: String): AuthResponseAuthentication

    fun logOut()

    fun currentUser(): AuthResponseAuthentication

    fun getName(): String

    fun getCurrentId(): String

}