package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.EmailPassword
import aeb.proyecto.datastore.model.LastSearched
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        private val THEME_MODE = intPreferencesKey("themeMode")
        private val LANGUAGE = stringPreferencesKey("language")
        private val EMAIL = stringPreferencesKey("email")
        private val PASSWORD = stringPreferencesKey("password")
        private val DAY_START_WEEK = stringPreferencesKey("dayStartWeek")
        private val CURRENT_ID = stringPreferencesKey("currentId")
        private val DATE  = stringPreferencesKey("date")
        private val SEARCHED = booleanPreferencesKey("searched")
    }

    val themeMode: Flow<Int> = dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: 0
    }

    val languageMode: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE]?: ""
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

    suspend fun setDayStartWeek(day:String){
        dataStore.edit { preferences ->
            preferences[DAY_START_WEEK] = day
        }
    }

    suspend fun setLanguage(language:String){
        dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }

    suspend fun setModeTheme(themeMode: Int) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode
        }
    }

    suspend fun setEmail(email:String){
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    suspend fun setPassword(password:String){
        dataStore.edit { preferences ->
            preferences[PASSWORD] = password
        }
    }

    suspend fun setLastSearched(uid:String, date:String){
        dataStore.edit { preferences ->
            preferences[CURRENT_ID] = uid
            preferences[DATE] = date
            preferences[SEARCHED] = true
        }
    }

    suspend fun clearDataUser(){
        dataStore.edit { preferences ->
            preferences[EMAIL] = ""
            preferences[PASSWORD] = ""
        }
    }

    suspend fun saveFirstDayOfWeek(){
        val firstDay = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        dataStore.edit { preferences ->
            preferences[DAY_START_WEEK] = firstDay.name
        }
    }
}