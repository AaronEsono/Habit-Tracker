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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepo: HabitRepo,
    private val dailyHabitRepo: DailyHabitRepo
): ViewModel(){
    private val _habits: MutableStateFlow<MutableList<HabitWithDailyHabit>> = MutableStateFlow(
        mutableListOf()
    )
    val habits:StateFlow<MutableList<HabitWithDailyHabit>> = _habits.asStateFlow()

    init {
        getHabits()
    }

    @SuppressLint("NewApi")
    private fun getHabits() = viewModelScope.launch(Dispatchers.IO){
        val habitsRoom = habitRepo.getHabits()

        habitsRoom.forEach {
            if(habitWithNotTodayDate(it.dailyHabits)){
                val dailyHabit = DailyHabit(idHabit = it.habit.id, date = LocalDate.now().toString(), timesDone = 0)
                dailyHabit.id = dailyHabitRepo.insertDailyHabit(dailyHabit)
                it.dailyHabits.add(dailyHabit)
            }
        }

        Log.d("HABITDEPPRUEBA",habitsRoom.toString())
        _habits.value = habitsRoom.toMutableList()
    }

}

@SuppressLint("NewApi")
fun habitWithNotTodayDate(habit: List<DailyHabit>): Boolean{
    return habit.find { LocalDate.parse(it.date) == LocalDate.now() } == null
}