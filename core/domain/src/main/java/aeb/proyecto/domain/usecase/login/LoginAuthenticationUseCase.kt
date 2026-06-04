package aeb.proyecto.domain.usecase.login

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import android.content.Context
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Domain Use Case driving the complete security and authentication matrix of the application.
 * Coordinates network-bound identity transactions—ranging from standard email credentials to
 * OAuth2 social sign-ins—behind a framework-agnostic boundary interface.
 *
 * Streams operational state mutations via reactive response tokens to update UI loaders gracefully.
 *
 * @property authenticationInterface The boundary contract handling remote identity provider transactions.
 */
class LoginAuthenticationUseCase @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
) {

    /**
     * Triggers the creation pipeline for a brand-new user identity record utilizing traditional credentials.
     *
     * @param email The raw, sanitized email address string acting as the unique user identifier.
     * @param password The raw, unencrypted password credential string.
     * @return A continuous reactive stream pipeline tracking the asynchronous state of the registration request.
     */
    suspend fun createAccount(email:String, password: String): Flow<AuthResponseAuthentication> {
        return authenticationInterface.createAccountWithEmail(email,password)
    }

    /**
     * Validates user credentials against the remote identity repository to establish an active session.
     *
     * @param email The raw, sanitized account email address string.
     * @param password The raw, unencrypted account verification password string.
     * @return A continuous reactive stream pipeline tracking the asynchronous state of the login request.
     */
    suspend fun signIn(email:String, password: String): Flow<AuthResponseAuthentication> {
        return authenticationInterface.signInWithEmail(email,password)
    }

    /**
     * Initiates the native Google One Tap / OAuth2 federated social identity authentication pipeline.
     * Requires an active window context platform anchor to invoke the native account picker overlay.
     *
     * @param context The active Android UI context framework token hosting the system credential picker.
     * @return A continuous reactive stream pipeline tracking the asynchronous state of the social login request.
     */
    fun signInWithGoogle(context: Context): Flow<AuthResponseAuthentication> {
        return authenticationInterface.signInWithGoogle(context)
    }

    /**
     * Requests the identity server to fire a secondary account verification email payload to the user.
     *
     * @param email The target account email address string requiring activation clearance.
     * @param password The account verification password string confirming ownership.
     * @return A continuous reactive stream pipeline tracking the asynchronous state of the dispatch sequence.
     */
    suspend fun resendEmail(email:String, password: String): Flow<AuthResponseAuthentication> {
        return authenticationInterface.resendEmail(email,password)
    }

    /**
     * Triggers a remote credential reset workflow, forcing the delivery of a password restoration link.
     *
     * @param email The target account email address string requesting recovery assistance.
     * @return A continuous reactive stream pipeline tracking the asynchronous state of the recovery request.
     */
    suspend fun forgotPassword(email: String): Flow<AuthResponseAuthentication> {
        return authenticationInterface.forgotPassword(email)

    }

}