package aeb.proyecto.settings

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.domain.usecase.settings.DataSettingsUseCase
import aeb.proyecto.domain.usecase.settings.SetLanguageUseCase
import aeb.proyecto.domain.usecase.settings.SettingsAuthenticationUseCase
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.model.SettingsDialogState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

/**
 * ViewModel responsible for managing the application's configuration states.
 * Handles user preferences (theme, language, start of week) and dialog interactions.
 *
 * @property dataSettingsUseCase Interacts with DataStore to read/write preferences.
 * @property setLanguageUseCase Handles the application-level locale changes.
 * @property settingsAuthenticationUseCase Verifies the current user session.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataSettingsUseCase: DataSettingsUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val settingsAuthenticationUseCase: SettingsAuthenticationUseCase
):ViewModel() {

    // Dialog state management
    private val _settingDialogState:MutableStateFlow<SettingsDialogState> = MutableStateFlow(SettingsDialogState())
    val settingDialogState:StateFlow<SettingsDialogState> = _settingDialogState.asStateFlow()

    /**
     * Reactive state stream of the application settings.
     * Maps the underlying DataStore flow directly to a [SettingsUIState].
     */
    val settingsUIState:StateFlow<SettingsUIState> = dataSettingsUseCase.dataSettings
        .map<AppSettings,SettingsUIState>{
            SettingsUIState.Success(it)
        }
        .catch {
            emit(SettingsUIState.Error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUIState.Loading
        )

    /**
     * Checks if there is an active authenticated user session.
     */
    fun getCurrentUser():Boolean{
        return settingsAuthenticationUseCase.getCurrentUser()
    }

    /**
     * Toggles the visibility of the settings dialog.
     */
    fun setStateDialog(state:Boolean){
        _settingDialogState.update { currentState ->
            currentState.copy(showDialog = state)
        }
    }

    /**
     * Prepares and displays a specific dialog mode (e.g., Theme selection, Language selection).
     * @param dataDialog The configuration defining which dialog to show.
     */
    fun setDataDialogMode(dataDialog: DataDialog) {
        _settingDialogState.update { currentState ->
            currentState.copy(dataDialog = dataDialog, showDialog = true)
        }
    }

    /**
     * Processes the user's selection from a dialog, applies the specific setting,
     * updates the DataStore, and dismisses the dialog.
     *
     * @param dataResult The encapsulated result from the dialog interaction.
     */
    fun treatResultDialog(dataResult: DataResult) {
        viewModelScope.launch {
            var appSettings = dataSettingsUseCase.getAppSettings()

            when(dataResult){
                is DataResult.LanguageResult -> {
                    appSettings = appSettings.copy(language = dataResult.language)
                    setLanguageUseCase.setLanguage(dataResult.language)
                }
                is DataResult.ThemeResult -> {
                    appSettings = appSettings.copy(themeMode = dataResult.theme)
                }
                is DataResult.DayOfWeekResult -> {
                    appSettings = appSettings.copy(dayStartWeek = dataResult.dayOfWeek.name)
                }
            }

            setStateDialog(false)
            dataSettingsUseCase.setAppSettings(appSettings)
        }
    }
}

/**
 * Represents the UI states for the main Settings screen.
 */
sealed class SettingsUIState(){
    data object Loading: SettingsUIState()
    data object Error: SettingsUIState()
    data class Success(val data: AppSettings): SettingsUIState()
}