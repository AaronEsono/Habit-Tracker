package aeb.proyecto.habittracker.ui.screens.habits

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.model.action.ActionIcon
import aeb.proyecto.habittracker.data.repo.DailyHabitRepo
import aeb.proyecto.habittracker.data.repo.HabitRepo
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
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

    fun plusOneHabit(id:Long, date:LocalDate? = null) = viewModelScope.launch(Dispatchers.IO) {
        var daily = habits.value.find { it.habit.id == id }?.dailyHabits?.find { LocalDate.parse(it.date) == (date ?: LocalDate.now()) }

        if(daily == null){
            val dailyHabit = DailyHabit(idHabit = id, date = date?.toString() ?: LocalDate.now().toString(), timesDone =  1)
            dailyHabitRepo.insertDailyHabit(dailyHabit)
        }else{

            daily = if(daily.timesDone >= habits.value.find { it.habit.id == id }!!.habit.times){
                daily.copy(timesDone = 0)
            }else{
                daily.copy(timesDone = daily.timesDone + 1)
            }

            dailyHabitRepo.updateDailyHabit(daily)
        }
    }

    fun deleteHabit(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        habitRepo.deleteHabit(id)
    }

}