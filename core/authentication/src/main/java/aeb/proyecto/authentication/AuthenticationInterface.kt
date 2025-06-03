package aeb.proyecto.authentication

import kotlinx.coroutines.flow.Flow

interface AuthenticationInterface {

    fun signInWithGoogle(): Flow<AuthResponseAuthentication>

    suspend fun signInWithEmail(email: String, password: String): Flow<AuthResponseAuthentication>

    suspend fun createAccountWithEmail(email: String, password: String): Flow<AuthResponseAuthentication>

    suspend fun resendEmail(email: String, password: String): Flow<AuthResponseAuthentication>

    suspend fun forgotPassword(email: String): Flow<AuthResponseAuthentication>

    fun logOut()

    fun currentUser(): AuthResponseAuthentication

    fun getName(): String

    fun getCurrentId(): String

}