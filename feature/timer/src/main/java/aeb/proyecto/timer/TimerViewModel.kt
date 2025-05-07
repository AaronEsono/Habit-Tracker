package aeb.proyecto.timer

import aeb.proyecto.domain.usecase.timer.GetTimerDataUseCase
import aeb.proyecto.domain.usecase.timer.TimerData
import aeb.proyecto.domain.usecase.timer.TimerDataStoreUseCase
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START
import aeb.proyecto.stopwatch.helper.StopWatchHelper
import aeb.proyecto.timer.components.bottomSheet.pickTime.model.TypePickState
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.timer.model.HabitLinkedState
import aeb.proyecto.timer.model.HourSelectedState
import aeb.proyecto.timer.model.TimerDataUIState
import aeb.proyecto.timer.model.getSegmentedButtonOptions
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val serviceHelper: StopWatchHelper,
    getTimerDataUseCase: GetTimerDataUseCase,
    private val timerDataStoreUseCase: TimerDataStoreUseCase
):ViewModel(){

    val timerData: StateFlow<TimerUiState> = getTimerDataUseCase.timerData
        .map<TimerData, TimerUiState> { data ->
            TimerUiState.Success(
                TimerDataUIState(
                    habitLinked = data.habitWithDay?.let { HabitLinkedState.Data(it) } ?: HabitLinkedState.NoData,
                    typeTimer = getSegmentedButtonOptions(data.typeTimer ?: 1),
                    hourSelected = data.hourSelected?.let { HourSelectedState.Data(it) } ?: HourSelectedState.NoData,
                    restHour = data.restHour?.let { HourSelectedState.Data(it) } ?: HourSelectedState.NoData
                )
            )
        }
        .catch { exception ->
            emit(TimerUiState.Error(exception.message ?: "Unknown error"))
        }
        .onStart {
            emit(TimerUiState.Loading)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimerUiState.Loading
        )

    fun startService(){
        serviceHelper.triggerForegroundService(ACTION_SERVICE_START)
    }

    fun onHourChange(hour:String) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveHourWheelTimer(hour.toIntOrNull() ?: 0)
    }

    fun onMinuteChange(hour:String) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveMinuteWheelTimer(hour.toIntOrNull() ?: 0)
    }

    fun onSecondChange(hour:String) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveSecondWheelTimer(hour.toIntOrNull() ?: 0)
    }

    private fun onRestHourChange(hour:String) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveRestHourWheelTimer(hour.toIntOrNull() ?: 0)
    }

    private fun onRestMinuteChange(hour:String) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveRestMinuteWheelTimer(hour.toIntOrNull() ?: 0)
    }

    private fun onRestSecondChange(hour:String) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveRestSecondWheelTimer(hour.toIntOrNull() ?: 0)
    }

    fun setIntervalHour(time:Triple<String,String,String>, type:Int) = viewModelScope.launch(Dispatchers.IO){
        when(type){
            TypePickState.WORK_TIME.value -> {
                onHourChange(time.first)
                onMinuteChange(time.second)
                onSecondChange(time.third)
            }
            TypePickState.REST_TIME.value -> {
                onRestHourChange(time.first)
                onRestMinuteChange(time.second)
                onRestSecondChange(time.third)
            }
        }
    }

    fun onTypeButtonChange(value:Int) = viewModelScope.launch(Dispatchers.IO){
        timerDataStoreUseCase.saveTypeButtonTimer(value)
    }
}

sealed class TimerUiState{
    data class Success(val timerDataUIState: TimerDataUIState) : TimerUiState()
    data class Error(val error:String) : TimerUiState()
    data object Loading : TimerUiState()
}