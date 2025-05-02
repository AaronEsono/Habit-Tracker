package aeb.proyecto.timer

import aeb.proyecto.domain.usecase.timer.GetHabitWithDayUseCase
import aeb.proyecto.room.entities.relations.HabitWithDay
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val getHabitWithDayUseCase: GetHabitWithDayUseCase
):ViewModel(){

    private val _habitData: MutableStateFlow<Pair<Long, LocalDate>?> = MutableStateFlow(null)
    private val _searched: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _timerSelected: MutableStateFlow<TimerSelectedState> = MutableStateFlow(TimerSelectedState.NoData)
    val timerSelected: StateFlow<TimerSelectedState> = _timerSelected.asStateFlow()

    private val _timeLeft: MutableStateFlow<String> = MutableStateFlow("")
    val timeLeft: StateFlow<String> = _timeLeft.asStateFlow()

    val timerUIState: StateFlow<TimerUiState> = _habitData.map { pair ->
            if (pair != null) {
                val data = withContext(Dispatchers.IO){
                    getHabitWithDayUseCase.getHabitWithDay(pair.first, pair.second)
                }

                if(timerSelected.value is TimerSelectedState.NoData){
                    _timerSelected.value = TimerSelectedState.Data(calculateTimeDifference(data))
                }
                _timeLeft.value = calculateRemainingTimeString(data)
                TimerUiState.Success(data)
            } else {
                TimerUiState.Loading
            }
        }
        .catch { exception ->
            emit(TimerUiState.Error(exception.toString()))
        }
        .onStart {
            emit(TimerUiState.Loading)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimerUiState.Loading
        )

    fun getData(data: Pair<Long, LocalDate>) {
        if(!_searched.value){
            _habitData.value = data
            _searched.value = true
        }
    }

    fun onHourChange(hour: String) {
        if (timerSelected.value is TimerSelectedState.Data) {
            val (_, currentMinute, currentSecond) = (timerSelected.value as TimerSelectedState.Data).data
            _timerSelected.update {
                TimerSelectedState.Data(Triple(hour.toInt(), currentMinute, currentSecond))
            }
        }
    }

    fun onMinuteChange(minute: String) {
        if (timerSelected.value is TimerSelectedState.Data) {
            val (currentHour, _, currentSecond) = (timerSelected.value as TimerSelectedState.Data).data
            _timerSelected.update {
                TimerSelectedState.Data(Triple(currentHour, minute.toInt(), currentSecond))
            }
        }
    }

    fun onSecondChange(second: String) {
        if (timerSelected.value is TimerSelectedState.Data) {
            val (currentHour, currentMinute, _) = (timerSelected.value as TimerSelectedState.Data).data
            _timerSelected.update {
                TimerSelectedState.Data(Triple(currentHour,currentMinute, second.toInt()))
            }
        }
    }
}


sealed class TimerUiState {
    data class Success(val habitWithDay: HabitWithDay) : TimerUiState()
    data class Error(val error:String) : TimerUiState()
    data object Loading : TimerUiState()
}

sealed class TimerSelectedState {
    data object NoData : TimerSelectedState()
    data class Data(val data: Triple<Int,Int,Int>) : TimerSelectedState()
}

fun calculateTimeDifference(habitWithDay: HabitWithDay): Triple<Int, Int, Int> {
    // Calculamos los segundos restantes
    val remainingSeconds = habitWithDay.habit.goal.subtract(habitWithDay.day.goalDone).toLong()

    // Calculamos las horas, minutos y segundos
    val hours = (remainingSeconds / 3600).toInt()
    val minutes = ((remainingSeconds % 3600) / 60).toInt()
    val seconds = (remainingSeconds % 60).toInt()

    // Devolvemos el resultado como un Triple
    return Triple(hours, minutes, seconds)
}

fun calculateRemainingTimeString(habitWithDay: HabitWithDay): String {
    val remainingSeconds = habitWithDay.habit.goal.subtract(habitWithDay.day.goalDone).toLong()

    val hours = (remainingSeconds / 3600).toInt()
    val minutes = ((remainingSeconds % 3600) / 60).toInt()
    val seconds = (remainingSeconds % 60).toInt()

    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%d:%02d".format(minutes, seconds)
    }
}