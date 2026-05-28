package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.datastore.model.EmailPassword
import aeb.proyecto.datastore.model.LastSearched
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Declarative Domain Boundary Contract defining local key-value persistence operations.
 *
 * This abstraction serves as the authoritative single-source-of-truth blueprint for all localized
 * session settings and configuration states. By isolating structural operations into cold reactive
 * streams and non-blocking coroutine suspension boundaries, it enforces loose coupling and full
 * testability across downstream domain and presentation layers.
 */
interface DatastoreInterface {

    // SETTINGS ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Global multi-preference configuration pipeline stream.
     *
     * Emits an updated, immutable [AppSettings] state topology snapshot whenever any underlying
     * localization or configuration attribute undergoes modification.
     */
    val appSettings: Flow<AppSettings>

    /**
     * Filtered reactive stream tracking the system's active visual interface theme token.
     *
     * Downstream presentation layers subscribing to this channel are guaranteed protection
     * against redundant layout evaluation triggers unless the underlying theme identity changes.
     */
    val themeMode: Flow<Int>

    /**
     * Filtered reactive stream tracking the active internationalization ISO-639 language code configuration.
     *
     * Downstream presentation layers subscribing to this channel are guaranteed protection
     * against redundant layout evaluation triggers unless the underlying language identity changes.
     */
    val language:Flow<String>

    /**
     * Filtered reactive stream tracking the preferred regional first day of the week identifier.
     *
     * Downstream presentation layers subscribing to this channel are guaranteed protection
     * against redundant layout evaluation triggers unless the underlying calendar identity changes.
     */
    val dayOfWeek:Flow<String>

    /**
     * Non-blocking query extraction reading the active cached multi-preference snapshot.
     *
     * @return The current stateful user experience configuration payload, or an empty fallback
     * [AppSettings] structural mapping if the storage layer is completely uninitialized.
     */
    suspend fun getAppSettings():AppSettings

    /**
     * Commits a holistic, multi-preference configuration payload mutation into persistent storage.
     *
     * This operation processes all systemic layout updates inside an isolated atomic transaction boundary
     * to eliminate multi-threaded data fragmentation.
     *
     * @param data The target immutable [AppSettings] layout structure to serialize.
     */
    suspend fun setAppSettings(data: AppSettings)

    /**
     * Automatically inspects the primary workstation regional configuration parameters to force-initialize
     * the regional calendar start marker boundary.
     *
     * Implementations should parse native device environment settings, extract locale metrics,
     * and merge the calculated mutation cleanly into the master preference layout matrix.
     */
    suspend fun setFirstDayOfWeek()
    // SETTINGS ----------------------------------------------------------------
    // *******************************************************************************************

    // STATISTICS ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Cold reactive stream emitting the active focused habit identifier token.
     *
     * Downstream consumer components or chart layout orchestrators subscribing to this pipeline
     * are insulated against redundant state emissions if the underlying long index remains unaltered.
     */
    val habitSelected: Flow<Long?>

    /**
     * Commits a structural database identifier token override to focus a specific habit profile.
     *
     * @param id The target database record long primary key to persist.
     */
    suspend fun setHabitSelected(id:Long)
    // STATISTICS ----------------------------------------------------------------
    // *******************************************************************************************

    val idTimerSelected:Flow<Long?>

    val dateTimerSelected:Flow<String?>

    val typeTimerSelected:Flow<Int?>

    val hourSelected:Flow<String>

    val restHourSelected:Flow<String>

    val timer:Flow<Int?>

    val timerLinkedAndFinished:Flow<Boolean>

    suspend fun getEmailAndPassword():EmailPassword

    suspend fun getLastSearched():LastSearched

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

    suspend fun setEmail(email:String)

    suspend fun setPassword(password:String)

    suspend fun setLastSearched(uid:String, date:String)

    suspend fun clearUser()
}