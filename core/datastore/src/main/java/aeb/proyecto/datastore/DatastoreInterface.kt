package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.datastore.model.EmailPassword
import aeb.proyecto.datastore.model.LastSearched
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DatastoreInterface {

    val appSettings: Flow<AppSettings>

    val themeMode: Flow<Int>

    val language:Flow<String>

    val dayOfWeek:Flow<String>

    val idTimerSelected:Flow<Long?>

    val dateTimerSelected:Flow<String?>

    val typeTimerSelected:Flow<Int?>

    val hourSelected:Flow<String>

    val restHourSelected:Flow<String>

    val timer:Flow<Int?>

    val timerLinkedAndFinished:Flow<Boolean>

    val habitSelected: Flow<Long?>

    suspend fun getEmailAndPassword():EmailPassword

    suspend fun getLastSearched():LastSearched

    suspend fun getDayStartWeek():String?

    suspend fun getLanguage():String?

    suspend fun getTypeSelected():String?

    suspend fun getIdTimerSelected():Long?

    suspend fun getDateTimerSelected():String?

    suspend fun getTypeTimerSelected():Int?

    suspend fun getRestIntervalHourTimer():Int?

    suspend fun getRestIntervalMinuteTimer():Int?

    suspend fun getRestIntervalSecondTimer():Int?

    suspend fun getSetsTimer():Int?

    suspend fun getTimePassedTimer():Long?

    suspend fun getIsLinkedHabitAndFinished():Boolean?

    suspend fun setHabitSelected(id:Long)

    suspend fun setTimePassedTimer(time:Long)

    suspend fun setIsLinkedHabitAndFinished(isLinked:Boolean)

    suspend fun setSetsTimer(sets:Int)

    suspend fun setRestIntervalHourTimer(hour:Int)

    suspend fun setRestIntervalMinuteTimer(minute:Int)

    suspend fun setRestIntervalSecondTimer(second:Int)

    suspend fun setTimerData(id:Long, date:String, type:Int, time:Triple<Int,Int,Int>)

    suspend fun setIdTimerSelected(id:Long)

    suspend fun setDateTimerSelected(date:String)

    suspend fun setTypeTimerSelected(type:Int)

    suspend fun setHourWheelTimer(hour:Int)

    suspend fun setMinuteWheelTimer(minute:Int)

    suspend fun setSecondWheelTimer(second:Int)

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