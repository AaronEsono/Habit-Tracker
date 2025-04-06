package aeb.proyecto.habittracker

import aeb.proyecto.datastore.DatastoreInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val datastoreInterface: DatastoreInterface
) : ViewModel(){

    private val _dataSet = MutableStateFlow(false)

    val themeMode: StateFlow<Int> = datastoreInterface.themeMode.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun setData() = viewModelScope.launch{
        if(!_dataSet.value){
            //Seteamos el primer dia se la semana si no está
            val day = datastoreInterface.getDayStartWeek()
            day ?: datastoreInterface.saveFirstDayOfWeek()

            _dataSet.value = true
        }
    }

}