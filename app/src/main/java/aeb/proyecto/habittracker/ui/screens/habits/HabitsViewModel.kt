package aeb.proyecto.habittracker.ui.screens.habits

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.model.state.HabitsScreenState
import aeb.proyecto.habittracker.data.repo.DailyHabitRepo
import aeb.proyecto.habittracker.data.repo.HabitRepo
import aeb.proyecto.habittracker.data.repo.HabitWithNotificacionRepo
import aeb.proyecto.habittracker.utils.Constans
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepo: HabitRepo,
    private val dailyHabitRepo: DailyHabitRepo,
    private val habitWithNotification: HabitWithNotificacionRepo
) : ViewModel() {

    val habits: StateFlow<List<HabitWithDailyHabit>> = habitRepo.getHabits().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Mantener activo mientras hay suscriptores
        initialValue = emptyList() // Valor inicial vacío
    )

    private val _habitSelected: MutableStateFlow<HabitWithDailyHabit?> = MutableStateFlow(null)
    val habitSelected: StateFlow<HabitWithDailyHabit?> = _habitSelected.asStateFlow()

    private val _uiState: MutableStateFlow<HabitsScreenState> =
        MutableStateFlow(HabitsScreenState())
    val uiState: StateFlow<HabitsScreenState> = _uiState.asStateFlow()

    fun choseStep(id: Long, date: LocalDate? = null) = viewModelScope.launch {
        setHabit(id)

        val unit = getUnit(_habitSelected.value?.habit)

        if (unit.action == Constans.PICK) {
            plusOneHabit(id, date)
        } else {
            val time = getRestSteps(id, date)
            if (time > 0) {
                _uiState.update { currentState ->
                    currentState.copy(showSteps = true, date = date)
                }
            }else{
                _uiState.update { currentState ->
                    currentState.copy(showGeneralDx = true, textAttention = R.string.general_dx_subtitle_restart, date = date)
                }
            }
        }

    }

    fun plusOneHabit(id: Long, date: LocalDate? = null, times: Int = 1, restart:Boolean = false) = viewModelScope.launch(Dispatchers.IO) {
            var daily = _habitSelected.value?.dailyHabits?.find {
                LocalDate.parse(it.date) == (date ?: LocalDate.now())
            }
            val timesHabit = _habitSelected.value?.habit?.times ?: 0

            if (daily == null) {
                val dailyHabit = DailyHabit(
                    idHabit = id,
                    date = date?.toString() ?: LocalDate.now().toString(),
                    timesDone = times
                )

                if (dailyHabit.timesDone > (timesHabit)){
                    dailyHabit.timesDone = timesHabit
                    dailyHabit.hourFinishDate = LocalDateTime.now().toString()
                }

                dailyHabitRepo.insertDailyHabit(dailyHabit)
            } else {
                daily = daily.copy(timesDone = daily.timesDone + times)
                if ((daily.timesDone > timesHabit && times == 1) || restart) {
                    daily = daily.copy(timesDone = 0)
                }

                if(daily.timesDone == timesHabit)
                    daily.hourFinishDate = LocalDateTime.now().toString()
                else
                    daily.hourFinishDate = null

                dailyHabitRepo.updateDailyHabit(daily)
            }
        }

    fun generalDxLogic(cancelNotificacions:(List<Long>) -> Unit){
        if(uiState.value.textAttention == R.string.general_dx_subtitle_delete){
            deleteUnit(getId())
            val id = _habitSelected.value?.habit?.id ?: 1

            viewModelScope.launch (Dispatchers.IO){
                cancelNotificacions(habitWithNotification.getNotificationById(id).map { it.id })
            }

        }else{
            plusOneHabit(getId(),uiState.value.date,0,true)
            closeGeneralDx()
        }
    }

    fun deleteHabit() = viewModelScope.launch(Dispatchers.IO) {
        habitRepo.deleteHabit(habitSelected.value?.habit?.id ?: 1)
    }

    fun getRestSteps(id:Long,date: LocalDate? = null):Int{
        val daily = _habitSelected.value?.dailyHabits?.find { LocalDate.parse(it.date) == (date ?: LocalDate.now()) }
        return _habitSelected.value?.habit?.times!! - (daily?.timesDone ?: 0)
    }

    fun getHabit(): HabitWithDailyHabit {
        return habits.value.find { it.habit.id == habitSelected.value?.habit?.id }
            ?: HabitWithDailyHabit()
    }

    fun getId(): Long {
        return habitSelected.value?.habit?.id ?: 1
    }

    fun getColor(): Color {
        return Color(habitSelected.value?.habit?.color ?: 111111)
    }

    fun deleteUnit(id: Long) = viewModelScope.launch {
        setHabit(id)
        deleteHabit()

        _uiState.update { currentState ->
            currentState.copy(showGeneralDx = false, showDialog = false)
        }
    }

    fun showDialog(id: Long) = viewModelScope.launch {
        setHabit(id)

        _uiState.update { currentState ->
            currentState.copy(showDialog = true)
        }
    }

    fun showGeneralDx() {
        _uiState.update { currentState ->
            currentState.copy(showGeneralDx = true, textAttention = R.string.general_dx_subtitle_delete)
        }
    }

    fun closeDialog() {
        _uiState.update { currentState ->
            currentState.copy(showDialog = false)
        }
    }

    fun closeGeneralDx() {
        _uiState.update { currentState ->
            currentState.copy(showGeneralDx = false)
        }
    }

    fun closeSteps() {
        _uiState.update { currentState ->
            currentState.copy(showSteps = false)
        }
    }

    fun openCalendar() {
        _uiState.update { currentState ->
            currentState.copy(showCalendar = true)
        }
    }

    fun closeCalendar() {
        _uiState.update { currentState ->
            currentState.copy(showCalendar = false)
        }
    }

    suspend fun setHabit(id: Long) {
        _habitSelected.emit(habits.value.find { it.habit.id == id })
    }

    fun getUnit(habit: Habit?): Constans.Units {
        return Constans.Units.entries.find { it.id == habit?.unit } ?: Constans.Units.TIMES
    }

    fun getTitle():Int{
        return (Constans.Units.entries.find { it.id == _habitSelected.value?.habit?.unit } ?: Constans.Units.TIMES).pluralTitle
    }

}