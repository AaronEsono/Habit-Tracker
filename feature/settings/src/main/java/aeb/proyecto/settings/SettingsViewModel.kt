package aeb.proyecto.settings

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.datastore.DatastoreInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
    private val datastoreInterface: DatastoreInterface
):ViewModel() {

    fun setTheme(themeMode:Int) = viewModelScope.launch{
        datastoreInterface.setModeTheme(themeMode)
    }

    fun getCurrentUser():Boolean{
        return authenticationInterface.currentUser() is AuthResponseAuthentication.Success
    }

}

sealed interface SettingsUIState{
    data object Loading
    data object Success
}