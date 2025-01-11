package aeb.proyecto.habittracker.ui.screens.statistics

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.model.state.BestStreak
import aeb.proyecto.habittracker.data.model.state.DaysCompleted
import aeb.proyecto.habittracker.data.model.state.StatisticsState
import aeb.proyecto.habittracker.data.repo.DailyHabitRepo
import aeb.proyecto.habittracker.data.repo.HabitRepo
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
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val habitRepo: HabitRepo,
    private val dailyHabitRepo: DailyHabitRepo
) : ViewModel() {

    val habits: StateFlow<List<Habit>> = habitRepo.getAllHabits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Mantener activo mientras hay suscriptores
            initialValue = emptyList() // Valor inicial vacío
        )

    private val _dailyHabits: MutableStateFlow<List<DailyHabit>> = MutableStateFlow(listOf())
    val dailyHabits: StateFlow<List<DailyHabit>> = _dailyHabits.asStateFlow()

    private val _statisticsState: MutableStateFlow<StatisticsState> =
        MutableStateFlow(StatisticsState())
    val statisticsState: StateFlow<StatisticsState> = _statisticsState.asStateFlow()

    private val _showData: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showData: StateFlow<Boolean> = _showData.asStateFlow()

    fun getDailyHabits(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        _showData.value = false

        _dailyHabits.value = dailyHabitRepo.getDailyHabits(id)

        _statisticsState.update { currentState ->
            currentState.copy(
                timesCompleted = timesCompleted(),
                streak = getStreak(),
                bestStreak = getBestStreak(),
                daysCompleted = getDaysCompleted()
            )
        }

        _showData.value = true
    }

    fun timesCompleted(): Int {
        findHabit()?.let {
            return _dailyHabits.value.count { dailyHabit -> dailyHabit.timesDone == it.times }
        }
        return 0
    }

    fun findHabit(): Habit? {
        return habits.value.find { _dailyHabits.value.any { dailyHabit -> dailyHabit.idHabit == it.id } }
    }

    fun findMaxTimes(): Int {
        return habits.value.find { _dailyHabits.value.any { dailyHabit -> dailyHabit.idHabit == it.id } }?.times
            ?: 0
    }

    fun getStreak(): Int {
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

    fun getBestStreak(): BestStreak {
        val sortedList = _dailyHabits.value.sortedByDescending { it.date }
        val maxTimes = findMaxTimes()
        var today = LocalDate.now()

        var streak = BestStreak()
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
                if (times > streak.times) {
                    streak = BestStreak(
                        startDate = today.plusDays(1),
                        endDate = startDateTemp,
                        times = times
                    )
                }
                times = 0
                today = today.minusDays(1)
            }
        }

        // Comparar la última racha
        if (times > streak.times) {
            streak = BestStreak(
                startDate = today.plusDays(1),
                endDate = LocalDate.parse(sortedList.first().date),
                times = times
            )
        }

        return streak
    }

    private fun getDaysCompleted(): DaysCompleted {
        val sortedList = _dailyHabits.value.filter { it.timesDone != 0 }.sortedByDescending { it.date }

        if (sortedList.isNotEmpty()) {
            val totalDays = ChronoUnit.DAYS.between(LocalDate.parse(sortedList.last().date), LocalDate.now()) + 1
            val daysTried = sortedList.size

            return DaysCompleted(
                completed = timesCompleted(),
                uncompleted = daysTried - timesCompleted(),
                noDone = (totalDays - daysTried - (daysTried - timesCompleted())).toInt()
            )
        } else {
            return DaysCompleted()
        }
    }

    //Calendario dias completados X
    //Numero veces completado X
    //Racha actual X
    //Mejor racha y fecha X
    //Media de hora en la que el habito se hace
    //Chart circular con los completados, no completados y pendientes
}