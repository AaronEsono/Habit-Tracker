package aeb.proyecto.habit.components.common.bottomSheet.editHabit.vm

import aeb.proyecto.domain.usecase.habit.GetDailyHabitUseCase
import aeb.proyecto.domain.usecase.habit.GetHabitUseCase
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.state.EditHabitState
import aeb.proyecto.habit.components.common.habitCards.utils.getSelected
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.calendar.source.CalendarDataSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class EditHabitVM @Inject constructor(
    getHabitUseCase: GetHabitUseCase,
    getDailyHabitUseCase: GetDailyHabitUseCase
): ViewModel() {

    private val _idHabit: MutableStateFlow<Long?> = MutableStateFlow(null)
    val idHabit: Flow<Long?> = _idHabit.asStateFlow()

    private val _yearMonth:MutableStateFlow<YearMonth> = MutableStateFlow(YearMonth.now())
    val yearMonth:StateFlow<YearMonth> = _yearMonth.asStateFlow()

    private val _startDayOfWeek: MutableStateFlow<DayOfWeek?> = MutableStateFlow(null)
    val startDayOfWeek: Flow<DayOfWeek?> = _startDayOfWeek.asStateFlow()

    val calendarDays: StateFlow<CalendarUIState<HabitWithDay>> = combine(
        idHabit, yearMonth, startDayOfWeek
    ) { idHabit, yearMonth, startDayOfWeek ->

        if (idHabit == null) {
            return@combine CalendarUIState(emptyList())
        }

        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()

        val habitWithDailyHabits = getDailyHabitUseCase
            .getHabitWithDailyHabitsByDate(idHabit, startDate, endDate)

        val days = CalendarDataSource().getDates(
            startDayOfWeek ?: DayOfWeek.MONDAY,
            yearMonth
        ) { date ->
            HabitWithDay(
                habit = habitWithDailyHabits.habit,
                day = getSelected(date, habitWithDailyHabits.dailyHabits) ?: HabitDay()
            )
        }

        CalendarUIState(days)
    }.flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = CalendarUIState(emptyList())
        )

    fun getIdHabit(id:Long){
        _idHabit.value = id
    }

    val bottomSheetState: StateFlow<EditHabitState> = _idHabit
        .map { id ->
            id ?: return@map EditHabitState.Error("Error inesperado")

            val habit = getHabitUseCase.getHabit(id)
            return@map EditHabitState.Success(habit)
        }
        .catch { id ->
            emit(EditHabitState.Error("Error inesperado"))
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = EditHabitState.Loading
    )

    fun onMonthButtonClicked(yearMonth: YearMonth){
        _yearMonth.update {yearMonth}
    }

    fun setDay(day: DayOfWeek?){
        _startDayOfWeek.update {day}
    }

}