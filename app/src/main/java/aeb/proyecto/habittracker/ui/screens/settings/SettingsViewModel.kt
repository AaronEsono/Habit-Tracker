package aeb.proyecto.habittracker.ui.screens.settings

import aeb.proyecto.habittracker.di.DataStoreManager
import aeb.proyecto.habittracker.utils.SharedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    fun saveMode(mode:Int) = viewModelScope.launch{
        dataStoreManager.setModeTheme(mode)
    }

}