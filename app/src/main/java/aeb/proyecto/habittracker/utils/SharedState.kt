package aeb.proyecto.habittracker.utils


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SharedState @Inject constructor() {
    private val _appState = MutableStateFlow<AppState>(AppState.Neutral)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    fun setLoading() {
        _appState.value = AppState.Loading
    }

    fun setError( messageInt: Int) {
        _appState.value = AppState.Error(messageInt)
    }

    fun setNeutral() {
        _appState.value = AppState.Neutral
    }

}

sealed class AppState{
    object Loading : AppState()
    data class Error(val messageInt: Int) : AppState()
    object Neutral:AppState()
}