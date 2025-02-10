package aeb.proyecto.habittracker.ui.screens.settings

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.authentication.AuthenticationManager
import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.habittracker.utils.SharedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
    private val sharedState: SharedState,
    private val datastoreInterface: DatastoreInterface
) : ViewModel() {

    fun checkUser(onSaveScreen: () -> Unit, onImportScreen: () -> Unit) {
        setLoading()

        if (authenticationInterface.currentUser() is AuthResponseAuthentication.Success)
            onSaveScreen()
        else
            onImportScreen()

        setNeutral()
    }

    private fun setLoading(){
        sharedState.setLoading()
    }

    private fun setNeutral(){
        sharedState.setNeutral()
    }

    fun setMode(mode:Int){
        viewModelScope.launch {
            datastoreInterface.setModeTheme(mode)
        }
    }
}