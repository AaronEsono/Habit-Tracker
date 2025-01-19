package aeb.proyecto.datastore.repository

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.EmailPassword
import aeb.proyecto.datastore.model.LastSearched
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatastoreRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager
): DatastoreInterface {
    override val themeMode: Flow<Int>
        get() = dataStoreManager.themeMode

    override suspend fun getEmailAndPassword(): EmailPassword {
        return dataStoreManager.getEmailPassword()
    }

    override suspend fun getLastSearched(): LastSearched {
        return dataStoreManager.getLastSearched()
    }

    override suspend fun setModeTheme(themeMode: Int) {
        dataStoreManager.setModeTheme(themeMode)
    }

    override suspend fun setEmail(email: String) {
        dataStoreManager.setEmail(email)
    }

    override suspend fun setPassword(password: String) {
        dataStoreManager.setPassword(password)
    }

    override suspend fun setLastSearched(uid: String, date: String) {
        dataStoreManager.setLastSearched(uid, date)
    }

    override suspend fun clearUser() {
        dataStoreManager.clearDataUser()
    }
}