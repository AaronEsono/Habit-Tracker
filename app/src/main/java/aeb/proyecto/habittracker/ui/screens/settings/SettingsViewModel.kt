package aeb.proyecto.habittracker.ui.screens.settings

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.habittracker.utils.AuthResponse
import aeb.proyecto.habittracker.utils.AuthenticationManager
import aeb.proyecto.habittracker.utils.SharedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager,
    private val sharedState: SharedState,
    private val datastoreInterface: DatastoreInterface
) : ViewModel() {

    fun checkUser(onSaveScreen: () -> Unit, onImportScreen: () -> Unit) {
        setLoading()
        authenticationManager.currentUser().onEach { result ->
            setNeutral()
            if (result is AuthResponse.Success) {
                onSaveScreen()
            } else {
                onImportScreen()
            }
        }.launchIn(viewModelScope)
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