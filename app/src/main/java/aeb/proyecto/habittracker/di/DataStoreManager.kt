package aeb.proyecto.habittracker.di

import android.content.Context
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

}

data class EmailPassword(
    val email: String,
    val password: String
)