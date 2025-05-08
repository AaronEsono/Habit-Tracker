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
import kotlinx.coroutines.delay
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
                    restHour = data.restHour?.let { HourSelectedState.Data(it) } ?: HourSelectedState.NoData,
                    sets = data.sets
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

    private fun setHour(value: Triple<Int,Int,Int>) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveHourWheelTimer(value.first)
        timerDataStoreUseCase.saveMinuteWheelTimer(value.second)
        timerDataStoreUseCase.saveSecondWheelTimer(value.third)
    }

    private fun setHourRest(value: Triple<Int,Int,Int>) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveRestHourWheelTimer(value.first)
        timerDataStoreUseCase.saveRestMinuteWheelTimer(value.second)
        timerDataStoreUseCase.saveRestSecondWheelTimer(value.third)
    }

    fun setIntervalHour(time:Triple<String,String,String>, type:Int) = viewModelScope.launch(Dispatchers.IO){
        when(type){
            TypePickState.WORK_TIME.value -> {
                setHour(
                    Triple(
                        time.first.toIntOrNull() ?: 0,
                        time.second.toIntOrNull() ?: 0,
                        time.third.toIntOrNull() ?: 0
                    )
                )
            }
            TypePickState.REST_TIME.value -> {
                setHourRest(
                    Triple(
                        time.first.toIntOrNull() ?: 0,
                        time.second.toIntOrNull() ?: 0,
                        time.third.toIntOrNull() ?: 0
                    )
                )
            }
        }
    }

    fun onTypeButtonChange(value:Int) = viewModelScope.launch(Dispatchers.IO){
        timerDataStoreUseCase.saveTypeButtonTimer(value)
    }

    fun onSetChange(value:Int) = viewModelScope.launch(Dispatchers.IO){
        if(value in 1..99){
            timerDataStoreUseCase.setTimer(value)
        }
    }

    fun addHourTimer(plusTime:Boolean) = viewModelScope.launch (Dispatchers.IO){
        setHour(editTime(getTime(),5,plusTime))
    }

    fun addRestTimer(plusTime:Boolean) = viewModelScope.launch (Dispatchers.IO){
        setHourRest(editTime(getRestTime(),5,plusTime))
    }

    private fun editTime(time:Triple<Int,Int,Int>, delta:Int, plusTime:Boolean):Triple<Int,Int,Int>{
        val (hours, minutes, seconds) = time

        val currentTotalSeconds = hours * 3600 + minutes * 60 + seconds

        val updatedTotalSeconds = when (plusTime) {
            true -> (currentTotalSeconds + delta).coerceAtMost(99 * 3600 + 59 * 60 + 59) // máx 99:59:59
            false -> (currentTotalSeconds - delta).coerceAtLeast(0) // mínimo 00:00:00
        }

        // Convertir total de segundos a h:m:s
        val newHours = updatedTotalSeconds / 3600
        val newMinutes = (updatedTotalSeconds % 3600) / 60
        val newSeconds = updatedTotalSeconds % 60

        return Triple(newHours, newMinutes, newSeconds)
    }

    private fun getTime():Triple<Int,Int,Int>{
        val time = (timerData.value as? TimerUiState.Success)?.timerDataUIState?.hourSelected ?: HourSelectedState.NoData
        var respond:Triple<Int,Int,Int> = Triple(0,0,0)

        if(time is HourSelectedState.Data){
            respond = Triple(time.data.first,time.data.second,time.data.third)
        }

        return respond
    }

    private fun getRestTime():Triple<Int,Int,Int>{
        val time = (timerData.value as? TimerUiState.Success)?.timerDataUIState?.restHour ?: HourSelectedState.NoData
        var respond:Triple<Int,Int,Int> = Triple(0,0,0)

        if(time is HourSelectedState.Data){
            respond = Triple(time.data.first,time.data.second,time.data.third)
        }

        return respond
    }

}

sealed class TimerUiState{
    data class Success(val timerDataUIState: TimerDataUIState) : TimerUiState()
    data class Error(val error:String) : TimerUiState()
    data object Loading : TimerUiState()
}