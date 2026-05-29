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

    override val idHabitLinkedTimer: Flow<Long?>
        get() = dataStoreManager.idTimerSelected

    override val dateHabitLinkedTimer: Flow<String?>
        get() = dataStoreManager.dateTimerSelected

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

    override val numberSetsSelected: Flow<Int?>
        get() = dataStoreManager.setsTimer

    override val timerLinkedAndFinished: Flow<Boolean>
        get() = dataStoreManager.timerLinkedAndFinished

    override suspend fun getLastSearched(): LastSearched {
        return dataStoreManager.getLastSearched()
    }

    override suspend fun getIdHabitLinkedTimer(): Long? {
        return dataStoreManager.getIdTimerSelected()
    }

    override suspend fun getDateHabitLinkedTimer(): String? {
        return dataStoreManager.getDateTimerSelected()
    }

    override suspend fun getTimePassedTimer(): Long? {
        return dataStoreManager.getTimePassedTimer()
    }

    override suspend fun getIsLinkedHabitAndFinished(): Boolean? {
        return dataStoreManager.getIsLinkedHabitAndFinished()
    }

    override suspend fun setTimePassedTimer(time: Long) {
        dataStoreManager.setTimePassedTimer(time)
    }

    override suspend fun setIsLinkedHabitAndFinished(isLinked: Boolean) {
        dataStoreManager.setIsLinkedHabitAndFinished(isLinked)
    }

    override suspend fun setSetsTimer(sets: Int) {
        dataStoreManager.setSetsTimer(sets)
    }

    override suspend fun setRestIntervalHourTimer(hour: Int) {
        dataStoreManager.setRestIntervalHourTimer(hour)
    }

    override suspend fun setRestIntervalMinuteTimer(minute: Int) {
        dataStoreManager.setRestIntervalMinuteTimer(minute)
    }

    override suspend fun setRestIntervalSecondTimer(second: Int) {
        dataStoreManager.setRestIntervalSecondTimer(second)
    }

    override suspend fun setIdTimerSelected(id: Long) {
        dataStoreManager.setIdTimerSelected(id)
    }

    override suspend fun setDateTimerSelected(date: String) {
        dataStoreManager.setDateTimerSelected(date)
    }

    override suspend fun setTypeTimerSelected(type: Int) {
        dataStoreManager.setTypeTimerSelected(type)
    }

    override suspend fun setHourWheelTimer(hour: Int) {
        dataStoreManager.setHourWheelTimer(hour)
    }

    override suspend fun setMinuteWheelTimer(minute: Int) {
        dataStoreManager.setMinuteWheelTimer(minute)
    }

    override suspend fun setSecondWheelTimer(second: Int) {
        dataStoreManager.setSecondWheelTimer(second)
    }

    override suspend fun setTimerData(id: Long, date: String, type: Int, time: Triple<Int,Int,Int>) {
        dataStoreManager.setTimerData(id, date, type, time)
    }

    override suspend fun setLastSearched(uid: String, date: String) {
        dataStoreManager.setLastSearched(uid, date)
    }
}