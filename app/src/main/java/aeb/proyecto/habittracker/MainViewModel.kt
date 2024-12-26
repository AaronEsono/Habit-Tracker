package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.data.model.action.ActionIcon
import aeb.proyecto.habittracker.data.repo.NotificationRepo
import aeb.proyecto.habittracker.di.DataStoreManager
import aeb.proyecto.habittracker.utils.setMode
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
) : ViewModel(){

    init {
        setModeTheme()
    }

    private fun setModeTheme() = viewModelScope.launch{
        val mode = dataStoreManager.themeMode.first() ?: 0
        setMode(mode)
    }
}