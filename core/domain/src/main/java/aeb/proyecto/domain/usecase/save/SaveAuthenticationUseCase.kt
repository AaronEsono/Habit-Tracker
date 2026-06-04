package aeb.proyecto.domain.usecase.save

import aeb.proyecto.authentication.AuthenticationInterface
import javax.inject.Inject

/**
 * Domain Use Case designed to manage profile metadata extraction and remote session
 * de-authorization pipelines inside the sync and save subsystem.
 *
 * Provides high-efficiency, framework-agnostic access to active identity tracking parameters
 * required to authorize cloud synchronization streams.
 *
 * @property authenticationInterface The boundary contract handling remote identity provider transactions.
 */
class SaveAuthenticationUseCase @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
) {

    /**
     * Termina de forma asíncrona la sesión del usuario en el proveedor de identidad remoto,
     * invalidando los tokens de acceso activos en el servidor.
     */
    suspend fun logOut() = authenticationInterface.logOut()

    /**
     * Recupera el identificador único global (UID) del usuario actualmente autenticado
     * para su uso como clave relacional en operaciones de sincronización en la nube.
     *
     * @return El identificador de cadena de texto único del usuario, o null si la sesión no está activa.
     */
    suspend fun getCurrentId() = authenticationInterface.getCurrentId()

    /**
     * Extrae el nombre de perfil o alias del usuario registrado en el proveedor de identidad.
     *
     * @return El nombre descriptivo del usuario, o null si no se ha configurado o no hay sesión.
     */
    suspend fun getName() = authenticationInterface.getName()

}