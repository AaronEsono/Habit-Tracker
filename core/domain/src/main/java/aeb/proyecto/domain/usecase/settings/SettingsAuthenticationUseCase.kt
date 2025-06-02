package aeb.proyecto.domain.usecase.settings

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import javax.inject.Inject

class SettingsAuthenticationUseCase @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
){

    fun getCurrentUser():Boolean{
        return authenticationInterface.currentUser() is AuthResponseAuthentication.Success
    }

}