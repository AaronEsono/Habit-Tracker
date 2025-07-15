package aeb.proyecto.datastore

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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        //Settings
        private val THEME_MODE = intPreferencesKey("themeMode")
        private val LANGUAGE = stringPreferencesKey("language")
        private val DAY_START_WEEK = stringPreferencesKey("dayStartWeek")

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

    val themeMode: Flow<Int> = dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: 0
    }

    val languageMode: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE] ?: ""
    }

    val dayOfWeek: Flow<String> = dataStore.data.map { preferences ->
        preferences[DAY_START_WEEK] ?: DayOfWeek.MONDAY.name
    }

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

    suspend fun getDayStartWeek() = dataStore.data.map { preferences ->
        preferences[DAY_START_WEEK]
    }.firstOrNull()

    suspend fun getLanguage() = dataStore.data.map { preferences ->
        preferences[LANGUAGE]
    }.firstOrNull()

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

        suspend fun setDayStartWeek(day: String) {
            dataStore.edit { preferences ->
                preferences[DAY_START_WEEK] = day
            }
        }

        suspend fun setLanguage(language: String) {
            dataStore.edit { preferences ->
                preferences[LANGUAGE] = language
            }
        }

        suspend fun setModeTheme(themeMode: Int) {
            dataStore.edit { preferences ->
                preferences[THEME_MODE] = themeMode
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

        suspend fun setFirstDayOfWeek() {
            val firstDayOfWeek =
                DayOfWeek.of(Calendar.getInstance(Locale.getDefault()).firstDayOfWeek).name
            dataStore.edit { preferences ->
                preferences[DAY_START_WEEK] = firstDayOfWeek
            }
        }
}