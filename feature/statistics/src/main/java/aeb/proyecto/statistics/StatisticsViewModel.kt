package aeb.proyecto.statistics

import aeb.proyecto.domain.usecase.statistics.GetHabitSelectedUseCase
import aeb.proyecto.domain.usecase.statistics.GetHabitsStatisticsUseCase
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.statistics.components.common.donutChart.PieChartData
import aeb.proyecto.statistics.components.common.donutChart.listaDePrueba
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
import java.time.temporal.ChronoUnit
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
                    val consistency = calculateConsistency(completedPeriods, selected.habit.typeHabit)

                    val goalDone = GoalsDoneState(
                        numberOfDaysCompleted = totalCompleted,
                        numberOfBestStreak = bestData.first,
                        consistencyPercentage = consistency,
                        bestStreakDates = Pair(bestData.second, bestData.third),
                        numberOfCurrentStreak = currentData.first,
                        currentStreakDates = Pair(currentData.second, currentData.third)
                    )

                    emit(goalDone)
                }
            }
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GoalsDoneState()
            )


    @OptIn(ExperimentalCoroutinesApi::class)
    val pieChartState: StateFlow<List<PieChartData>> =
        statisticsState
            .flatMapLatest {
                flow {
                    emit(listaDePrueba)
                }
            }
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = listOf()
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

    /**
     * Calculates the habit's streak data, supporting both historical records and the current status.
     *
     * This function evaluates the consecutive completion periods based on the provided history.
     * It determines how many consecutive "steps" (days, weeks, or months) the habit was
     * maintained without interruption.
     *
     * @param completedPeriods A sorted list of [LocalDate] representing the start of each successful period.
     * @param type The [TypeHabit] configuration used to define the time interval between successes.
     * @param isCurrent Boolean flag:
     * - If `true`, returns the currently active streak (resetting to 0 if the streak is broken today).
     * - If `false`, returns the longest historical streak ever achieved for this habit.
     * @return A [Triple] containing:
     * 1. The streak count (number of consecutive periods).
     * 2. The start date of the streak ([LocalDate]).
     * 3. The end date of the streak ([LocalDate]).
     * @see getCompletedPeriods
     */
    private fun calculateStreak(
        completedPeriods: List<LocalDate>,
        type: TypeHabit,
        isCurrent: Boolean
    ): Triple<Int, LocalDate, LocalDate> {
        if (completedPeriods.isEmpty()) return Triple(0, LocalDate.now(), LocalDate.now())

        /**
         * Defines the temporal distance between two consecutive successful periods.
         * This unit is used to verify if the streak continues or has been broken.
         */
        val step: TemporalAmount = when (type) {
            is TypeHabit.Daily -> Period.ofDays(1)
            is TypeHabit.Recurring -> Period.ofDays(type.interval)
            is TypeHabit.Weekly -> Period.ofWeeks(1)
            is TypeHabit.Monthly -> Period.ofMonths(1)
        }

        /**
         * Calculates the inclusive end date of a given period based on the habit type.
         * * - Daily/Recurring: The period ends on the same day it starts.
         * - Weekly: The period ends 6 days after the start (the full week).
         * - Monthly: The period ends on the last day of that specific month.
         *
         * @param start The initial date of the successful period.
         * @return The [LocalDate] representing the final day of that period.
         */
        fun getEndOfPeriod(start: LocalDate): LocalDate {
            return when (type) {
                is TypeHabit.Daily, is TypeHabit.Recurring -> start
                is TypeHabit.Weekly -> start.plusDays(6)
                is TypeHabit.Monthly -> start.with(TemporalAdjusters.lastDayOfMonth())
            }
        }

        var bestStreak = 0
        var bestStart = completedPeriods.first()
        var bestEnd = getEndOfPeriod(completedPeriods.first())
        var currentStreak = 1
        var currentStart = completedPeriods.first()

        // --- HISTORICAL STREAK CALCULATION ---
        // This block identifies the longest consecutive chain of successful periods in the habit's history.
        if(!isCurrent){
            for (i in 1 until completedPeriods.size){
                // Check if the current period follows the previous one without any gaps
                if(completedPeriods[i] == completedPeriods[i-1].plus(step)){
                    currentStreak++
                }else{
                    // A gap was found; check if the streak just ended is the new all-time record
                    if(currentStreak > bestStreak){
                        bestStreak = currentStreak
                        bestStart = currentStart
                        bestEnd = getEndOfPeriod(completedPeriods[i - 1])
                    }
                    // Reset for the next potential streak starting at the current period
                    currentStreak = 1
                    currentStart = completedPeriods[i]
                }
            }

            // Final check: validate the last streak in the list (since the 'else' block
            // won't trigger if the history ends while a streak is still active)
            if (currentStreak > bestStreak) {
                bestStreak = currentStreak
                bestStart = currentStart
                bestEnd = getEndOfPeriod(completedPeriods.last())
            }

        // --- CURRENT STREAK CALCULATION ---
        // This block determines if the habit is still active today and calculates its ongoing progress.
        } else{
            val now = LocalDate.now()
            val lastCompleted = completedPeriods.last()

            // Identify the theoretical start date of the current period based on the habit type
            val currentPeriodStart = when (type) {
                is TypeHabit.Daily -> now
                // For Weekly habits, we align 'now' to the same weekday as the last completion
                // to check if we are still within the same weekly cycle.
                is TypeHabit.Weekly -> now.with(TemporalAdjusters.previousOrSame(completedPeriods.last().dayOfWeek))
                is TypeHabit.Monthly -> now.with(TemporalAdjusters.firstDayOfMonth())
                is TypeHabit.Recurring -> {
                    // Logic for Recurring: Calculate the most recent 'due date' relative to the start date.
                    // Formula: (Current Day - Start Day) / Interval * Interval
                    val daysFromStart = java.time.temporal.ChronoUnit.DAYS.between(type.date, now)
                    val lastExpectedInterval = (daysFromStart / type.interval) * type.interval
                    type.date.plusDays(lastExpectedInterval)
                }
            }

            // A streak is considered "alive" if the last recorded completion
            // occurred during the current period or the immediately preceding one.
            val isAlive = lastCompleted == currentPeriodStart || lastCompleted == currentPeriodStart.minus(step)

            if (isAlive) {
                // If the streak is alive, we count backwards to find the length
                // of the uninterrupted chain ending at the most recent record.
                var count = 1
                var startDate = lastCompleted

                // Iterate backwards through the list to find consecutive periods
                for (i in completedPeriods.size - 1 downTo 1) {
                    // Check if the current period in the loop is exactly one 'step' after the previous one
                    if (completedPeriods[i] == completedPeriods[i - 1].plus(step)) {
                        count++
                        startDate = completedPeriods[i - 1]
                    } else {
                        break // Continuity broken; stop counting
                    }
                }

                bestStreak = count
                bestStart = startDate
                bestEnd = getEndOfPeriod(lastCompleted)
            } else {
                // Streak is "dead": the last record is older than the allowed grace period.
                bestStreak = 0
                bestStart = now
                bestEnd = now
            }
        }

        return Triple(bestStreak, bestStart, bestEnd)
    }

    /**
     * Calculates the habit's consistency score as a percentage.
     *
     * This score represents the ratio of completed periods against the total
     * possible periods since the user first started recording the habit.
     *
     * @param completedPeriods Sorted list of dates where the goal was met.
     * @param type The habit configuration to determine the time step.
     * @return An integer percentage between 0 and 100.
     */
    private fun calculateConsistency(
        completedPeriods: List<LocalDate>,
        type: TypeHabit
    ): Int {
        if (completedPeriods.isEmpty()) return 0

        val firstRecord = completedPeriods.first()
        val today = LocalDate.now()

        // We calculate how many periods have passed in total since the beginning
        val totalPossiblePeriods = when (type) {
            is TypeHabit.Daily -> ChronoUnit.DAYS.between(firstRecord, today) + 1
            is TypeHabit.Weekly -> ChronoUnit.WEEKS.between(firstRecord, today) + 1
            is TypeHabit.Monthly -> ChronoUnit.MONTHS.between(firstRecord, today) + 1
            is TypeHabit.Recurring -> {
                val daysBetween = ChronoUnit.DAYS.between(firstRecord, today)
                (daysBetween / type.interval) + 1
            }
        }

        return if (totalPossiblePeriods > 0) {
            // (Done / Possibilities) * 100
            ((completedPeriods.size.toDouble() / totalPossiblePeriods.toDouble()) * 100).toInt().coerceIn(0, 100)
        } else {
            0
        }
    }

    fun getDataPieChart(): List<PieChartData>{
        return listOf()
    }

}