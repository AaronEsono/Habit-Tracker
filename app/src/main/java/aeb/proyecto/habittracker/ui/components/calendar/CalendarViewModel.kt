package aeb.proyecto.habittracker.ui.components.calendar

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.model.calendar.CalendarDataSource
import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _habit: MutableStateFlow<HabitWithDailyHabit> =
        MutableStateFlow(HabitWithDailyHabit())
    val habit: StateFlow<HabitWithDailyHabit> = _habit.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    dates = dataSource.getDates(currentState.yearMonth)
                )
            }
        }
    }

    fun setHabit(habit: HabitWithDailyHabit) {
        _habit.value = habit
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

    fun findHabit(date: CalendarUiState.Date): DailyHabit? {
        return habit.value.dailyHabits.find {
            date.dayOfMonth.isNotEmpty() && LocalDate.parse(it.date) == LocalDate.of(
                uiState.value.yearMonth.year,
                uiState.value.yearMonth.month.value,
                date.dayOfMonth.toInt()
            )
        }
    }

    fun setBackground(dailyHabit: DailyHabit?): Color {
        var colorBackground = Color.Transparent

        if(dailyHabit != null && dailyHabit.timesDone != 0){
           colorBackground = if(dailyHabit.timesDone == habit.value.habit.times) Color(habit.value.habit.color).copy(alpha = 0.75f)
           else  Color(habit.value.habit.color).copy(alpha = 0.2f)
        }

        return colorBackground
    }

}