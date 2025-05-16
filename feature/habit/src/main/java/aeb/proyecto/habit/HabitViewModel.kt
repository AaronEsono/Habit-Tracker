package aeb.proyecto.habit

import aeb.proyecto.domain.usecase.habit.GetDailyHabitUseCase
import aeb.proyecto.domain.usecase.habit.GetHabitUseCase
import aeb.proyecto.domain.usecase.habit.GetTypesOfHabitUseCase
import aeb.proyecto.domain.usecase.habit.HabitDatastoreUseCase
import aeb.proyecto.habit.constants.rangeDays
import aeb.proyecto.habit.constants.stopTimeOutMillis
import aeb.proyecto.habit.model.BottomSheetType
import aeb.proyecto.habit.model.DataHabit
import aeb.proyecto.habit.model.pager.PagerElement
import aeb.proyecto.habit.model.pager.PagerSelected
import aeb.proyecto.habit.model.pager.findPagerElement
import aeb.proyecto.habit.model.pager.orderPagerElements
import aeb.proyecto.habit.utils.initializeSelectedTypeIfNeeded
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.DAILY_TAG
import aeb.proyecto.room.model.classes.MONTHLY_TAG
import aeb.proyecto.room.model.classes.RECURRING_TAG
import aeb.proyecto.room.model.classes.WEEKLY_TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val getTypesOfHabitUseCase: GetTypesOfHabitUseCase,
    private val getDailyHabitUseCase: GetDailyHabitUseCase,
    private val habitDatastoreUseCase: HabitDatastoreUseCase,
    private val getHabitUseCase: GetHabitUseCase
):ViewModel() {

    /** Fecha seleccionada actual por el usuario. */
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate:StateFlow<LocalDate> = _selectedDate.asStateFlow()

    /** Tipo de hábito seleccionado por el usuario, reflejado en la pantalla con un tabRow. */
    private val _currentPagerType  = MutableStateFlow<CurrentPagerSelection>(CurrentPagerSelection.Uninitialized)
    val currentPagerType : StateFlow<CurrentPagerSelection> = _currentPagerType.asStateFlow()

    /** Controla los estados de los dialogos y de las hojas inferiores*/
    private val _dataHabitUIState = MutableStateFlow(DataHabit())
    val dataHabitUIState:StateFlow<DataHabit> = _dataHabitUIState.asStateFlow()

    /** Día de inicio de la semana seleccionado por el usuario. */
    private val _startDayOfWeek:StateFlow<DayOfWeek?> = habitDatastoreUseCase.startDayOfWeek
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeOutMillis),
        initialValue = null
    )

    /**
     * Reúne los distintos tipos de hábitos que tiene el usuario.
     * Filtra y ordena los tipos para luego mostrarlos en pantalla.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val availablePagerTypesUiState: StateFlow<PagerTypesUiState> = getTypesOfHabitUseCase()
        .map { types ->
            types.map { findPagerElement(it) }
                .sortedBy { orderPagerElements.indexOf(it) }
        }
        .flatMapLatest { sortedTypes ->
            flow {
                val initialized = initializeSelectedTypeIfNeeded(
                    sortedTypes = sortedTypes,
                    selectedType = _currentPagerType,
                    updateSelected = { _currentPagerType.value = it },
                    habitDatastoreUseCase = habitDatastoreUseCase,
                )

                emit(
                    if (initialized) PagerTypesUiState.Success(sortedTypes)
                    else PagerTypesUiState.Error
                )
            }
        }
        .catch {
            emit(PagerTypesUiState.Error)
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeOutMillis),
            initialValue = PagerTypesUiState.Loading
        )

    /**
     * Calcula el rango de fechas que se debe mostrar, en función del tipo de hábito seleccionado.
     * Por ejemplo: días para hábitos diarios, semanas para semanales, etc.
     */
    val selectedTimeRangeUiState: StateFlow<TimeRangeUiState> =
        combine(_selectedDate, _currentPagerType, _startDayOfWeek) { date, selected, dayOfWeek ->
            Triple(date, selected.getTag(), dayOfWeek)
        }.scan<Triple<LocalDate, String?, DayOfWeek?>, TimeRangeUiState>(TimeRangeUiState.Empty) { previousState, (date, tag, dayOfWeek) ->

            when (tag) {
                DAILY_TAG-> {
                    val prevRange = (previousState as? TimeRangeUiState.Daily)?.days.orEmpty()
                    if (date in prevRange) {
                        previousState // No actualizar el rango
                    } else {
                        val newDays = rangeDays.map { date.plusDays(it.toLong()) }
                        TimeRangeUiState.Daily(newDays)
                    }
                }

                RECURRING_TAG-> {
                    val prevRange = (previousState as? TimeRangeUiState.Recurring)?.days.orEmpty()
                    if (date in prevRange) {
                        previousState // No actualizar el rango
                    } else {
                        val newDays = rangeDays.map { date.plusDays(it.toLong()) }
                        TimeRangeUiState.Recurring(newDays)
                    }
                }

                WEEKLY_TAG -> {
                    if (dayOfWeek == null) return@scan TimeRangeUiState.Empty

                    val startOfWeek = date.with(TemporalAdjusters.previousOrSame(dayOfWeek))
                    val endOfWeek = startOfWeek.plusDays(6)
                    TimeRangeUiState.Weekly(startOfWeek, endOfWeek)
                }

                MONTHLY_TAG -> {
                    val start = date.withDayOfMonth(1)
                    val end = date.withDayOfMonth(date.lengthOfMonth())
                    TimeRangeUiState.Monthly(start, end)
                }

                else -> TimeRangeUiState.Empty
            }
        }.flowOn(Dispatchers.Default)
            .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeOutMillis),
            initialValue = TimeRangeUiState.Empty
        )

    /**
     * Habitos filtrados según el rango temporal y el tipo de hábito.
     * Pilla tanto el hábito como sus registros (dailyHabits).
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val habitsForSelectedTimeUiState: StateFlow<FilteredHabitsUiState> = combine(
        selectedTimeRangeUiState,
        selectedDate
    ){timeState,date -> Pair(timeState,date) }
        .flatMapLatest { (timeState,date) ->
            when (timeState) {
                is TimeRangeUiState.Daily -> {
                    getDailyHabitUseCase.getDailyHabitsByType(date,date, DAILY_TAG) // O usa el tag real
                        .map<List<HabitWithDailyHabit>, FilteredHabitsUiState> {FilteredHabitsUiState.Success(it) }
                        .catch { emit(FilteredHabitsUiState.Error) }
                }
                is TimeRangeUiState.Recurring -> {
                    getDailyHabitUseCase.getDailyHabitsByType(date,date, RECURRING_TAG) // O usa el tag real
                        .map<List<HabitWithDailyHabit>, FilteredHabitsUiState> { FilteredHabitsUiState.Success(it) }
                        .catch { emit(FilteredHabitsUiState.Error) }
                }
                is TimeRangeUiState.Weekly -> {
                    getDailyHabitUseCase.getDailyHabitsByType(timeState.startOfWeek, timeState.endOfWeek, WEEKLY_TAG)
                        .map<List<HabitWithDailyHabit>, FilteredHabitsUiState> { FilteredHabitsUiState.Success(it) }
                        .catch { emit(FilteredHabitsUiState.Error) }
                }
                is TimeRangeUiState.Monthly -> {
                    getDailyHabitUseCase.getDailyHabitsByType(timeState.startOfMonth, timeState.endOfMonth, MONTHLY_TAG)
                        .map<List<HabitWithDailyHabit>, FilteredHabitsUiState> { FilteredHabitsUiState.Success(it) }
                        .catch { emit(FilteredHabitsUiState.Error) }
                }
                else -> flowOf(FilteredHabitsUiState.Empty)
            }
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeOutMillis),
            FilteredHabitsUiState.Loading
        )

    /**
     * Cambia el tipo de hábito seleccionado.
     *
     * @param pagerElement Elemento seleccionado del TabRow.
     */
    fun onPagerTypeSelected(pagerElement: PagerElement) = viewModelScope.launch{
        (availablePagerTypesUiState.value as? PagerTypesUiState.Success)?.availableTypes
            ?.indexOfFirst { it == pagerElement }
            ?.takeIf { it >= 0 }
            ?.let { index ->
                _currentPagerType.value = CurrentPagerSelection.Selected(PagerSelected(index, pagerElement))
                habitDatastoreUseCase.setSelectedHabitType(pagerElement.tag)
            }
    }

    /**
     * Cambia la fecha seleccionada.
     */
    fun onClickTimeRange(date:LocalDate){
        _selectedDate.update { date }
    }

    /**
     * Cambia el bottomSheet seleccionado
     */
    fun onBottomSheetSelected(bottomSheetType: BottomSheetType) {
        _dataHabitUIState.update { currentState ->
            currentState.copy(
                bottomSheetState = currentState
                    .bottomSheetState.copy(
                        type = bottomSheetType,
                        isExpanded = true
                    )
            )
        }
    }

    /**
     * Cierra el bottomSheet seleccionado
     */
    fun onDismissBottomSheet(){
        _dataHabitUIState.update { currentState ->
            currentState.copy(
                bottomSheetState = currentState
                    .bottomSheetState.copy(
                        isExpanded = false
                    )
            )
        }
    }

    /**
     * Abre el bottomSheet para editar un dailyHabit
     */
    fun onClick(id:Long,date: LocalDate) = viewModelScope.launch (Dispatchers.IO){
        val habit = findHabit(id)
        val habitDay = findDay(id,date) ?: HabitDay(id = habit.id, date = date)

        _dataHabitUIState.update { currentState ->
            currentState.copy(
                showEditHabitDayBT = currentState
                    .showEditHabitDayBT.copy(
                        showEditHabitDayBT = true,
                        habit = habit,
                        habitDay = habitDay
                    )
            )
        }
    }

    fun onDismissEdit(){
        _dataHabitUIState.update { currentState ->
            currentState.copy(
                showEditHabitDayBT = currentState.showEditHabitDayBT.copy(
                    showEditHabitDayBT = false
                )
            )
        }
    }

    /**
     *  Permite editar un dailyHabit
     */
    fun onClick(id:Long,date: LocalDate,goalDone: BigDecimal) = viewModelScope.launch (Dispatchers.IO){
        val habit = findHabit(id)
        val habitDay = findDay(id,date)

        // Actualizamos
        if(habitDay != null){
            val updatedHabitDay = habitDay.copy(
                goalDone = habitDay.goalDone.plus(goalDone),
                hourFinishDate = LocalTime.now()
            )
            getDailyHabitUseCase.updateHabitDay(updatedHabitDay)
        }else{
            // Insertamos
            val newHabitDay = HabitDay(
                idHabit = habit.id,
                date = date,
                goalDone = goalDone,
                hourFinishDate = LocalTime.now()
            )
            getDailyHabitUseCase.insertHabitDay(newHabitDay)
        }

    }

    /**
     * Permite terminar el dailyHabit del día seleccionado
     */
    fun onLongClick(id:Long,date: LocalDate) = viewModelScope.launch (Dispatchers.IO){
        val habit = findHabit(id)
        val habitDay = findDay(id,date)

        //Actualizamos
        if(habitDay != null){
            val updatedHabitDay = habitDay.copy(
                goalDone = habit.goal,
                hourFinishDate = LocalTime.now()
            )
            getDailyHabitUseCase.updateHabitDay(updatedHabitDay)
        }else{
            //Insertamos
            val newHabitDay = HabitDay(
                idHabit = habit.id,
                date = date,
                goalDone = habit.goal,
                hourFinishDate = LocalTime.now()
            )
            getDailyHabitUseCase.insertHabitDay(newHabitDay)
        }
    }

    /**
     * Permite eliminar el dailyHabit del día seleccionado
     */
    fun onRestart(id:Long,date: LocalDate) = viewModelScope.launch (Dispatchers.IO){
        getDailyHabitUseCase.deleteHabitDay(id,date)
    }

    /**
     * Busca el dailyHabit del día seleccionado
     */
    private fun findDay(id:Long,date: LocalDate): HabitDay? {
        return getDailyHabitUseCase.getDailyHabitByDate(id,date)
    }

    /**
     * Busca el hábito seleccionado
     */
    private fun findHabit(id:Long):Habit{
        return getHabitUseCase.getHabit(id)
    }
}

sealed class PagerTypesUiState(){
    data class Success(val availableTypes:List<PagerElement>):PagerTypesUiState()
    data object Error:PagerTypesUiState()
    data object Loading:PagerTypesUiState()
}

sealed class FilteredHabitsUiState(){
    data class Success(val habits:List<HabitWithDailyHabit>):FilteredHabitsUiState()
    data object Error:FilteredHabitsUiState()
    data object Loading:FilteredHabitsUiState()
    data object Empty:FilteredHabitsUiState()
}

sealed class CurrentPagerSelection {
    data object Uninitialized : CurrentPagerSelection()
    data class Selected(val pagerSelected: PagerSelected) : CurrentPagerSelection()

    fun getTag(): String? = (this as? Selected)?.pagerSelected?.pagerElement?.tag
}

sealed class TimeRangeUiState {
    data object Empty : TimeRangeUiState()
    data class Daily(val days: List<LocalDate>) : TimeRangeUiState()
    data class Recurring(val days: List<LocalDate>) : TimeRangeUiState()
    data class Weekly(val startOfWeek: LocalDate, val endOfWeek: LocalDate) : TimeRangeUiState()
    data class Monthly(val startOfMonth: LocalDate, val endOfMonth: LocalDate) : TimeRangeUiState()
}