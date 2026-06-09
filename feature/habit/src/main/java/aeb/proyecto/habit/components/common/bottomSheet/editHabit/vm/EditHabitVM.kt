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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.YearMonth
import javax.inject.Inject

/**
 * ViewModel orchestrator for the habit modification module.
 * Manages the reactive state lifecycle for habit-specific configurations,
 * including date ranges, temporal offsets, and persistence identifiers.
 *
 * @property getHabitUseCase Interactor for retrieving individual habit entities.
 * @property getDailyHabitUseCase Interactor for resolving daily progression data.
 */
@HiltViewModel
class EditHabitVM @Inject constructor(
    getHabitUseCase: GetHabitUseCase,
    getDailyHabitUseCase: GetDailyHabitUseCase
): ViewModel() {

    /** Identifier for the target habit currently being edited; null if initializing. */
    private val _idHabit: MutableStateFlow<Long?> = MutableStateFlow(null)
    val idHabit: Flow<Long?> = _idHabit.asStateFlow()

    /** Tracks the active calendar window for habit progression analysis. */
    private val _yearMonth:MutableStateFlow<YearMonth> = MutableStateFlow(YearMonth.now())
    val yearMonth:StateFlow<YearMonth> = _yearMonth.asStateFlow()

    /** Defines the weekly calendar synchronization anchor (e.g., Monday vs Sunday start). */
    private val _startDayOfWeek: MutableStateFlow<DayOfWeek?> = MutableStateFlow(null)
    val startDayOfWeek: Flow<DayOfWeek?> = _startDayOfWeek.asStateFlow()

    /**
     * Reactive pipeline that resolves calendar data based on filtered date ranges
     * and habit context. Automatically recomputes on [yearMonth] or [startDayOfWeek] updates.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarDays: StateFlow<CalendarUIState<HabitWithDay>> =
        combine(idHabit, yearMonth, startDayOfWeek) { idHabit, yearMonth, startDayOfWeek ->
            Triple(idHabit, yearMonth, startDayOfWeek)
        }
            .flatMapLatest { (idHabit, yearMonth, startDayOfWeek) ->

                if (idHabit == null) {
                    return@flatMapLatest flowOf(CalendarUIState(emptyList()))
                }

                val start = yearMonth.atDay(1)
                val end = yearMonth.atEndOfMonth()

                getDailyHabitUseCase
                    .getHabitWithDailyHabitsByDate(idHabit, start, end)
                    .map { habitWithDailyHabits ->

                        if(habitWithDailyHabits != null){
                            val days = CalendarDataSource().getDates(
                                startDayOfWeek ?: DayOfWeek.MONDAY,
                                yearMonth
                            ) { date ->
                                HabitWithDay(
                                    habit = habitWithDailyHabits.habit,
                                    day = habitWithDailyHabits.dailyHabits
                                        .find { it.date == date }
                                        ?: HabitDay()
                                )
                            }

                            CalendarUIState(days)
                        }else{
                            CalendarUIState(emptyList())
                        }

                    }
            }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CalendarUIState(emptyList())
            )

    /** Updates the target habit identifier, triggering automatic state reloading. */
    fun getIdHabit(id:Long){
        _idHabit.value = id
    }

    /**
     * Exposes the reactive state of the habit being edited.
     * Derives [EditHabitState] by fetching the entity from [getHabitUseCase] upon ID change.
     */
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

    /** Updates the active view period for the calendar grid. */
    fun onMonthButtonClicked(yearMonth: YearMonth){
        _yearMonth.update {yearMonth}
    }

    /** Sets the weekly calendar synchronization anchor. */
    fun setDay(day: DayOfWeek?){
        _startDayOfWeek.update {day}
    }

}