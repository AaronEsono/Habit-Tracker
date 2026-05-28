package aeb.proyecto.domain.usecase.login

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.UserSession
import javax.inject.Inject

class SaveLoginCredentialUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
) {

    suspend fun clearData() = datastoreInterface.clearSession()

    suspend fun saveUserSession(data: UserSession) {
        datastoreInterface.saveUserSession(data)
    }

    suspend fun getCredentials():UserSession{
        return datastoreInterface.getUserSession()
    }

}