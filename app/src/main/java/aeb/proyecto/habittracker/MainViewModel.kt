package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.di.DataStoreManager
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

    init {
        setModeTheme()
    }

    private fun setModeTheme() = viewModelScope.launch{
        val mode = dataStoreManager.themeMode.first() ?: 0
        setMode(mode)
    }

    fun getState():SharedState{
        return sharedState
    }

    fun setNeutral(){
        sharedState.setNeutral()
    }

}