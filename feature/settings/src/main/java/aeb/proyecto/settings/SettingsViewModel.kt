package aeb.proyecto.settings

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.settings.model.SettingsDialogState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
    private val datastoreInterface: DatastoreInterface
):ViewModel() {

    private val _settingDialogState:MutableStateFlow<SettingsDialogState> = MutableStateFlow(SettingsDialogState())
    val settingDialogState:StateFlow<SettingsDialogState> = _settingDialogState.asStateFlow()

    fun setTheme(themeMode:Int) = viewModelScope.launch{
        datastoreInterface.setModeTheme(themeMode)
    }

    fun getCurrentUser():Boolean{
        return authenticationInterface.currentUser() is AuthResponseAuthentication.Success
    }

    fun setStateDialog(state:Boolean){
        _settingDialogState.update { currentState ->
            currentState.copy(showDialog = state)
        }
    }
}