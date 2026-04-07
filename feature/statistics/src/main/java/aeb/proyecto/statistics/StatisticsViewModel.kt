package aeb.proyecto.statistics

import aeb.proyecto.domain.usecase.statistics.GetHabitSelectedUseCase
import aeb.proyecto.domain.usecase.statistics.GetHabitsStatisticsUseCase
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.statistics.model.DayBoxState
import aeb.proyecto.statistics.model.GoalsDoneState
import aeb.proyecto.statistics.model.GraphicsState
import aeb.proyecto.statistics.model.NUMBER_OF_DAYS
import aeb.proyecto.statistics.model.StatisticsState
import aeb.proyecto.statistics.model.StatisticsSuccessState
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.calendar.source.CalendarDataSource
import android.R.attr.firstDayOfWeek
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
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalAmount
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

    private val _yearHourlyGraphicsSelected = MutableStateFlow(LocalDate.now().year)
    val yearHourlyGraphicsSelected: StateFlow<Int> = _yearHourlyGraphicsSelected.asStateFlow()


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
                                                habitDay.goalDone.compareTo(BigDecimal.ZERO) > 0
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


    @OptIn(ExperimentalCoroutinesApi::class)
    val hourlyGraphicsState: StateFlow<GraphicsState> =
        combine(
            yearHourlyGraphicsSelected,
            statisticsState
        ) { year, state -> year to state }
            .flatMapLatest { (year, state) ->
                flow {
                    when (state) {
                        is StatisticsState.Error, StatisticsState.Loading -> {
                            emit(GraphicsState())
                        }
                        is StatisticsState.Success -> {
                            val successState = state.state
                            if (successState is StatisticsSuccessState.Habits) {
                                val selected = successState.habitSelected
                                val goal = selected.habit.goal
                                val typeHabit = selected.habit.typeHabit

                                // 1. Filtrar por año y por éxito (misma lógica que el mensual)
                                val completedByHour = selected.dailyHabits
                                    .filter { it.date.year == year }
                                    .filter { habitDay ->
                                        when (typeHabit) {
                                            is TypeHabit.Daily, is TypeHabit.Recurring -> {
                                                habitDay.goalDone.compareTo(goal) >= 0
                                            }
                                            else -> {
                                                habitDay.goalDone.compareTo(BigDecimal.ZERO) > 0
                                            }
                                        }
                                    }
                                    // 2. Agrupar por la HORA de finalización (0..23)
                                    .groupBy { it.hourFinishDate.hour }

                                // 3. Crear lista de 24 elementos (uno por cada hora del día)
                                val yValues = (0..23).map { hour ->
                                    completedByHour[hour]?.size?.toDouble() ?: 0.0
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
                            } else {
                                emit(GraphicsState())
                            }
                        }
                    }
                }
            }
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GraphicsState()
            )


    @OptIn(ExperimentalCoroutinesApi::class)
    val goalsDoneState: StateFlow<GoalsDoneState> =
        combine(
            dayOfWeek,
            statisticsState
        ) { firstDayOfWeek, state -> firstDayOfWeek to state }
            .flatMapLatest { (dayOfWeek, state) ->
                flow {
                    val successState = (state as? StatisticsState.Success)?.state as? StatisticsSuccessState.Habits

                    if (successState == null) {
                        emit(GoalsDoneState())
                        return@flow
                    }

                    val selected = successState.habitSelected

                    // Refactorizar las funciones, arreglarlas para que vayan niqueladas, las dos y documentar
                    val completedPeriods = getCompletedPeriods(selected, dayOfWeek)

                    // 1. Calculamos cada parte usando funciones dedicadas
                    val totalCompleted = completedPeriods.size
                    val bestData = calculateStreak(completedPeriods, selected.habit.typeHabit, isCurrent = false)
                    val currentData = calculateStreak(completedPeriods, selected.habit.typeHabit, isCurrent = true)

                    val goalDone = GoalsDoneState(
                        numberOfDaysCompleted = totalCompleted,
                        numberOfBestStreak = bestData.first,
                        bestStreakDates = Pair(bestData.second, bestData.third),
                        numberOfCurrentStreak = currentData.first,
                        currentStreakDates = Pair(currentData.second, currentData.third)
                    )


                    Log.d("TAG", "goalsDoneState: $goalDone")

                    emit(goalDone)
                }
            }
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GoalsDoneState()
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

    fun onHourYearSelected(isNext: Boolean){
        _yearHourlyGraphicsSelected.update {
            if(isNext){
                it + 1
            }else{
                it - 1
            }
        }
    }

    /**
     * Returns the dates on which the habit's goal was successfully achieved.
     *
     * This function extracts the completion dates based on the habit type:
     * - For daily/recurring habits: returns the exact date of completion.
     * - For weekly habits: returns the start date of the completed week.
     * - For monthly habits: returns the first day of the completed month.
     *
     * @param selected The habit and its associated logs to evaluate.
     * @param firstDay The user-defined start of the week (e.g., Monday). Used to group
     * weekly logs from [firstDay] to the end of that week.
     * @return A sorted list of [LocalDate] representing the start of each successful period.
     */
    private fun getCompletedPeriods(selected: HabitWithDailyHabit, firstDay: DayOfWeek): List<LocalDate> {
        val habitGoal = selected.habit.goal
        val type = selected.habit.typeHabit

        // Filter to count records only up to the current date
        val dailyHabits = selected.dailyHabits.filter { !it.date.isAfter(LocalDate.now()) }

        val listDates = when(type){
            TypeHabit.Daily, is TypeHabit.Recurring -> {
                dailyHabits.filter { it.goalDone >= habitGoal }.map { it.date }
            }
            is TypeHabit.Weekly -> {
                // First, group by week start date
                val datesByWeek = dailyHabits.groupBy { it.date.with(TemporalAdjusters.previousOrSame(firstDay)) }

                // If it's a cumulative goal, sum the progress; otherwise, count successful days
                datesByWeek.filter { (_, week) ->
                    if (type.weeklyGoal) week.sumOf { it.goalDone.toDouble() } >= habitGoal.toDouble()
                    else week.count { it.goalDone >= habitGoal } >= type.numberDays
                }.keys.toList()
            }
            is TypeHabit.Monthly -> {
                // First, group by month start date
                val datesByMonth = dailyHabits.groupBy { it.date.with(TemporalAdjusters.firstDayOfMonth()) }

                // If it's a cumulative goal, sum the progress; otherwise, count successful occurrences
                datesByMonth.filter { (_, month) ->
                    if (type.monthlyGoal) month.sumOf { it.goalDone.toDouble() } >= habitGoal.toDouble()
                    else month.count { it.goalDone >= habitGoal } >= type.numberTimes
                }.keys.toList()
            }
        }

        return listDates.sorted()
    }

    private fun calculateStreak(
        completedPeriods: List<LocalDate>,
        type: TypeHabit,
        isCurrent: Boolean // true para racha actual, false para la mejor histórica
    ): Triple<Int, LocalDate, LocalDate> {
        if (completedPeriods.isEmpty()) return Triple(0, LocalDate.now(), LocalDate.now())

        val step: TemporalAmount = when (type) {
            is TypeHabit.Daily -> Period.ofDays(1)
            is TypeHabit.Recurring -> Period.ofDays(type.interval)
            is TypeHabit.Weekly -> Period.ofWeeks(1)
            is TypeHabit.Monthly -> Period.ofMonths(1)
        }

        fun getEndOfPeriod(start: LocalDate): LocalDate {
            return when (type) {
                is TypeHabit.Daily, is TypeHabit.Recurring -> start
                is TypeHabit.Weekly -> start.plusDays(6)
                is TypeHabit.Monthly -> start.with(TemporalAdjusters.lastDayOfMonth())
            }
        }

        // --- BLOQUE 1: CALCULAR LA RACHA QUE LLEGUE HASTA EL FINAL ---
        // Esto nos sirve tanto para la Mejor como para la Actual
        var lastStreakCount = 1
        var lastStreakStart = completedPeriods.first()

        // Recorremos de atrás hacia adelante para encontrar la racha "viva" al final de la lista
        for (i in completedPeriods.size - 1 downTo 1) {
            if (completedPeriods[i] == completedPeriods[i - 1].plus(step)) {
                lastStreakCount++
                lastStreakStart = completedPeriods[i - 1]
            } else {
                // En cuanto encontramos un hueco, paramos: ya tenemos la racha final
                break
            }
        }

        // --- BLOQUE 3: SI PIDEN LA MEJOR RACHA (HISTÓRICA) ---
        // Aquí sí usamos el bucle completo que ya teníamos
        var bestStreak = 0
        var bestStart = completedPeriods.first()
        var bestEnd = getEndOfPeriod(completedPeriods.first())
        var currentStreak = 1
        var currentStart = completedPeriods.first()

        for (i in 1 until completedPeriods.size) {
            if (completedPeriods[i] == completedPeriods[i - 1].plus(step)) {
                currentStreak++
            } else {
                if (currentStreak > bestStreak) {
                    bestStreak = currentStreak
                    bestStart = currentStart
                    bestEnd = getEndOfPeriod(completedPeriods[i - 1])
                }
                currentStreak = 1
                currentStart = completedPeriods[i]
            }
        }
        // Validar la racha que termina en el último elemento
        if (currentStreak > bestStreak) {
            bestStreak = currentStreak
            bestStart = currentStart
            bestEnd = getEndOfPeriod(completedPeriods.last())
        }

        // --- BLOQUE 2: SI PIDEN LA RACHA ACTUAL ---
        if (isCurrent) {
            val now = LocalDate.now()
            val lastCompleted = completedPeriods.last()

            val isAlive = when(type) {
                is TypeHabit.Recurring -> !now.isAfter(lastCompleted.plus(step))
                is TypeHabit.Daily -> !now.isAfter(lastCompleted.plusDays(1))
                is TypeHabit.Weekly -> {
                    val currentWeekStart = now.with(TemporalAdjusters.previousOrSame(lastStreakStart.dayOfWeek))
                    lastCompleted == currentWeekStart || lastCompleted == currentWeekStart.minusWeeks(1)
                }
                is TypeHabit.Monthly -> {
                    val currentMonthStart = now.with(TemporalAdjusters.firstDayOfMonth())
                    lastCompleted == currentMonthStart || lastCompleted == currentMonthStart.minusMonths(1)
                }
            }

            return if (isAlive) {
                // Devolvemos la racha del final, sea grande o pequeña (ej: 2 días)
                Triple(lastStreakCount, lastStreakStart, getEndOfPeriod(lastCompleted))
            } else {
                Triple(0, LocalDate.now(), LocalDate.now())
            }
        }

        return Triple(bestStreak, bestStart, bestEnd)
    }

}