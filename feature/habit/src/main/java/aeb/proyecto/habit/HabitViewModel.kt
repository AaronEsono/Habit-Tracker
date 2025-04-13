package aeb.proyecto.habit

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.domain.usecase.habit.GetDailyHabitUseCase
import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.habit.model.PagerSelected
import aeb.proyecto.habit.model.findPagerElement
import aeb.proyecto.habit.model.orderPagerElements
import aeb.proyecto.habit.utils.initializeSelectedTypeIfNeeded
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.DAILY_TAG
import aeb.proyecto.room.model.classes.MONTHLY_TAG
import aeb.proyecto.room.model.classes.RECURRING_TAG
import aeb.proyecto.room.model.classes.WEEKLY_TAG
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo,
    private val getDailyHabitUseCase: GetDailyHabitUseCase,
    private val datastoreInterface: DatastoreInterface
):ViewModel() {

    private val _dateSelected = MutableStateFlow(LocalDate.now())
    val dateSelected:StateFlow<LocalDate> = _dateSelected.asStateFlow()

    private val _selectedType = MutableStateFlow<SelectedTypeState>(SelectedTypeState.Uninitialized)
    val selectedType: StateFlow<SelectedTypeState> = _selectedType.asStateFlow()

    private val _startDayOfWeek:StateFlow<DayOfWeek?> = datastoreInterface.dayOfWeek
        .map { dayOfWeek ->
            DayOfWeek.valueOf(dayOfWeek)
        }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val typeUIState: StateFlow<TypeUIState> = habitWithDailyHabitRepo.getExistingTypesHabit()
        .map { types ->
            types.map { findPagerElement(it) }
                .sortedBy { orderPagerElements.indexOf(it) }
        }
        .flatMapLatest { sortedTypes ->
            flow {
                val initialized = initializeSelectedTypeIfNeeded(
                    sortedTypes = sortedTypes,
                    selectedType = selectedType,
                    updateSelected = { _selectedType.value = it },
                    datastore = datastoreInterface,
                )

                emit(
                    if (initialized) TypeUIState.Success(sortedTypes)
                    else TypeUIState.Error
                )
            }
        }
        .catch {
            emit(TypeUIState.Error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TypeUIState.Loading
        )

    private val timeSelectedUIState: StateFlow<TimeSelectedUiState> =
        combine(_dateSelected, _selectedType,_startDayOfWeek){ date, selected,dayOfWeek ->
            val tag = selected.getTag()
            when(tag){
                DAILY_TAG ->{
                    val days = (-150..50).map { date.plusDays(it.toLong()) }
                    TimeSelectedUiState.Daily(days, date)
                }
                RECURRING_TAG ->{
                    val days = (-150..50).map { date.plusDays(it.toLong()) }
                    TimeSelectedUiState.Recurring(days, date)
                }
                WEEKLY_TAG ->{
                    if (dayOfWeek == null) return@combine TimeSelectedUiState.Empty

                    val startOfWeek = date.with(TemporalAdjusters.previousOrSame(dayOfWeek))
                    val endOfWeek = startOfWeek.plusDays(6)
                    TimeSelectedUiState.Weekly(startOfWeek, endOfWeek)
                }
                MONTHLY_TAG ->{
                    val start = date.withDayOfMonth(1)
                    val end = date.withDayOfMonth(date.lengthOfMonth())
                    TimeSelectedUiState.Monthly(start, end)
                }
                else -> TimeSelectedUiState.Empty
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TimeSelectedUiState.Empty
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val habitUIState: StateFlow<HabitsUIState> = timeSelectedUIState
        .flatMapLatest { timeState ->
            when (timeState) {
                is TimeSelectedUiState.Daily -> {
                    getDailyHabitUseCase(timeState.selected,timeState.selected, DAILY_TAG) // O usa el tag real
                        .map<List<HabitWithDailyHabit>, HabitsUIState> { HabitsUIState.Success(it) }
                        .catch { emit(HabitsUIState.Error) }
                }
                is TimeSelectedUiState.Recurring -> {
                    getDailyHabitUseCase(timeState.selected,timeState.selected, RECURRING_TAG) // O usa el tag real
                        .map<List<HabitWithDailyHabit>, HabitsUIState> { HabitsUIState.Success(it) }
                        .catch { emit(HabitsUIState.Error) }
                }
                is TimeSelectedUiState.Weekly -> {
                    getDailyHabitUseCase(timeState.startOfWeek, timeState.endOfWeek, WEEKLY_TAG)
                        .map<List<HabitWithDailyHabit>, HabitsUIState> { HabitsUIState.Success(it) }
                        .catch { emit(HabitsUIState.Error) }
                }
                is TimeSelectedUiState.Monthly -> {
                    getDailyHabitUseCase(timeState.startOfMonth, timeState.endOfMonth, MONTHLY_TAG)
                        .map<List<HabitWithDailyHabit>, HabitsUIState> { HabitsUIState.Success(it) }
                        .catch { emit(HabitsUIState.Error) }
                }
                else -> flowOf(HabitsUIState.Empty)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HabitsUIState.Loading
        )

    fun onClickTab(pagerElement: PagerElement) = viewModelScope.launch{
        (typeUIState.value as? TypeUIState.Success)?.availableTypes
            ?.indexOfFirst { it == pagerElement }
            ?.takeIf { it >= 0 }
            ?.let { index ->
                _selectedType.value = SelectedTypeState.Selected(PagerSelected(index, pagerElement))
                datastoreInterface.setTypeSelectedDate(pagerElement.tag)
            }
    }

}

sealed class TypeUIState(){
    data class Success(val availableTypes:List<PagerElement>):TypeUIState()
    data object Error:TypeUIState()
    data object Loading:TypeUIState()
}

sealed class HabitsUIState(){
    data class Success(val habits:List<HabitWithDailyHabit>):HabitsUIState()
    data object Error:HabitsUIState()
    data object Loading:HabitsUIState()
    data object Empty:HabitsUIState()
}

sealed class SelectedTypeState {
    data object Uninitialized : SelectedTypeState()
    data class Selected(val pagerSelected: PagerSelected) : SelectedTypeState()

    fun getTag(): String? = (this as? Selected)?.pagerSelected?.pagerElement?.tag
}

sealed class TimeSelectedUiState {
    data object Empty : TimeSelectedUiState()
    data class Daily(val days: List<LocalDate>, val selected: LocalDate) : TimeSelectedUiState()
    data class Recurring(val days: List<LocalDate>, val selected: LocalDate) : TimeSelectedUiState()
    data class Weekly(val startOfWeek: LocalDate, val endOfWeek: LocalDate) : TimeSelectedUiState()
    data class Monthly(val startOfMonth: LocalDate, val endOfMonth: LocalDate) : TimeSelectedUiState()
}