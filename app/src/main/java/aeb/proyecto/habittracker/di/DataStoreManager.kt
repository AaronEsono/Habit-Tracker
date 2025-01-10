package aeb.proyecto.habittracker.di

import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private companion object {
        private val THEMEMODE = intPreferencesKey("themeMode")
        private val EMAIL = stringPreferencesKey("email")
        private val PASSWORD = stringPreferencesKey("password")
        private val CURRENTID = stringPreferencesKey("currentId")
        private val DATE  =stringPreferencesKey("date")
        private val SEARCHED = booleanPreferencesKey("searched")
    }

    val themeMode: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[THEMEMODE]
    }

    val emailPassword:Flow<EmailPassword?> = context.dataStore.data.map { preferences ->
        EmailPassword(
            email = preferences[EMAIL] ?: "",
            password = preferences[PASSWORD] ?: ""
        )
    }

    suspend fun setModeTheme(themeMode: Int) {
        context.dataStore.edit { preferences ->
            preferences[THEMEMODE] = themeMode
        }
    }

    suspend fun setEmail(email:String){
        context.dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    suspend fun setPassword(password:String){
        context.dataStore.edit { preferences ->
            preferences[PASSWORD] = password
        }
    }

    suspend fun clearDataUser(){
        context.dataStore.edit { preferences ->
            preferences[EMAIL] = ""
            preferences[PASSWORD] = ""
        }
    }

    suspend fun setLastSearched(uid:String, date:String){
        context.dataStore.edit { preferences ->
            preferences[CURRENTID] = uid
            preferences[DATE] = date
            preferences[SEARCHED] = true
        }
    }

    fun getLastSearched(): Flow<LastSearched> {
        return context.dataStore.data.map { preferences ->
            LastSearched(
                uid = preferences[CURRENTID] ?: "",
                date = preferences[DATE] ?: "",
                searched = preferences[SEARCHED] ?: false
            )
        }
    }
}

data class EmailPassword(
    val email: String,
    val password: String
)

data class LastSearched(
    val uid:String? = "",
    val date:String? = "",
    val searched:Boolean = false
)