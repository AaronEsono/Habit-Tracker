package aeb.proyecto.habittracker.ui.components.calendar

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.model.calendar.CalendarDataSource
import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val dataSource: CalendarDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState(YearMonth.now(), emptyList()))
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _dailyHabits = MutableStateFlow(emptyList<DailyHabit>())
    val dailyHabits: StateFlow<List<DailyHabit>> = _dailyHabits.asStateFlow()

    private val _habit: MutableStateFlow<Habit> = MutableStateFlow(Habit())
    val habitWithDailyHabit: StateFlow<Habit> = _habit.asStateFlow()

    private val _showCalendar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showCalendar: StateFlow<Boolean> = _showCalendar.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentState ->
                currentState.copy(
                    dates = dataSource.getDates(currentState.yearMonth)
                )
            }
        }
    }

    fun toNextMonth(nextMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = nextMonth,
                    dates = dataSource.getDates(nextMonth)
                )
            }
        }
    }

    fun toPreviousMonth(prevMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = prevMonth,
                    dates = dataSource.getDates(prevMonth)
                )
            }
        }
    }

    fun createDate(date: CalendarUiState.Date): LocalDate? {
        var dateR:LocalDate? = null

        if(date.dayOfMonth.isNotEmpty()){
            dateR = LocalDate.of(
                _uiState.value.yearMonth.year,
                _uiState.value.yearMonth.month,
                date.dayOfMonth.toInt()
            )
            if(!dateR.isBefore(LocalDate.now().plusDays(1))){
                dateR = null
            }
        }else{
            dateR = null
        }

        return dateR
    }

    fun getColorFromDate(date: CalendarUiState.Date): Color {
        var color:Color = Color.Transparent

        val getDate = createDate(date)
        getDate?.let {
            val findDate = dailyHabits.value.find { LocalDate.parse(it.date) == getDate && it.timesDone != 0 }

            findDate?.let {
                color = if(it.timesDone == _habit.value.times)
                    Color(_habit.value.color)
                else
                    Color(_habit.value.color).copy(alpha = 0.2f)
            }
        }

        return color
    }

    fun getDailyHabitFromDate(date: CalendarUiState.Date):DailyHabit?{
        return dailyHabits.value.find { LocalDate.parse(it.date) == createDate(date) }
    }

    fun setHabitsAndColor(habits:HabitWithDailyHabit){
        _showCalendar.value = false
        _dailyHabits.update { habits.dailyHabits }
        _habit.update { habits.habit }
        _showCalendar.value = true
    }

}