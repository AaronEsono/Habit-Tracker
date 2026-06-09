package aeb.proyecto.habit.components.common.habitCards.monthlyCard

import aeb.proyecto.habit.components.common.habitCards.utils.getSelected
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.calendar.source.CalendarDataSource
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

/**
 * Generates the UI state for a calendar view populated with habit progress data.
 *
 * Maps a month's dates to their corresponding [HabitWithDay] status,
 * associating the habit configuration with the specific daily progress.
 *
 * @param startOfMonth The [LocalDate] representing the first day of the target month.
 * @param startDayWeek The [DayOfWeek] used as the start of the week (locale-dependent).
 * @param habits The [HabitWithDailyHabit] containing the habit metadata and progress records.
 * @return A [CalendarUIState] populated with [HabitWithDay] objects for each calendar cell.
 */
fun getDates(
    startOfMonth: LocalDate,
    startDayWeek:DayOfWeek?,
    habits: HabitWithDailyHabit
): CalendarUIState<HabitWithDay>{

    val yearMonth = YearMonth.of(startOfMonth.year, startOfMonth.month)
    val firstDay = startDayWeek ?: DayOfWeek.MONDAY
    val calendarDataSource = CalendarDataSource()

    val getDates = calendarDataSource.getDates(firstDay,yearMonth){ date ->
        HabitWithDay(
            habit = habits.habit,
            day = getSelected(date,habits.dailyHabits) ?: HabitDay()
        )
    }

    return CalendarUIState(getDates)
}

/**
 * Calculates the number of days in a given month where the habit goal was successfully met.
 *
 * @param habits The [HabitWithDailyHabit] containing progress records.
 * @param startOfMonth The [LocalDate] indicating the month to evaluate.
 * @return A [BigDecimal] representing the total count of completed days.
 */
fun daysCompletedOnAMonth(habits: HabitWithDailyHabit, startOfMonth: LocalDate):BigDecimal{
    val daysInThisMonth = startOfMonth.lengthOfMonth()

    return habits.dailyHabits.filter {
        it.date in startOfMonth..startOfMonth.plusDays(daysInThisMonth.toLong())
                && it.goalDone >= habits.habit.goal
    }.size.toBigDecimal()
}

/**
 * Determines the maximum possible days required to complete a target goal within a specific month.
 *
 * @param goalToDo The total target count required.
 * @param startOfMonth The [LocalDate] representing the month duration context.
 * @return The effective number of days to complete.
 */
fun numberOfDaysToComplete(goalToDo: Int, startOfMonth: LocalDate):Int{
    val daysInThisMonth = startOfMonth.lengthOfMonth()
    return if(daysInThisMonth < goalToDo) daysInThisMonth else goalToDo
}

/**
 * Aggregates the total progress (sum of [goalDone]) achieved by a habit throughout a full month.
 *
 * @param habits The [HabitWithDailyHabit] containing all daily progress records.
 * @param startOfMonth The [LocalDate] indicating the month to aggregate.
 * @return A [BigDecimal] representing the total sum of completed progress across the month.
 */
fun timesCompletedInAEntireMonth(habits: HabitWithDailyHabit, startOfMonth: LocalDate):BigDecimal{
    val daysInThisMonth = startOfMonth.lengthOfMonth()

    return habits.dailyHabits.filter {
        it.date in startOfMonth..startOfMonth.plusDays(daysInThisMonth.toLong())
    }.sumOf {
        it.goalDone
    }
}