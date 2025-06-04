package aeb.proyecto.domain.usecase.save

import aeb.proyecto.authentication.AuthenticationInterface
import javax.inject.Inject

class SaveAuthenticationUseCase @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
) {

    suspend fun logOut() = authenticationInterface.logOut()

    suspend fun getCurrentId() = authenticationInterface.getCurrentId()

    suspend fun getName() = authenticationInterface.getName()

}