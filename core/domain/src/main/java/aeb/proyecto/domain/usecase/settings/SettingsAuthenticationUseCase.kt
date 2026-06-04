package aeb.proyecto.domain.usecase.settings

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import javax.inject.Inject

/**
 * Domain Use Case designed to perform quick, structural session state evaluations
 * specifically tailored for runtime layout mutations inside the settings view hierarchy.
 *
 * Checks corporate identity states without exposing complex token footprints or profile
 * models to downstream UI layers.
 *
 * @property authenticationInterface The boundary contract handling identity provider session status.
 */
class SettingsAuthenticationUseCase @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
){

    /**
     * Evaluates whether there is an actively authenticated user session securely bound to the application.
     * Maps the underlying polymorphic auth response into a lightweight boolean flag.
     *
     * @return True if the active session matches an authenticated [AuthResponseAuthentication.Success] token matrix,
     * false otherwise.
     */
    fun getCurrentUser():Boolean{
        return authenticationInterface.currentUser() is AuthResponseAuthentication.Success
    }

}