package aeb.proyecto.habittracker

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.habittracker.utils.SharedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedState: SharedState,
    private val datastoreInterface: DatastoreInterface
) : ViewModel(){

    val themeMode: StateFlow<Int> = datastoreInterface.themeMode.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun getState():SharedState{
        return sharedState
    }

    fun setNeutral(){
        sharedState.setNeutral()
    }

}