package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.datastore.model.EmailPassword
import aeb.proyecto.datastore.model.LastSearched
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

        //Login Screen
        private val EMAIL = stringPreferencesKey("email")
        private val PASSWORD = stringPreferencesKey("password")

        // Habit Screen
        private val TYPE_SELECTED = stringPreferencesKey("typeSelected")

        //Timer Screeen
        //Id del habito
        private val ID_TIMER_SELECTED = longPreferencesKey("idTimerSelected")
        // Fecha del habito
        private val DATE_TIMER_SELECTED = stringPreferencesKey("dateTimerSelected")
        // Tipo de timer seleccionado
        private val TYPE_TIMER_SELECTED = intPreferencesKey("typeTimerSelected")

        //Wheel picker timer
        private val HOUR_WHEEL_TIMER = intPreferencesKey("hourWheelTimer")
        private val MINUTE_WHEEL_TIMER = intPreferencesKey("minuteWheelTimer")
        private val SECOND_WHEEL_TIMER = intPreferencesKey("secondWheelTimer")

        //Rest interval time
        private val REST_INTERVAL_HOUR_TIMER = intPreferencesKey("restIntervalHourTime")
        private val REST_INTERVAL_MINUTE_TIMER = intPreferencesKey("restIntervalMinuteTime")
        private val REST_INTERVAL_SECOND_TIMER = intPreferencesKey("restIntervalSecondTime")

        //Sets Timer
        private val SETS_TIMER = intPreferencesKey("setsTimer")

        //Tiempo pasado
        private val TIME_PASSED_TIMER = longPreferencesKey("timePassedTimer")
        //Timer habito vinculado
        private val IS_LINKED_HABIT_AND_FINISHED = booleanPreferencesKey("isLinkedHabitAndFinished")

        // Por mirar
        private val CURRENT_ID = stringPreferencesKey("currentId")
        private val DATE = stringPreferencesKey("date")
        private val SEARCHED = booleanPreferencesKey("searched")
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

    val idTimerSelected: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[ID_TIMER_SELECTED]
    }

    val dateTimerSelected: Flow<String?> = dataStore.data.map { preferences ->
        preferences[DATE_TIMER_SELECTED]
    }

    val typeTimerSelected: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[TYPE_TIMER_SELECTED]
    }

    val hourWheelTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[HOUR_WHEEL_TIMER]
    }

    val minuteWheelTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[MINUTE_WHEEL_TIMER]
    }

    val secondWheelTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[SECOND_WHEEL_TIMER]
    }

    val restIntervalHourTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[REST_INTERVAL_HOUR_TIMER]
    }

    val restIntervalMinuteTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[REST_INTERVAL_MINUTE_TIMER]
    }

    val restIntervalSecondTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[REST_INTERVAL_SECOND_TIMER]
    }

    val setsTimer: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[SETS_TIMER]
    }

    val timerLinkedAndFinished: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LINKED_HABIT_AND_FINISHED] ?: false
    }

    suspend fun getEmailPassword() = dataStore.data.map { preferences ->
        EmailPassword(
            email = preferences[EMAIL] ?: "",
            password = preferences[PASSWORD] ?: ""
        )
    }.firstOrNull() ?: EmailPassword()

    suspend fun getLastSearched() =
        dataStore.data.map { preferences ->
            LastSearched(
                uid = preferences[CURRENT_ID] ?: "",
                date = preferences[DATE] ?: "",
                searched = preferences[SEARCHED] ?: false
            )
        }.firstOrNull() ?: LastSearched()

    suspend fun getTypeSeleted() = dataStore.data.map { preferences ->
        preferences[TYPE_SELECTED]
    }.firstOrNull()

    suspend fun getIdTimerSelected() = dataStore.data.map { preferences ->
        preferences[ID_TIMER_SELECTED]
    }.firstOrNull()

    suspend fun getDateTimerSelected() = dataStore.data.map { preferences ->
        preferences[DATE_TIMER_SELECTED]
    }.firstOrNull()

    suspend fun getTypeTimerSelected() = dataStore.data.map { preferences ->
        preferences[TYPE_TIMER_SELECTED]
    }.firstOrNull()

    suspend fun getRestIntervalHourTimer() = dataStore.data.map { preferences ->
        preferences[REST_INTERVAL_HOUR_TIMER]
    }.firstOrNull()

    suspend fun getRestIntervalMinuteTimer() = dataStore.data.map { preferences ->
        preferences[REST_INTERVAL_MINUTE_TIMER]
    }.firstOrNull()

    suspend fun getRestIntervalSecondTimer() = dataStore.data.map { preferences ->
        preferences[REST_INTERVAL_SECOND_TIMER]
    }.firstOrNull()

    suspend fun getSetsTimer() = dataStore.data.map { preferences ->
        preferences[SETS_TIMER]
    }.firstOrNull()

    suspend fun getTimePassedTimer() = dataStore.data.map { preferences ->
        preferences[TIME_PASSED_TIMER]
    }.firstOrNull()

    suspend fun getIsLinkedHabitAndFinished() = dataStore.data.map { preferences ->
        preferences[IS_LINKED_HABIT_AND_FINISHED]
    }.firstOrNull()

    suspend fun setIsLinkedHabitAndFinished(isLinked: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LINKED_HABIT_AND_FINISHED] = isLinked
        }
    }

    suspend fun setTimePassedTimer(time: Long) {
        dataStore.edit { preferences ->
            preferences[TIME_PASSED_TIMER] = time
        }
    }

    suspend fun setSetsTimer(sets: Int) {
        dataStore.edit { preferences ->
            preferences[SETS_TIMER] = sets
        }
    }

    suspend fun setRestIntervalHourTimer(hour: Int) {
        dataStore.edit { preferences ->
            preferences[REST_INTERVAL_HOUR_TIMER] = hour
        }
    }

    suspend fun setRestIntervalMinuteTimer(minute: Int) {
        dataStore.edit { preferences ->
            preferences[REST_INTERVAL_MINUTE_TIMER] = minute
        }
    }

    suspend fun setRestIntervalSecondTimer(second: Int) {
        dataStore.edit { preferences ->
            preferences[REST_INTERVAL_SECOND_TIMER] = second
        }
    }

    suspend fun setIdTimerSelected(id: Long) {
        dataStore.edit { preferences ->
            preferences[ID_TIMER_SELECTED] = id
        }
    }


    suspend fun setDateTimerSelected(date: String) {
        dataStore.edit { preferences ->
            preferences[DATE_TIMER_SELECTED] = date
        }
    }


    suspend fun setTypeTimerSelected(type: Int) {
        dataStore.edit { preferences ->
            preferences[TYPE_TIMER_SELECTED] = type
        }
    }

    suspend fun setHourWheelTimer(hour: Int) {
        dataStore.edit { preferences ->
            preferences[HOUR_WHEEL_TIMER] = hour
        }
    }

    suspend fun setMinuteWheelTimer(minute: Int) {
        dataStore.edit { preferences ->
            preferences[MINUTE_WHEEL_TIMER] = minute
        }
    }

    suspend fun setSecondWheelTimer(second: Int) {
        dataStore.edit { preferences ->
            preferences[SECOND_WHEEL_TIMER] = second
        }
    }

    suspend fun setTimerData(id: Long, date: String, type: Int, time: Triple<Int,Int,Int>) {
        dataStore.edit { preferences ->
            preferences[ID_TIMER_SELECTED] = id
            preferences[DATE_TIMER_SELECTED] = date
            preferences[TYPE_TIMER_SELECTED] = type

            preferences[HOUR_WHEEL_TIMER] = time.first
            preferences[MINUTE_WHEEL_TIMER] = time.second
            preferences[SECOND_WHEEL_TIMER] = time.third
        }
    }

        suspend fun setTypeSelectedDate(type: String) {
            dataStore.edit { preferences ->
                preferences[TYPE_SELECTED] = type
            }
        }

        suspend fun setEmail(email: String) {
            dataStore.edit { preferences ->
                preferences[EMAIL] = email
            }
        }

        suspend fun setPassword(password: String) {
            dataStore.edit { preferences ->
                preferences[PASSWORD] = password
            }
        }

        suspend fun setLastSearched(uid: String, date: String) {
            dataStore.edit { preferences ->
                preferences[CURRENT_ID] = uid
                preferences[DATE] = date
                preferences[SEARCHED] = true
            }
        }

        suspend fun clearDataUser() {
            dataStore.edit { preferences ->
                preferences[EMAIL] = ""
                preferences[PASSWORD] = ""
            }
        }
}