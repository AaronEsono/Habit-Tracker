package aeb.proyecto.habittracker.ui.screens.habits

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.model.action.ActionIcon
import aeb.proyecto.habittracker.data.model.state.HabitsScreenState
import aeb.proyecto.habittracker.data.repo.DailyHabitRepo
import aeb.proyecto.habittracker.data.repo.HabitRepo
import aeb.proyecto.habittracker.utils.Constans
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepo: HabitRepo,
    private val dailyHabitRepo: DailyHabitRepo
): ViewModel(){

    val habits: StateFlow<List<HabitWithDailyHabit>> = habitRepo.getHabits().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Mantener activo mientras hay suscriptores
        initialValue = emptyList() // Valor inicial vacío
    )

    private val _habitSelected: MutableStateFlow<HabitWithDailyHabit?> = MutableStateFlow(null)
    val habitSelected: StateFlow<HabitWithDailyHabit?> = _habitSelected.asStateFlow()

    private val _uiState: MutableStateFlow<HabitsScreenState> = MutableStateFlow(HabitsScreenState())
    val uiState: StateFlow<HabitsScreenState> = _uiState.asStateFlow()

    fun choseStep(id:Long,date:LocalDate? = null) = viewModelScope.launch{
        setHabit(id)

        val  unit = getUnit(_habitSelected.value?.habit)

        if(unit.action == Constans.PICK){
            plusOneHabit(id,date)
        }else{
            //Futuro
        }

    }

    private fun plusOneHabit(id:Long, date:LocalDate? = null) = viewModelScope.launch(Dispatchers.IO) {
        var daily = _habitSelected.value?.dailyHabits?.find { LocalDate.parse(it.date) == (date ?: LocalDate.now()) }

        if(daily == null){
            val dailyHabit = DailyHabit(idHabit = id, date = date?.toString() ?: LocalDate.now().toString(), timesDone =  1)
            dailyHabitRepo.insertDailyHabit(dailyHabit)
        }else{

            daily = if (daily.timesDone >= (_habitSelected.value?.habit?.times ?: 0)) {
                daily.copy(timesDone = 0)
            } else {
                daily.copy(timesDone = daily.timesDone + 1)
            }

            dailyHabitRepo.updateDailyHabit(daily)
        }
    }

    fun deleteHabit() = viewModelScope.launch(Dispatchers.IO) {
        habitRepo.deleteHabit(habitSelected.value?.habit?.id ?: 1)
    }

    fun getHabit():HabitWithDailyHabit {
        return habits.value.find { it.habit.id == habitSelected.value?.habit?.id } ?: HabitWithDailyHabit()
    }

    fun getId():Long{
        return habitSelected.value?.habit?.id ?: 1
    }

    fun getColor(): Color {
        return Color(habitSelected.value?.habit?.color ?: 111111)
    }

    fun deleteUnit(id:Long) = viewModelScope.launch{
        setHabit(id)
        deleteHabit()

        _uiState.update {
            currentState -> currentState.copy(showGeneralDx = false, showDialog = false)
        }
    }

    fun showDialog(id:Long) = viewModelScope.launch{
        setHabit(id)

        _uiState.update { currentState ->
            currentState.copy(showDialog = true)
        }
    }

    fun showGeneralDx(){
        _uiState.update { currentState ->
            currentState.copy(showGeneralDx = true)
        }
    }

    fun closeDialog() {
        _uiState.update { currentState ->
            currentState.copy(showDialog = false)
        }
    }

    fun closeGeneralDx(){
        _uiState.update { currentState ->
            currentState.copy(showGeneralDx = false)
        }
    }

    fun openCalendar(){
        _uiState.update { currentState ->
            currentState.copy(showCalendar = true)
        }
    }

    fun closeCalendar(){
        _uiState.update { currentState ->
            currentState.copy(showCalendar = false)
        }
    }

    suspend fun setHabit(id:Long) {
        _habitSelected.emit(habits.value.find { it.habit.id == id })
    }

    fun getUnit(habit:Habit?):Constans.Units{
        return Constans.Units.entries.find { it.id == habit?.unit } ?: Constans.Units.TIMES
    }
}