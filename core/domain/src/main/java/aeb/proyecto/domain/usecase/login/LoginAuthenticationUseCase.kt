package aeb.proyecto.domain.usecase.login

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import android.content.Context
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginAuthenticationUseCase @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
) {

    suspend fun createAccount(email:String, password: String): Flow<AuthResponseAuthentication> {
        return authenticationInterface.createAccountWithEmail(email,password)
    }

    suspend fun signIn(email:String, password: String): Flow<AuthResponseAuthentication> {
        return authenticationInterface.signInWithEmail(email,password)
    }

    fun signInWithGoogle(context: Context): Flow<AuthResponseAuthentication> {
        return authenticationInterface.signInWithGoogle(context)
    }

    suspend fun resendEmail(email:String, password: String): Flow<AuthResponseAuthentication> {
        return authenticationInterface.resendEmail(email,password)
    }

    suspend fun forgotPassword(email: String): Flow<AuthResponseAuthentication> {
        return authenticationInterface.forgotPassword(email)

    }

}