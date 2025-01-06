package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.di.DataStoreManager
import aeb.proyecto.habittracker.ui.theme.AppTheme
import aeb.proyecto.habittracker.utils.SharedState
import aeb.proyecto.habittracker.utils.setMode
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val sharedState: SharedState
) : ViewModel(){

    private val _themeMode: MutableStateFlow<Int> = MutableStateFlow(AppTheme.DARK.theme)
    val themeMode: StateFlow<Int> = _themeMode.asStateFlow()

    init {
        setModeTheme()
    }

    private fun setModeTheme() = viewModelScope.launch{
        _themeMode.value = dataStoreManager.themeMode.first() ?: 0
    }

    fun getState():SharedState{
        return sharedState
    }

    fun setNeutral(){
        sharedState.setNeutral()
    }

    fun saveMode(mode:Int) = viewModelScope.launch{
        _themeMode.value = mode
        dataStoreManager.setModeTheme(mode)
    }

}