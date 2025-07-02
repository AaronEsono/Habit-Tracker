package aeb.proyecto.timer

import aeb.proyecto.domain.usecase.timer.GetTimerDataUseCase
import aeb.proyecto.domain.usecase.timer.TimerData
import aeb.proyecto.domain.usecase.timer.TimerDataStoreUseCase
import aeb.proyecto.stopwatch.helper.StopWatchHelper
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.timer.components.commom.typeTimer.intervalSegmented.model.TypePickState
import aeb.proyecto.timer.model.HabitLinkedState
import aeb.proyecto.timer.model.HourSelectedState
import aeb.proyecto.timer.model.SegmentedButtonOptions
import aeb.proyecto.timer.model.TimerDataUIState
import aeb.proyecto.timer.model.TimerServiceUIState
import aeb.proyecto.timer.model.getSegmentedButtonOptions
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class TimerViewModel @Inject constructor(
    private val serviceHelper: StopWatchHelper,
    getTimerDataUseCase: GetTimerDataUseCase,
    private val timerDataStoreUseCase: TimerDataStoreUseCase,
    private val stopWatchStateManager: StopWatchStateManager
):ViewModel(){

    val timerData: StateFlow<TimerUiState> = getTimerDataUseCase.timerData
        .map<TimerData, TimerUiState> { data ->
            val hourSelected = data.hourSelected?.let {  HourSelectedState.Data(it) } ?: HourSelectedState.NoData
            val typeTimer = getSegmentedButtonOptions(data.typeTimer ?: 1)

            TimerUiState.Success(
                TimerDataUIState(
                    habitLinked = data.habitWithDay?.let { HabitLinkedState.Data(it) } ?: HabitLinkedState.NoData,
                    typeTimer = typeTimer,
                    hourSelected = hourSelected,
                    restHour = data.restHour?.let { HourSelectedState.Data(it) } ?: HourSelectedState.NoData,
                    sets = data.sets,
                    buttonEnabled = typeTimer == SegmentedButtonOptions.StopWatch || (
                            hourSelected is HourSelectedState.Data &&
                                    !(
                                    hourSelected.data.first == 0 &&
                                    hourSelected.data.second == 0 &&
                                    hourSelected.data.third == 0
                                            )
                            )
                )
            )
        }
        .catch { exception ->
            emit(TimerUiState.Error(exception.message ?: "Unknown error"))
        }
        .onStart {
            emit(TimerUiState.Loading)
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimerUiState.Loading
        )

    val timerStopWatchUIState: StateFlow<TimerServiceUIState> = combine(
        stopWatchStateManager.elapsedTime,
        stopWatchStateManager.typeTimer,
        stopWatchStateManager.currentState,
        stopWatchStateManager.timerString,
        stopWatchStateManager.habitLinked
    ) { elapsedTime, typeTimer, currentState, timerString, habitLinked ->

        if(currentState == StopwatchState.Idle) TimerServiceUIState.NoTimer
        else{
            TimerServiceUIState.TimerRunning(
                elapsedTime = elapsedTime,
                typeTimer = typeTimer,
                currentState = currentState,
                hourString = timerString,
                habitLinked = habitLinked
            )
        }

    }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimerServiceUIState.NoTimer
        )

    private val _bottomSheetState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val bottomSheetState: StateFlow<Boolean> = _bottomSheetState.asStateFlow()

    fun startService(){
        val data = (timerData.value as? TimerUiState.Success)?.timerDataUIState
        val time = getLongMillisecondsTime(data?.hourSelected ?: HourSelectedState.NoData)
        val rest = getLongMillisecondsTime(data?.restHour ?: HourSelectedState.NoData)
        val interval = data?.sets ?: 1

        val habitLinked = (timerData.value as? TimerUiState.Success)?.timerDataUIState?.habitLinked

        val habitData:Pair<Long,String> = if(habitLinked is HabitLinkedState.Data){
            habitLinked.data.habit.id to habitLinked.data.day.date.toString()
        }else{
            -1L to ""
        }


        when(data?.typeTimer){
            SegmentedButtonOptions.StopWatch -> {
                serviceHelper.startForegroundServiceOnStopWatch(habitData)
            }
            SegmentedButtonOptions.Timer -> {
                serviceHelper.startForegroundServiceOnTimer(time,habitData)
            }
            SegmentedButtonOptions.Interval -> {
                serviceHelper.startForegroundServiceOnInterval(time,rest,interval,habitData)
            }
            null -> {
                serviceHelper.startForegroundServiceOnStopWatch(habitData)
            }
        }
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
        var currentTotalSeconds = hours * 3600 + minutes * 60 + seconds

        // Ajuste inicial para alinear al múltiplo de delta
        val remainder = currentTotalSeconds % delta
        val updatedTotalSeconds = when {
            remainder != 0 -> {
                if (plusTime) {
                    (currentTotalSeconds + (delta - remainder)).coerceAtMost(99 * 3600 + 59 * 60 + 59)
                } else {
                    (currentTotalSeconds - remainder).coerceAtLeast(0)
                }
            }
            else -> {
                if (plusTime) {
                    (currentTotalSeconds + delta).coerceAtMost(99 * 3600 + 59 * 60 + 59)
                } else {
                    (currentTotalSeconds - delta).coerceAtLeast(0)
                }
            }
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

    private fun getLongMillisecondsTime(hour: HourSelectedState):Long{
        return when(hour){
            is HourSelectedState.Data -> {
                (hour.data.first * 3600 + hour.data.second * 60 + hour.data.third) * 1000L
            }
            HourSelectedState.NoData -> {
                0
            }
        }
    }

    fun finishService(){
        try {
            serviceHelper.finishService()
        }catch (e:Exception){
            Log.e("Error","${e.message}")
        }
    }

    fun cancelService(){
        try {
            serviceHelper.cancelService()
        }catch (e:Exception){
            Log.e("Error","${e.message}")
        }
    }

    fun resumeService(){
        try {
            serviceHelper.resumeService()
        }catch (e:Exception){
            Log.e("Error","${e.message}")
        }
    }

    fun stopService(){
        try {
            serviceHelper.stopService()
        }catch (e:Exception){
            Log.e("Error","${e.message}")
        }
    }

    fun onClickHabitButton(){
        _bottomSheetState.update { true }
    }

    fun onDismissHabitBottomSheet(){
        _bottomSheetState.update { false }
    }

    fun onAcceptBottomSheetPickHabit(id:Long, date:LocalDate) = viewModelScope.launch{
        timerDataStoreUseCase.setHabitLinked(id,date)
    }

    fun cancelHabitLinked() = viewModelScope.launch {
        timerDataStoreUseCase.removeHabitLinked()
    }

}

sealed class TimerUiState{
    data class Success(val timerDataUIState: TimerDataUIState) : TimerUiState()
    data class Error(val error:String) : TimerUiState()
    data object Loading : TimerUiState()
}