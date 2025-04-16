package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.EmailPassword
import aeb.proyecto.datastore.model.LastSearched
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DatastoreInterface {

    val themeMode: Flow<Int>

    val language:Flow<String>

    val dayOfWeek:Flow<String>

    suspend fun getEmailAndPassword():EmailPassword

    suspend fun getLastSearched():LastSearched

    suspend fun getDayStartWeek():String?

    suspend fun getLanguage():String?

    suspend fun getTypeSelected():String?

    suspend fun setTypeSelectedDate(type:String)

    suspend fun setDayStartWeek(day:String)

    suspend fun setLanguage(language:String)

    suspend fun setModeTheme(themeMode:Int)

    suspend fun setEmail(email:String)

    suspend fun setPassword(password:String)

    suspend fun setLastSearched(uid:String, date:String)

    suspend fun clearUser()

    suspend fun setFirstDayOfWeek()
}