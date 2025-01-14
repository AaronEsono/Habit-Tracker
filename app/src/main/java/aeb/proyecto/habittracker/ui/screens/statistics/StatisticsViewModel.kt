package aeb.proyecto.habittracker.ui.screens.statistics

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.model.state.BestStreak
import aeb.proyecto.habittracker.data.model.state.DaysCompleted
import aeb.proyecto.habittracker.data.model.state.StatisticsState
import aeb.proyecto.habittracker.data.repo.DailyHabitRepo
import aeb.proyecto.habittracker.data.repo.HabitRepo
import aeb.proyecto.habittracker.utils.SharedState
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val habitRepo: HabitRepo,
    private val dailyHabitRepo: DailyHabitRepo,
    private val sharedState: SharedState
) : ViewModel() {

    private val _habits: MutableStateFlow<List<Habit>> = MutableStateFlow(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    private val _selectedDailyHabit: MutableStateFlow<Habit?> = MutableStateFlow(null)
    val selectedDailyHabit: StateFlow<Habit?> = _selectedDailyHabit.asStateFlow()

    private val _dailyHabits: MutableStateFlow<List<DailyHabit>> = MutableStateFlow(emptyList())
    val dailyHabits: StateFlow<List<DailyHabit>> = _dailyHabits.asStateFlow()

    private val _statisticsState: MutableStateFlow<StatisticsState?> = MutableStateFlow(null)
    val statisticsState: StateFlow<StatisticsState?> = _statisticsState.asStateFlow()

    private val _loaded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loaded: StateFlow<Boolean> = _loaded.asStateFlow()

    fun getHabits() = viewModelScope.launch(Dispatchers.IO) {
        setLoading()

        _habits.value = habitRepo.getAllHabits()
        _loaded.value = true

        if(_habits.value.isEmpty()){
            setNeutral()
        }
    }

    fun getDailyHabits() = viewModelScope.launch(Dispatchers.IO) {
        setLoading()
        _statisticsState.value = null

        val habit = habits.value[0]

        _selectedDailyHabit.value = habit
        _dailyHabits.value = dailyHabitRepo.getDailyHabits(habit.id)

        _statisticsState.update {
            StatisticsState(
                timesCompleted = timesCompleted(),
                actualStreak = getStreak(),
                bestStreak = getBestStreak()
            )
        }
        setNeutral()
    }

    fun setSelectedHabit(habit: Habit){
        _selectedDailyHabit.value = habit
        getDailyHabits()
    }

    private fun findHabit(): Habit? {
        return habits.value.find { it == _selectedDailyHabit.value }
    }

    private fun timesCompleted(): Int {
        findHabit()?.let {
            return _dailyHabits.value.count { dailyHabit -> dailyHabit.timesDone == it.times }
        }
        return 0
    }

    private fun findMaxTimes(): Int {
        return findHabit()?.times ?: 0
    }

    private fun getStreak(): Int {
        val sortedList = _dailyHabits.value.sortedByDescending { it.date }
        val maxTimes = findMaxTimes()
        var streak = 0
        var today = LocalDate.now()

        for (dailyHabit in sortedList) {
            if (dailyHabit.timesDone == maxTimes && LocalDate.parse(dailyHabit.date) == today) {
                streak++
                today = today.minusDays(1)
            } else {
                break
            }
        }
        return streak
    }

    private fun getBestStreak(): Int {
        val sortedList = _dailyHabits.value.sortedByDescending { it.date }
        val maxTimes = findMaxTimes()
        var today = LocalDate.now()

        var streak:Int = 0
        var times = 0
        var startDateTemp = LocalDate.now()


        for (dailyHabit in sortedList) {
            if (dailyHabit.timesDone == maxTimes && times == 0) {
                //Empezamos nueva racha
                times++
                today = LocalDate.parse(dailyHabit.date)
                startDateTemp = today
                today = today.minusDays(1)

            } else if (dailyHabit.timesDone == maxTimes && LocalDate.parse(dailyHabit.date) == today) {
                //Contamos nueva racha
                times++
                today = today.minusDays(1)

            } else {
                //Comparamos con la anterior
                if (times > streak) {
                    streak = times
                }
                times = 0
                today = today.minusDays(1)
            }
        }

        // Comparar la última racha
        if (times > streak) {
            streak = times
        }

        return streak
    }

    private fun setLoading(){
        sharedState.setLoading()
    }

    private fun setNeutral(){
        sharedState.setNeutral()
    }
}