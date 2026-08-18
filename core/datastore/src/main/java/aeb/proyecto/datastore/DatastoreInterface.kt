package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.datastore.model.LastSearched
import aeb.proyecto.datastore.model.UserSession
import kotlinx.coroutines.flow.Flow

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

    // LOGIN SCREEN ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Commits a holistic authentication credential payload mutation into persistent storage.
     *
     * This operation processes both user identification and validation tokens inside an isolated
     * atomic transaction boundary to ensure credentials never fall out of sync.
     *
     * @param session The target immutable [UserSession] state structure to serialize.
     */
    suspend fun saveUserSession(session: UserSession)

    /**
     * Non-blocking query extraction reading the active cached authentication session snapshot.
     *
     * @return The current [UserSession] state metadata, or an unauthenticated fallback structure
     * if the storage layer is empty.
     */
    suspend fun getUserSession():UserSession

    /**
     * Atomically purges all localized authentication credentials from the persistent storage layer.
     *
     * This operation resets the session context to an anonymous state, triggering downstream
     * reactive gateway navigation events.
     */
    suspend fun clearSession()

    // LOGIN SCREEN ----------------------------------------------------------------
    // *******************************************************************************************

    // HABIT SCREEN ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Non-blocking operational query extracting the instantaneous cached habit classification filter.
     *
     * @return The active localized category string token, or null if no filtering boundary
     * has been established.
     */
    suspend fun getTypeSelected():String?

    /**
     * Commits a structural habit classification type filter override to persistent storage.
     *
     * @param type The target category or classification string token to persist.
     */
    suspend fun setTypeSelected(type:String)
    // HABIT SCREEN ----------------------------------------------------------------
    // *******************************************************************************************

    // TIMER SCREEN ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Cold reactive stream emitting the unique database identifier (ID) of the habit linked to the current timer session.
     * Emits null if no habit is currently associated.
     */
    val idHabitLinkedTimer:Flow<Long?>

    /**
     * Cold reactive stream emitting the target calendar date string linked to the current timer tracking session.
     */
    val dateHabitLinkedTimer:Flow<String?>

    /**
     * Cold reactive stream emitting the active timer operational mode identifier token (e.g., Stopwatch or Countdown).
     */
    val typeTimerSelected:Flow<Int?>

    /**
     * Cold reactive stream emitting the formatted state representation of the primary countdown hour picker.
     */
    val wheelHourSelected:Flow<String>

    /**
     * Cold reactive stream emitting the formatted state representation of the auxiliary rest interval hour picker.
     */
    val restHourSelected:Flow<String>

    /**
     * Cold reactive stream emitting the total targeted loop iteration or interval sets count.
     */
    val numberSetsTimerSelected:Flow<Int?>

    /**
     * Non-blocking query extracting the instantaneous cached database identifier of the linked habit profile.
     *
     * @return The active localized long primary key, or null if unlinked.
     */
    suspend fun getIdHabitLinkedTimer():Long?

    /**
     * Non-blocking query extracting the instantaneous cached tracking target date string.
     *
     * @return The localized calendar date string snapshot, or null if completely uninitialized.
     */
    suspend fun getDateHabitLinkedTimer():String?

    /**
     * Commits the current hours position within the auxiliary rest interval duration wheel picker.
     *
     * @param hour The target duration value to serialize.
     */
    suspend fun setRestIntervalHourTimer(hour:Int)

    /**
     * Commits the current minutes position within the auxiliary rest interval duration wheel picker.
     *
     * @param minute The target duration value to serialize.
     */
    suspend fun setRestIntervalMinuteTimer(minute:Int)

    /**
     * Commits the current seconds position within the auxiliary rest interval duration wheel picker.
     *
     * @param second The target duration value to serialize.
     */
    suspend fun setRestIntervalSecondTimer(second:Int)

    /**
     * Commits the current hours position within the primary countdown duration wheel picker.
     *
     * @param hour The target duration value to serialize.
     */
    suspend fun setHourWheelTimer(hour:Int)

    /**
     * Commits the current minutes position within the primary countdown duration wheel picker.
     *
     * @param minute The target duration value to serialize.
     */
    suspend fun setMinuteWheelTimer(minute:Int)

    /**
     * Commits the current seconds position within the primary countdown duration wheel picker.
     *
     * @param second The target duration value to serialize.
     */
    suspend fun setSecondWheelTimer(second:Int)

    /**
     * Commits the total targeted loop interval sets count independently.
     *
     * @param sets The absolute target repetition index to persist.
     */
    suspend fun setNumberSetsTimer(sets:Int)

    /**
     * Commits a structural database identifier token override to link a specific habit profile to the active timer session.
     *
     * @param id The target database record long primary key to persist.
     */
    suspend fun setIdHabitLinkedTimer(id:Long)

    /**
     * Commits the target scheduling calendar date string boundary independently.
     *
     * @param date The target calendar reference sequence to persist.
     */
    suspend fun setDateHabitLinkedTimer(date:String)

    /**
     * Commits the active timer operational behavior mode token independently.
     *
     * @param type The targeted structural mode configuration token to persist.
     */
    suspend fun setTypeTimerSelected(type:Int)
    // TIMER SCREEN ----------------------------------------------------------------
    // *******************************************************************************************

    // STOPWATCH SERVICE ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Cold reactive stream tracking the stateful validation marker verifying if the active
     * tracking session completed successfully with an attached habit connection profile.
     */
    val timerLinkedAndFinished:Flow<Boolean>

    /**
     * Overrides the stateful linked habit completion validation flag inside infrastructure storage.
     *
     * @param isLinked The target confirmation state visibility token to serialize.
     */
    suspend fun setIsLinkedHabitAndFinished(isLinked:Boolean)

    /**
     * Non-blocking query extraction pipeline reading the instantaneous elapsed timing snapshot.
     *
     * @return The current long tracking duration, or null if uninitialized.
     */
    suspend fun getTimePassedTimer():Long?

    /**
     * Commits an updated elapsed duration metric independently into localized storage.
     *
     * @param time The current absolute time metric sequence to persist.
     */
    suspend fun setTimePassedTimer(time:Long)
    // STOPWATCH SERVICE ----------------------------------------------------------------
    // *******************************************************************************************

    // ONBOARD SCREEN ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Observes whether the onboarding screen should be displayed.
     *
     * Emits `true` when the onboarding screen should be shown,
     * or `false` when it should not be displayed.
     */
    val showOnboardScreen: Flow<Boolean>

    /**
     * Updates whether the onboarding screen should be displayed.
     *
     * @param showOnboardScreen `true` to display the onboarding screen,
     * or `false` to prevent it from being displayed.
     */
    suspend fun setShowOnboardScreen(showOnboardScreen: Boolean)
    // ONBOARD SCREEN ----------------------------------------------------------------
    // *******************************************************************************************

}