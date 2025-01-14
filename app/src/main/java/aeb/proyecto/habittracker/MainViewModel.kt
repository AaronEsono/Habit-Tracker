package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.di.DataStoreManager
import aeb.proyecto.habittracker.ui.theme.AppTheme
import aeb.proyecto.habittracker.utils.SharedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val sharedState: SharedState
) : ViewModel(){

    val themeMode: StateFlow<Int> = dataStoreManager.themeMode.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000)
    )

    fun getState():SharedState{
        return sharedState
    }

    fun setNeutral(){
        sharedState.setNeutral()
    }

}