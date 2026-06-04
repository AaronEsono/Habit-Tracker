package aeb.proyecto.domain.usecase.login

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.UserSession
import javax.inject.Inject

/**
 * Domain Use Case driving the local persistence lifecycle of active user credentials.
 * Handles the secure hydration, mutation, and absolute eviction of session tokens ([UserSession])
 * inside the local preference storage engine.
 *
 * Ensures seamless automatic login sequences across application boot cycles.
 *
 * @property datastoreInterface The abstracted data-layer preference storage contract.
 */
class SaveLoginCredentialUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
) {

    /**
     * Executes an absolute, atomic wipe of all local session parameters from the device storage.
     * Typically invoked during user log-out or token invalidation sequences to secure the local environment.
     */
    suspend fun clearData() = datastoreInterface.clearSession()

    /**
     * Persists an active, unified user session payload into local device preferences.
     *
     * @param data The unified domain model carrying active authentication credentials and profile tokens.
     */
    suspend fun saveUserSession(data: UserSession) {
        datastoreInterface.saveUserSession(data)
    }

    /**
     * Performs a synchronous-bound lookup to fetch the active session state vectors from disk storage.
     *
     * @return The strongly-typed [UserSession] profile containing current tokens or empty fallbacks.
     */
    suspend fun getCredentials():UserSession{
        return datastoreInterface.getUserSession()
    }

}