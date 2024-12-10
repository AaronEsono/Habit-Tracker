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

    val habits: LiveData<List<HabitWithDailyHabit>> = habitRepo.getHabits()

    fun plusOneHabit(id:Long) = viewModelScope.launch(Dispatchers.IO) {
        var daily = habits.value?.find { it.habit.id == id }?.dailyHabits?.find { LocalDate.parse(it.date) == LocalDate.now() }

        if(daily == null){
            val dailyHabit = DailyHabit(idHabit = id, date = LocalDate.now().toString(), timesDone =  1)
            dailyHabitRepo.insertDailyHabit(dailyHabit)
        }else{
            daily = daily.copy(timesDone = daily.timesDone + 1)

            if(daily.timesDone > habits.value?.find { it.habit.id == id }!!.habit.times){
                daily = daily.copy(timesDone = 0)
            }

            dailyHabitRepo.updateDailyHabit(daily)
        }
    }
}