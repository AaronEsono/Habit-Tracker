package aeb.proyecto.statistics

import aeb.proyecto.domain.usecase.statistics.GetHabitSelectedUseCase
import aeb.proyecto.domain.usecase.statistics.GetHabitsStatisticsUseCase
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.statistics.model.DayBoxState
import aeb.proyecto.statistics.model.GraphicsState
import aeb.proyecto.statistics.model.NUMBER_OF_DAYS
import aeb.proyecto.statistics.model.StatisticsState
import aeb.proyecto.statistics.model.StatisticsSuccessState
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.calendar.source.CalendarDataSource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getHabitsStatisticsUseCase: GetHabitsStatisticsUseCase,
    private val getHabitSelectedUseCase: GetHabitSelectedUseCase
) : ViewModel() {

    private val _yearMonth:MutableStateFlow<YearMonth> = MutableStateFlow(YearMonth.now())
    val yearMonth:StateFlow<YearMonth> = _yearMonth.asStateFlow()

    private val _yearGraphicsSelected = MutableStateFlow(LocalDate.now().year)
    val yearGraphicsSelected: StateFlow<Int> = _yearGraphicsSelected.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val statisticsState: StateFlow<StatisticsState> =
        combine(
            getHabitsStatisticsUseCase.getAllHabits(),
            getHabitSelectedUseCase.getHabitSelected()
        ) { habits, habitSelected ->
            habits to habitSelected
        }
            .flatMapLatest { (habits, habitSelected): Pair<List<Habit>, Long?> ->

                flow<StatisticsState> {
                    if (habits.isEmpty()) {
                        emit(
                            StatisticsState.Success(
                                StatisticsSuccessState.Empty
                            )
                        )
                    } else {
                        val idSelected =
                            habits.firstOrNull { it.id == habitSelected }?.id
                                ?: habits.first().id

                        getHabitsStatisticsUseCase
                            .getHabitWithDailyHabit(idSelected)
                            .collect { habitWithDailyHabit ->

                                habitWithDailyHabit?.let {
                                    emit(
                                        StatisticsState.Success(
                                            StatisticsSuccessState.Habits(
                                                habits,
                                                habitWithDailyHabit
                                            )
                                        )
                                    )
                                }
                            }
                    }
                }
            }
            .onStart {
                emit(StatisticsState.Loading)
            }
            .catch { emit(StatisticsState.Error(it.message ?: "")) }
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = StatisticsState.Loading
            )


    val dayOfWeek: StateFlow<DayOfWeek> = getHabitSelectedUseCase.getDaySelected()
        .map {
            DayOfWeek.valueOf(it ?: DayOfWeek.MONDAY.name)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DayOfWeek.MONDAY
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarUIState: StateFlow<CalendarUIState<HabitWithDay>> =
        combine(
            yearMonth,
            dayOfWeek,
            statisticsState
        ) { yearMonth, startDayOfWeek, statisticsState ->
            Triple(yearMonth, startDayOfWeek, statisticsState)
        }
            .flatMapLatest { (yearMonth, startDayOfWeek, statisticsState) ->
                flow {
                    when(statisticsState){
                        is StatisticsState.Error, StatisticsState.Loading -> {
                            emit(CalendarUIState(emptyList()))
                        }
                        is StatisticsState.Success -> {
                            if(statisticsState.state is StatisticsSuccessState.Empty){
                                emit(CalendarUIState(emptyList()))
                            }else{
                                val state = statisticsState.state as StatisticsSuccessState.Habits

                                val days = CalendarDataSource().getDates(
                                    startDayOfWeek,
                                    yearMonth
                                ) { date ->
                                    HabitWithDay(
                                        habit = state.habitSelected.habit,
                                        day = state.habitSelected.dailyHabits
                                            .find { it.date == date }
                                            ?: HabitDay()
                                    )
                                }

                                emit(CalendarUIState(days))
                            }
                        }
                    }

                }
            }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CalendarUIState(emptyList())
            )


    @OptIn(ExperimentalCoroutinesApi::class)
    val boxUIState: StateFlow<List<BoxUIState>> =
        combine(
            dayOfWeek,
            statisticsState
        ) { startDayOfWeek, statisticsState ->
            Pair(startDayOfWeek, statisticsState)
        }
            .flatMapLatest { (startDayOfWeek, statisticsState) ->
                flow {
                    val today = LocalDate.now()

                    when(statisticsState){
                        is StatisticsState.Error, StatisticsState.Loading -> {
                            emit(emptyList<BoxUIState>())
                        }
                        is StatisticsState.Success -> {
                            if(statisticsState.state is StatisticsSuccessState.Empty){
                                emit(emptyList<BoxUIState>())
                            }else{

                                val dailyHabits = (statisticsState.state as StatisticsSuccessState.Habits)
                                    .habitSelected
                                    .dailyHabits
                                    .associateBy { it.date }

                                val goal = statisticsState.state.habitSelected.habit.goal


                                val result = (0 until NUMBER_OF_DAYS).map { offset ->
                                    val day = today.minusDays(offset.toLong())
                                    val dailyHabit = dailyHabits[day]

                                    val dayState = when {
                                        dailyHabit == null || dailyHabit.goalDone.compareTo(BigDecimal.ZERO) == 0 ->
                                            DayBoxState.NotDone

                                        dailyHabit.goalDone.compareTo(goal) == 0 ->
                                            DayBoxState.Done

                                        dailyHabit.goalDone > BigDecimal.ZERO &&
                                                dailyHabit.goalDone < goal ->
                                            DayBoxState.Uncompleted

                                        else ->
                                            DayBoxState.Done
                                    }

                                    BoxUIState(
                                        day = day,
                                        dayState = dayState
                                    )
                                }

                                emit(result.asReversed())
                            }
                        }
                    }

                }
            }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    @OptIn(ExperimentalCoroutinesApi::class)
    val graphicsState: StateFlow<GraphicsState> =
        combine(
            yearGraphicsSelected,
            statisticsState
        ) { year, state -> year to state }
            .flatMapLatest { (year, state) ->
                flow {
                    when(state){
                        is StatisticsState.Error, StatisticsState.Loading -> {
                            emit(GraphicsState())
                        }
                        is StatisticsState.Success -> {
                            if(state.state is StatisticsSuccessState.Empty){
                                emit(GraphicsState())
                            }else{
                                val successState = state.state
                                val selected = (successState as StatisticsSuccessState.Habits).habitSelected
                                val goal = selected.habit.goal
                                val typeHabit = selected.habit.typeHabit

                                val completedByMonth = selected.dailyHabits
                                    .filter { it.date.year == year } // Primero filtramos el año
                                    .filter { habitDay ->
                                        when (typeHabit) {
                                            is TypeHabit.Daily, is TypeHabit.Recurring -> {
                                                habitDay.goalDone.compareTo(goal) >= 0
                                            }
                                            else -> {
                                                habitDay.goalDone.compareTo(java.math.BigDecimal.ZERO) > 0
                                            }
                                        }
                                    }
                                    .groupBy { it.date.monthValue }


                                val yValues = (1..12).map { month ->
                                    completedByMonth[month]?.size?.toDouble() ?: 0.0
                                }


                                val chartModel = CartesianChartModel(
                                    LineCartesianLayerModel.build {
                                        series(yValues)
                                    }
                                )

                                emit(GraphicsState(
                                    color = selected.habit.color,
                                    model = chartModel
                                ))
                            }
                        }
                    }

                }
            }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GraphicsState()
            )

    fun onCLickCard(id:Long) = viewModelScope.launch(Dispatchers.IO){
        getHabitSelectedUseCase.setHabitSelected(id)
    }

    fun onMonthButtonClicked(yearMonth: YearMonth){
        _yearMonth.update {yearMonth}
    }

    fun onYearSelected(isNext: Boolean){
        _yearGraphicsSelected.update {
            if(isNext){
                it + 1
            }else{
                it - 1
            }
        }
    }

}