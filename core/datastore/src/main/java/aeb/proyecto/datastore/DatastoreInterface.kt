package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.EmailPassword
import aeb.proyecto.datastore.model.LastSearched
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DatastoreInterface {

    val themeMode: Flow<Int>

    suspend fun getEmailAndPassword():EmailPassword

    suspend fun getLastSearched():LastSearched

    suspend fun setModeTheme(themeMode:Int)

    suspend fun setEmail(email:String)

    suspend fun setPassword(password:String)

    suspend fun setLastSearched(uid:String, date:String)

    suspend fun clearUser()

}