package aeb.proyecto.datastore.repository

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.datastore.model.LastSearched
import aeb.proyecto.datastore.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Concrete Infrastructure Repository implementing local preference storage abstractions.
 *
 * This component acts as the architectural boundary implementation defined by [DatastoreInterface].
 * It decouples downstream domain logic and presentation ViewModels from platform-specific
 * serialization lifecycles by routing all structural data streams and asynchronous mutation
 * transactions through the central [DataStoreManager].
 *
 * @property dataStoreManager The thread-safe single-source-of-truth preferences engine wrapper.
 */
class DatastoreRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager
): DatastoreInterface {

    // SETTINGS ----------------------------------------------------------------
    // *******************************************************************************************

    override val appSettings: Flow<AppSettings>
        get() = dataStoreManager.appSettings

    override val themeMode: Flow<Int>
        get() = dataStoreManager.themeMode

    override val language: Flow<String>
        get() = dataStoreManager.languageMode

    override val dayOfWeek: Flow<String>
        get() = dataStoreManager.dayOfWeek

    /**
     * Forwards the asynchronous query request to extract the localized configuration snapshot.
     *
     * @return An absolute, immutable [AppSettings] state topology matrix wrapper.
     */
    override suspend fun getAppSettings(): AppSettings {
        return dataStoreManager.getAppSettings()
    }

    /**
     * Routes the holistic multi-preference payload to the core infrastructure engine for serialization.
     *
     * @param data The target stateful [AppSettings] metadata object to commit.
     */
    override suspend fun setAppSettings(data: AppSettings) {
        dataStoreManager.saveAppSettings(data)
    }

    /**
     * Dispatches the regional synchronization event hook down to the core layout manager.
     */
    override suspend fun setFirstDayOfWeek(){
        dataStoreManager.setFirstDayOfWeek()
    }
    // SETTINGS ----------------------------------------------------------------
    // *******************************************************************************************

    // STATISTICS ----------------------------------------------------------------
    // *******************************************************************************************
    override val habitSelected: Flow<Long?>
        get() = dataStoreManager.habitSelected

    /**
     * Routes the focused habit database record identifier to the infrastructure manager for local serialization.
     *
     * @param id The target database long primary key to persist.
     */
    override suspend fun setHabitSelected(id: Long) {
        dataStoreManager.setHabitSelected(id)
    }
    // STATISTICS ----------------------------------------------------------------
    // *******************************************************************************************


    // LOGIN SCREEN ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Routes the holistic authentication credential payload to the infrastructure manager for serialization.
     *
     * @param session The target stateful [UserSession] metadata object to commit.
     */
    override suspend fun saveUserSession(session: UserSession) {
        dataStoreManager.saveUserSession(session)
    }

    /**
     * Forwards the asynchronous query request to extract the localized authentication snapshot.
     *
     * @return An absolute, immutable [UserSession] state topology matrix wrapper.
     */
    override suspend fun getUserSession(): UserSession {
        return dataStoreManager.getUserSession()
    }

    /**
     * Dispatches the session purge event hook down to the core layout manager.
     */
    override suspend fun clearSession() {
        dataStoreManager.clearSession()
    }
    // LOGIN SCREEN ----------------------------------------------------------------
    // *******************************************************************************************

    // HABIT SCREEN ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Forwards the asynchronous query request to extract the active habit classification token.
     *
     * @return The active localized category string token, or null if unestablished.
     */
    override suspend fun getTypeSelected(): String? {
        return dataStoreManager.getTypeSelected()
    }

    /**
     * Routes the active habit classification string token to the infrastructure manager for serialization.
     *
     * @param type The target category or classification string token to persist.
     */
    override suspend fun setTypeSelected(type: String) {
        dataStoreManager.setTypeSelectedDate(type)
    }
    // HABIT SCREEN ----------------------------------------------------------------
    // *******************************************************************************************

    // TIMER SCREEN ----------------------------------------------------------------
    // *******************************************************************************************

    override val idHabitLinkedTimer: Flow<Long?>
        get() = dataStoreManager.idHabitLinkedSelected

    override val dateHabitLinkedTimer: Flow<String?>
        get() = dataStoreManager.dateHabitLinkedSelected

    override val typeTimerSelected: Flow<Int?>
        get() = dataStoreManager.typeTimerSelected

    override val wheelHourSelected: Flow<String> = combine(
        dataStoreManager.hourWheelTimer,
        dataStoreManager.minuteWheelTimer,
        dataStoreManager.secondWheelTimer
    ){ hour, minute, second ->

        val hourString = hour.toString().padStart(2, '0')
        val minuteString = minute.toString().padStart(2, '0')
        val secondString = second.toString().padStart(2, '0')

        "$hourString:$minuteString:$secondString"
    }

    override val restHourSelected: Flow<String> = combine(
        dataStoreManager.restIntervalHourTimer,
        dataStoreManager.restIntervalMinuteTimer,
        dataStoreManager.restIntervalSecondTimer
    ){ hour, minute, second ->

        val hourString = hour.toString().padStart(2, '0')
        val minuteString = minute.toString().padStart(2, '0')
        val secondString = second.toString().padStart(2, '0')

        "$hourString:$minuteString:$secondString"
    }

    override val numberSetsTimerSelected: Flow<Int?>
        get() = dataStoreManager.numberSetsTimerSelected

    /**
     * Forwards the asynchronous query request to extract the current linked habit ID snapshot.
     *
     * @return The localized unique identifier, or null if unlinked.
     */
    override suspend fun getIdHabitLinkedTimer(): Long? {
        return dataStoreManager.getIdHabitLinkedSelected()
    }

    /**
     * Forwards the asynchronous query request to extract the target tracking calendar date string.
     *
     * @return The targeted calendar reference snapshot sequence, or null if uninitialized.
     */
    override suspend fun getDateHabitLinkedTimer(): String? {
        return dataStoreManager.getDateHabitLinkedSelected()
    }

    /**
     * Routes the target rest interval hours component to the infrastructure layer.
     */
    override suspend fun setRestIntervalHourTimer(hour: Int) {
        dataStoreManager.setRestIntervalHourTimer(hour)
    }

    /**
     * Routes the target rest interval minutes component to the infrastructure layer.
     */
    override suspend fun setRestIntervalMinuteTimer(minute: Int) {
        dataStoreManager.setRestIntervalMinuteTimer(minute)
    }

    /**
     * Routes the target rest interval seconds component to the infrastructure layer.
     */
    override suspend fun setRestIntervalSecondTimer(second: Int) {
        dataStoreManager.setRestIntervalSecondTimer(second)
    }

    /**
     * Routes the target countdown hours component to the infrastructure layer.
     */
    override suspend fun setHourWheelTimer(hour: Int) {
        dataStoreManager.setHourWheelTimer(hour)
    }

    /**
     * Routes the target countdown minutes component to the infrastructure layer.
     */
    override suspend fun setMinuteWheelTimer(minute: Int) {
        dataStoreManager.setMinuteWheelTimer(minute)
    }

    /**
     * Routes the target countdown seconds component to the infrastructure layer.
     */
    override suspend fun setSecondWheelTimer(second: Int) {
        dataStoreManager.setSecondWheelTimer(second)
    }

    /**
     * Routes the active loop set configuration total count to the infrastructure layer.
     */
    override suspend fun setNumberSetsTimer(sets: Int) {
        dataStoreManager.setNumberSetsTimerSelected(sets)
    }

    /**
     * Binds a specific habit profile structure unique primary key directly into the active timer context.
     */
    override suspend fun setIdHabitLinkedTimer(id: Long) {
        dataStoreManager.setIdHabitLinkedSelected(id)
    }

    /**
     * Overrides the targeting system calendar snapshot sequence for tracking data consolidation.
     */
    override suspend fun setDateHabitLinkedTimer(date: String) {
        dataStoreManager.setDateHabitLinkedSelected(date)
    }

    /**
     * Overrides the active operational behavioral mode token inside infrastructure storage.
     */
    override suspend fun setTypeTimerSelected(type: Int) {
        dataStoreManager.setTypeTimerSelected(type)
    }
    // TIMER SCREEN ----------------------------------------------------------------
    // *******************************************************************************************

    // STOPWATCH SERVICE ----------------------------------------------------------------
    // *******************************************************************************************

    override val timerLinkedAndFinished: Flow<Boolean>
        get() = dataStoreManager.timerLinkedAndFinished

    /**
     * Forwards the asynchronous query request to extract the instantaneous elapsed timing snapshot.
     *
     * @return The current long tracking duration, or null if uninitialized.
     */
    override suspend fun getTimePassedTimer(): Long? {
        return dataStoreManager.getTimePassedTimer()
    }

    /**
     * Routes the active elapsed runtime metric down to the core infrastructure engine for serialization.
     *
     * @param time The current absolute time metric sequence to persist.
     */
    override suspend fun setTimePassedTimer(time: Long) {
        dataStoreManager.setTimePassedTimer(time)
    }

    /**
     * Routes the session completion validation flag down to the core infrastructure engine for serialization.
     *
     * @param isLinked The target confirmation state visibility token to serialize.
     */
    override suspend fun setIsLinkedHabitAndFinished(isLinked: Boolean) {
        dataStoreManager.setIsLinkedHabitAndFinished(isLinked)
    }

    // STOPWATCH SERVICE ----------------------------------------------------------------
    // *******************************************************************************************
}