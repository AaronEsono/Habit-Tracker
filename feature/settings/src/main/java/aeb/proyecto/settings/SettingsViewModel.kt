package aeb.proyecto.settings

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.language.LanguageInterface
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.model.GeneralOptionsData
import aeb.proyecto.settings.model.SettingsDialogState
import aeb.proyecto.settings.model.TypeDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
    private val datastoreInterface: DatastoreInterface,
    private val languageInterface: LanguageInterface
):ViewModel() {

    private val _settingDialogState:MutableStateFlow<SettingsDialogState> = MutableStateFlow(SettingsDialogState())
    val settingDialogState:StateFlow<SettingsDialogState> = _settingDialogState.asStateFlow()

    private val _generalOptionsData:MutableStateFlow<GeneralOptionsData> = MutableStateFlow(GeneralOptionsData())
    val generalOptionsData:StateFlow<GeneralOptionsData> = _generalOptionsData.asStateFlow()

    private val _dataSearched:MutableStateFlow<Boolean> = MutableStateFlow(false)

    val themeSelected = datastoreInterface.themeMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val languageSelected = datastoreInterface.language.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "en"
    )

    private fun setTheme(themeMode:Int) = viewModelScope.launch{
        setStateDialog(false)
        datastoreInterface.setModeTheme(themeMode)
    }

    private fun setLanguage(language:String) = viewModelScope.launch{
        setStateDialog(false)
        languageInterface.setLanguage(language)
        datastoreInterface.setLanguage(language)
    }

    fun getCurrentUser():Boolean{
        return authenticationInterface.currentUser() is AuthResponseAuthentication.Success
    }

    fun setStateDialog(state:Boolean){
        _settingDialogState.update { currentState ->
            currentState.copy(showDialog = state)
        }
    }

    fun setDataDialogMode(typeDialog: TypeDialog) {
        _settingDialogState.update { currentState ->
            currentState.copy(dataDialog = typeDialog, showDialog = true)
        }
    }

    fun treatResultDialog(dataResult: DataResult){
        when(dataResult){
            is DataResult.LanguageResult -> {setLanguage(dataResult.language)}
            is DataResult.ThemeResult -> {setTheme(dataResult.theme)}
        }
    }

    fun getGeneralOptionsData() = viewModelScope.launch{
        if(!_dataSearched.value){
            _generalOptionsData.update { currentState ->
                currentState.copy(
                    firstDayOfWeek = DayOfWeek.valueOf(datastoreInterface.getDayStartWeek() ?: "MONDAY")
                )
            }

            _dataSearched.value = true
        }
    }

}