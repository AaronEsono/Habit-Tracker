package aeb.proyecto.domain.usecase.login

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.EmailPassword
import javax.inject.Inject

class SaveLoginCredentialUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
) {

    suspend fun clearData() = datastoreInterface.clearUser()

    suspend fun setData(email:String,password:String) {
        datastoreInterface.setEmail(email)
        datastoreInterface.setPassword(password)
    }

    suspend fun getCredentials():EmailPassword{
        return datastoreInterface.getEmailAndPassword()
    }

}