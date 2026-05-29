package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.datastore.model.LastSearched
import aeb.proyecto.datastore.model.UserSession
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Enterprise-grade Infrastructure Engine coordinating local reactive key-value serialization.
 *
 * This manager centralizes file-system transactions over the underlying [DataStore] binary
 * file. Actively decorated as a [@Singleton], it guarantees a structural single-source-of-truth
 * pipeline across the global application context, pattern-preventing race conditions or concurrent
 * file-lock mutation faults.
 *
 * All data extraction processes expose structural cold streams ([kotlinx.coroutines.flow.Flow]),
 * while state mutators enforce atomic, non-blocking coroutine suspension boundaries.
 *
 * @property dataStore The single-pointer, thread-safe platform interface handling data serialization workflows.
 */
@Singleton
class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        // SETTINGS ----------------------------------------------------------------
        // *******************************************************************************************
        /**
         * Internal preference key mapping the active visual presentation user interface mode.
         */
        private val THEME_MODE = intPreferencesKey("themeMode")

        /**
         * Internal preference key tracking the ISO-639 localization configuration code sequence.
         */
        private val LANGUAGE = stringPreferencesKey("language")

        /**
         * Internal preference key mapping the preferred chronological regional first day of the week.
         */
        private val DAY_START_WEEK = stringPreferencesKey("dayStartWeek")
        // SETTINGS ----------------------------------------------------------------
        // *******************************************************************************************

        // STATISTICS ----------------------------------------------------------------
        // *******************************************************************************************
        /**
         * Internal preference key mapping the database unique identifier (ID) of the
         * structurally focused habit instance within the statistics and analytical dashboards.
         */
        private val HABIT_SELECTED = longPreferencesKey("habitSelected")
        // STATISTICS ----------------------------------------------------------------
        // *******************************************************************************************

        // LOGIN SCREEN ----------------------------------------------------------------
        // *******************************************************************************************
        /**
         * Internal preference key mapping the active user account identification sequence.
         */
        private val EMAIL = stringPreferencesKey("email")

        /**
         * Internal preference key mapping the stateful account ownership verification token.
         */
        private val PASSWORD = stringPreferencesKey("password")

        // LOGIN SCREEN ----------------------------------------------------------------
        // *******************************************************************************************

        // HABIT SCREEN ----------------------------------------------------------------
        // *******************************************************************************************
        /**
         * Internal preference key mapping the active structural category or behavior classification
         * filter selected by the user within the habit management workflow.
         */
        private val TYPE_SELECTED = stringPreferencesKey("typeSelected")
        // HABIT SCREEN ----------------------------------------------------------------
        // *******************************************************************************************

        // TIMER SCREEN ----------------------------------------------------------------
        // *******************************************************************************************
        /**
         * Internal preference key mapping the database unique identifier (ID) of the habit
         * currently linked to the active timer session for post-completion tracking.
         */
        private val ID_HABIT_LINKED_TIMER = longPreferencesKey("idHabitLinkedTimer")

        /**
         * Internal preference key mapping the targeted persistence calendar date string boundary
         * representing when the accumulated timer session duration should merge.
         */
        private val DATE_TIMER_SELECTED = stringPreferencesKey("dateTimerSelected")

        /**
         * Internal preference key mapping the active operational behavior mode token of the timer
         * (e.g., Stopwatch, Countdown, or Interval Sets configuration).
         */
        private val TYPE_TIMER_SELECTED = intPreferencesKey("typeTimerSelected")



        // Primary Wheel Picker
        /**
         * Internal preference key mapping the current hours position within the primary
         * countdown duration wheel picker.
         */
        private val HOUR_WHEEL_TIMER = intPreferencesKey("hourWheelTimer")

        /**
         * Internal preference key mapping the current minutes position within the primary
         * countdown duration wheel picker.
         */
        private val MINUTE_WHEEL_TIMER = intPreferencesKey("minuteWheelTimer")

        /**
         * Internal preference key mapping the current seconds position within the primary
         * countdown duration wheel picker.
         */
        private val SECOND_WHEEL_TIMER = intPreferencesKey("secondWheelTimer")



        // Rest Interval Time
        /**
         * Internal preference key mapping the current hours position within the auxiliary
         * rest interval duration wheel picker.
         */
        private val REST_INTERVAL_HOUR_TIMER = intPreferencesKey("restIntervalHourTime")

        /**
         * Internal preference key mapping the current minutes position within the auxiliary
         * rest interval duration wheel picker.
         */
        private val REST_INTERVAL_MINUTE_TIMER = intPreferencesKey("restIntervalMinuteTime")

        /**
         * Internal preference key mapping the current seconds position within the auxiliary
         * rest interval duration wheel picker.
         */
        private val REST_INTERVAL_SECOND_TIMER = intPreferencesKey("restIntervalSecondTime")

        // Sets Count
        /**
         * Internal preference key mapping the total targeted loop iteration metrics or interval sets
         * to process inside the structured session workflow.
         */
        private val NUMBER_SETS_TIMER_SELECTED = intPreferencesKey("numberSetsTimerSelected")
        // TIMER SCREEN ----------------------------------------------------------------
        // *******************************************************************************************

        // STOPWATCH SERVICE ----------------------------------------------------------------
        // *******************************************************************************************
        /**
         * Internal preference key tracking the cumulative milliseconds or seconds sequence
         * that has elapsed during the active background timing execution.
         */
        private val TIME_PASSED_TIMER = longPreferencesKey("timePassedTimer")

        /**
         * Internal preference key flagging whether a tracking session had an associated habit profile
         * and successfully reached its completion state topology boundary.
         */
        private val IS_LINKED_HABIT_AND_FINISHED = booleanPreferencesKey("isLinkedHabitAndFinished")
        // STOPWATCH SERVICE ----------------------------------------------------------------
        // *******************************************************************************************
    }

    // SETTINGS ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Global multi-preference configuration execution pipeline stream.
     * * Emits an updated immutable [AppSettings] snapshot mapping every transactional mutation
     * happening within the file-system boundary.
     */
    val appSettings: Flow<AppSettings> = dataStore.data.map { preferences ->
        AppSettings(
            themeMode = preferences[THEME_MODE] ?: 0,
            language = preferences[LANGUAGE] ?: "",
            dayStartWeek = preferences[DAY_START_WEEK] ?: DayOfWeek.MONDAY.name
        )
    }

    /**
     * Filtered reactive stream tracking structural modifications over the visual theme token.
     * * Employs distinct memory evaluation gating to suppress redundant downstream presentation
     * recompositions if the underlying integer identity remains unaltered.
     */
    val themeMode: Flow<Int> = appSettings.map { it.themeMode }.distinctUntilChanged()

    /**
     * Filtered reactive stream tracking structural modifications over the internationalization language code.
     * * Employs distinct memory evaluation gating to suppress redundant downstream presentation
     * recompositions if the underlying string identity remains unaltered.
     */
    val languageMode: Flow<String> = appSettings.map { it.language }.distinctUntilChanged()

    /**
     * Filtered reactive stream tracking structural modifications over the first day of the week identifier.
     * * Employs distinct memory evaluation gating to suppress redundant downstream presentation
     * recompositions if the underlying string identity remains unaltered.
     */
    val dayOfWeek: Flow<String> = appSettings.map { it.dayStartWeek }.distinctUntilChanged()

    /**
     * Commits a holistic, multi-preference configuration payload mutation into disk storage.
     *
     * This transaction operation synchronizes all underlying system values inside a single,
     * atomic write block, minimizing physical file descriptor locks.
     *
     * @param settings The target immutable [AppSettings] layout structure to serialize.
     */
    suspend fun saveAppSettings(settings: AppSettings) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = settings.themeMode
            preferences[LANGUAGE] = settings.language
            preferences[DAY_START_WEEK] = settings.dayStartWeek
        }
    }

    /**
     * Non-blocking query extraction pipeline reading the active cached configuration snapshot.
     *
     * @return The current stateful configuration mapping payload, or an empty fallback [AppSettings]
     * reference structure if the underlying binary pointer is unestablished.
     */
    suspend fun getAppSettings(): AppSettings = appSettings.firstOrNull() ?: AppSettings()

    /**
     * Automatically inspects the primary workstation regional configuration parameters to force-initialize
     * the regional calendar start marker boundary.
     *
     * This operation processes values dynamically based on native runtime system settings
     * and merges the localized mutation cleanly into the master configuration layout structure.
     */
    suspend fun setFirstDayOfWeek() {
        val firstDayOfWeek = DayOfWeek.of(Calendar.getInstance(Locale.getDefault()).firstDayOfWeek).name
        dataStore.edit { preferences ->
            preferences[DAY_START_WEEK] = firstDayOfWeek
        }
    }
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
    val habitSelected: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[HABIT_SELECTED]
    }

    /**
     * Commits a structural database identifier token override to focus a specific habit profile.
     *
     * @param id The target database record long primary key to persist.
     */
    suspend fun setHabitSelected(id: Long) {
        dataStore.edit { preferences ->
            preferences[HABIT_SELECTED] = id
        }
    }
    // STATISTICS ----------------------------------------------------------------
    // *******************************************************************************************

    // LOGIN SCREEN ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Global reactive authentication stream emitting the active local session topology snapshot.
     *
     * Downstream security interceptors or login gateway layout routers subscribing to this pipeline
     * are insulated against redundant evaluation ticks if credentials remain structurally unchanged.
     */
    val userSession: Flow<UserSession> = dataStore.data.map { preferences ->
        UserSession(
            email = preferences[EMAIL] ?: "",
            password = preferences[PASSWORD] ?: ""
        )
    }.distinctUntilChanged()

    /**
     * Commits a holistic authentication credential payload mutation into persistent storage.
     *
     * @param session The target immutable [UserSession] state structure to serialize.
     */
    suspend fun saveUserSession(session: UserSession) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = session.email
            preferences[PASSWORD] = session.password
        }
    }

    /**
     * Non-blocking query extraction pipeline reading the active cached session token snapshot.
     *
     * @return The current [UserSession] state metadata, or an unauthenticated fallback structure
     * if the file-system pointer is empty.
     */
    suspend fun getUserSession(): UserSession = userSession.firstOrNull() ?: UserSession()

    /**
     * Atomically purges all localized authentication credentials from the persistent storage layer.
     *
     * This operation resets the session context to an anonymous state, triggering downstream
     * reactive gateway navigation events if applicable.
     */
    suspend fun clearSession() {
        saveUserSession(UserSession())
    }
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
    suspend fun getTypeSelected() = dataStore.data.map { preferences ->
        preferences[TYPE_SELECTED]
    }.firstOrNull()

    /**
     * Commits a structural habit classification type filter override to persistent storage.
     *
     * @param type The target category or classification string token to persist.
     */
    suspend fun setTypeSelectedDate(type: String) {
        dataStore.edit { preferences ->
            preferences[TYPE_SELECTED] = type
        }
    }
    // HABIT SCREEN ----------------------------------------------------------------
    // *******************************************************************************************

    // TIMER SCREEN ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Cold reactive stream emitting the unique identifier (ID) of the habit linked to the current timer session.
     */
    val idHabitLinkedSelected: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[ID_HABIT_LINKED_TIMER]
    }

    /**
     * Cold reactive stream emitting the target calendar date string assigned to the active tracking session.
     */
    val dateHabitLinkedSelected: Flow<String?> = dataStore.data.map { preferences ->
        preferences[DATE_TIMER_SELECTED]
    }

    /**
     * Cold reactive stream emitting the active timer operational mode index configuration token.
     */
    val typeTimerSelected: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[TYPE_TIMER_SELECTED]
    }

    /**
     * Cold reactive stream tracking the current raw hour index selection of the primary countdown wheels.
     */
    val hourWheelTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[HOUR_WHEEL_TIMER]
    }

    /**
     * Cold reactive stream tracking the current raw minute index selection of the primary countdown wheels.
     */
    val minuteWheelTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[MINUTE_WHEEL_TIMER]
    }

    /**
     * Cold reactive stream tracking the current raw second index selection of the primary countdown wheels.
     */
    val secondWheelTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[SECOND_WHEEL_TIMER]
    }

    /**
     * Cold reactive stream tracking the auxiliary hours selection dedicated to loop rest intervals.
     */
    val restIntervalHourTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[REST_INTERVAL_HOUR_TIMER]
    }

    /**
     * Cold reactive stream tracking the auxiliary minutes selection dedicated to loop rest intervals.
     */
    val restIntervalMinuteTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[REST_INTERVAL_MINUTE_TIMER]
    }

    /**
     * Cold reactive stream tracking the auxiliary seconds selection dedicated to loop rest intervals.
     */
    val restIntervalSecondTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[REST_INTERVAL_SECOND_TIMER]
    }

    /**
     * Cold reactive stream tracking the active loop repetition target limit or intervals count.
     */
    val numberSetsTimerSelected: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[NUMBER_SETS_TIMER_SELECTED]
    }

    /**
     * Non-blocking query extraction pipeline reading the instantaneous habit ID boundary snapshot.
     */
    suspend fun getIdHabitLinkedSelected() = dataStore.data.map { preferences ->
        preferences[ID_HABIT_LINKED_TIMER]
    }.firstOrNull()

    /**
     * Non-blocking query extraction pipeline reading the instantaneous tracking date string snapshot.
     */
    suspend fun getDateHabitLinkedSelected() = dataStore.data.map { preferences ->
        preferences[DATE_TIMER_SELECTED]
    }.firstOrNull()

    /**
     * Commits the target rest interval hours component independently into local storage.
     */
    suspend fun setRestIntervalHourTimer(hour: Int) {
        dataStore.edit { preferences ->
            preferences[REST_INTERVAL_HOUR_TIMER] = hour
        }
    }

    /**
     * Commits the target rest interval minutes component independently into local storage.
     */
    suspend fun setRestIntervalMinuteTimer(minute: Int) {
        dataStore.edit { preferences ->
            preferences[REST_INTERVAL_MINUTE_TIMER] = minute
        }
    }

    /**
     * Commits the target rest interval seconds component independently into local storage.
     */
    suspend fun setRestIntervalSecondTimer(second: Int) {
        dataStore.edit { preferences ->
            preferences[REST_INTERVAL_SECOND_TIMER] = second
        }
    }

    /**
     * Overrides the active operational configuration behavior mode token inside infrastructure storage.
     */
    suspend fun setTypeTimerSelected(type: Int) {
        dataStore.edit { preferences ->
            preferences[TYPE_TIMER_SELECTED] = type
        }
    }

    /**
     * Commits the target countdown hours component independently into local storage.
     */
    suspend fun setHourWheelTimer(hour: Int) {
        dataStore.edit { preferences ->
            preferences[HOUR_WHEEL_TIMER] = hour
        }
    }

    /**
     * Commits the target countdown minutes component independently into local storage.
     */
    suspend fun setMinuteWheelTimer(minute: Int) {
        dataStore.edit { preferences ->
            preferences[MINUTE_WHEEL_TIMER] = minute
        }
    }

    /**
     * Commits the target countdown seconds component independently into local storage.
     */
    suspend fun setSecondWheelTimer(second: Int) {
        dataStore.edit { preferences ->
            preferences[SECOND_WHEEL_TIMER] = second
        }
    }

    /**
     * Commits the total loop interval target iteration metric independently into local storage.
     */
    suspend fun setNumberSetsTimerSelected(sets: Int) {
        dataStore.edit { preferences ->
            preferences[NUMBER_SETS_TIMER_SELECTED] = sets
        }
    }

    /**
     * Binds a specific habit profile structure long primary key safely into the local configuration context.
     */
    suspend fun setIdHabitLinkedSelected(id: Long) {
        dataStore.edit { preferences ->
            preferences[ID_HABIT_LINKED_TIMER] = id
        }
    }

    /**
     * Overrides the tracking system target calendar execution date reference sequence.
     */
    suspend fun setDateHabitLinkedSelected(date: String) {
        dataStore.edit { preferences ->
            preferences[DATE_TIMER_SELECTED] = date
        }
    }
    // TIMER SCREEN ----------------------------------------------------------------
    // *******************************************************************************************

    // STOPWATCH SERVICE ----------------------------------------------------------------
    // *******************************************************************************************
    /**
     * Cold reactive stream tracking the stateful validation marker verifying if the active
     * tracking session completed successfully with an attached habit connection profile.
     */
    val timerLinkedAndFinished: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LINKED_HABIT_AND_FINISHED] ?: false
    }

    /**
     * Non-blocking query extraction pipeline reading the instantaneous elapsed timing snapshot.
     *
     * @return The current long tracking duration, or null if the system hasn't initialized a session.
     */
    suspend fun getTimePassedTimer() = dataStore.data.map { preferences ->
        preferences[TIME_PASSED_TIMER]
    }.firstOrNull()

    /**
     * Overrides the stateful linked habit completion validation flag inside infrastructure storage.
     *
     * @param isLinked The target confirmation state visibility token to serialize.
     */
    suspend fun setIsLinkedHabitAndFinished(isLinked: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LINKED_HABIT_AND_FINISHED] = isLinked
        }
    }

    /**
     * Commits an updated elapsed duration metric independently into localized storage.
     *
     * @param time The current absolute time metric sequence to persist.
     */
    suspend fun setTimePassedTimer(time: Long) {
        dataStore.edit { preferences ->
            preferences[TIME_PASSED_TIMER] = time
        }
    }
    // STOPWATCH SERVICE ----------------------------------------------------------------
    // *******************************************************************************************
}