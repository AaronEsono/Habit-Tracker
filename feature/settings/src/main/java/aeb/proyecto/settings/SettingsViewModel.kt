package aeb.proyecto.settings

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.domain.usecase.settings.DataSettingsUseCase
import aeb.proyecto.domain.usecase.settings.SetLanguageUseCase
import aeb.proyecto.domain.usecase.settings.SetValueDataStoreSettingsUseCase
import aeb.proyecto.domain.usecase.settings.SettingsAuthenticationUseCase
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.model.SettingsDialogState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel
class SettingsViewModel @Inject constructor(
    dataSettingsUseCase: DataSettingsUseCase,
    private val setValueDataStoreSettingsUseCase: SetValueDataStoreSettingsUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val settingsAuthenticationUseCase: SettingsAuthenticationUseCase
):ViewModel() {

    private val _settingDialogState:MutableStateFlow<SettingsDialogState> = MutableStateFlow(SettingsDialogState())
    val settingDialogState:StateFlow<SettingsDialogState> = _settingDialogState.asStateFlow()

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

    private fun setTheme(themeMode:Int) = viewModelScope.launch{
        setStateDialog(false)
        setValueDataStoreSettingsUseCase.setTheme(themeMode)
    }

    private fun setLanguage(language:String) = viewModelScope.launch{
        setStateDialog(false)
        setLanguageUseCase.setLanguage(language)
        setValueDataStoreSettingsUseCase.setLanguage(language)
    }

    private fun setDaySelected(dayOfWeek: DayOfWeek) = viewModelScope.launch{
        setStateDialog(false)
        setValueDataStoreSettingsUseCase.setDaySelected(dayOfWeek.name)
    }

    fun getCurrentUser():Boolean{
        return settingsAuthenticationUseCase.getCurrentUser()
    }

    fun setStateDialog(state:Boolean){
        _settingDialogState.update { currentState ->
            currentState.copy(showDialog = state)
        }
    }

    fun setDataDialogMode(dataDialog: DataDialog) {
        _settingDialogState.update { currentState ->
            currentState.copy(dataDialog = dataDialog, showDialog = true)
        }
    }

    fun treatResultDialog(dataResult: DataResult){
        when(dataResult){
            is DataResult.LanguageResult -> {setLanguage(dataResult.language)}
            is DataResult.ThemeResult -> {setTheme(dataResult.theme)}
            is DataResult.DayOfWeekResult -> {setDaySelected(dataResult.dayOfWeek)}
        }
    }
}

sealed class SettingsUIState(){
    data object Loading: SettingsUIState()
    data object Error: SettingsUIState()
    data class Success(val data: AppSettings): SettingsUIState()
}