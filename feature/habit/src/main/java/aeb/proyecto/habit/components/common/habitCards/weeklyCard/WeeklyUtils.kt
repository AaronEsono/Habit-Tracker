package aeb.proyecto.habit.components.common.habitCards.weeklyCard

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Retrieves the specific progress record for a habit on a given date.
 *
 * If no record exists for the provided [date], it initializes a new [HabitDay]
 * associated with the habit's ID to maintain consistency in the UI state.
 *
 * @param habits The [HabitWithDailyHabit] aggregation.
 * @param date The [LocalDate] to query.
 * @return A [HabitWithDay] containing the habit configuration and the relevant daily progress.
 */
fun getHabitDayFromADate(habits: HabitWithDailyHabit, date:LocalDate):HabitWithDay{

    val dailyHabit = habits.dailyHabits.find { it.date == date }
        ?: HabitDay(
            idHabit = habits.habit.id,
            date = date
        )

    return HabitWithDay(
        habit = habits.habit,
        day = dailyHabit
    )
}

/**
 * Calculates the number of days within a specific week where the habit goal was met.
 *
 * @param habits The [HabitWithDailyHabit] containing progress history.
 * @param startOfWeek The [LocalDate] marking the beginning of the week.
 * @return A [BigDecimal] count of days with goals reached.
 */
fun daysCompletedOnAWeek(habits: HabitWithDailyHabit, startOfWeek: LocalDate):BigDecimal{
    return habits.dailyHabits.filter {
        it.date in startOfWeek..startOfWeek.plusDays(6)
                && it.goalDone >= habits.habit.goal
    }.size.toBigDecimal()
}

/**
 * Aggregates the total goal progress achieved across an entire week.
 *
 * @param habits The [HabitWithDailyHabit] containing progress history.
 * @param startOfWeek The [LocalDate] marking the beginning of the week.
 * @return A [BigDecimal] sum of all progress recorded during the week.
 */
fun timesCompletedInAEntireWeek(habits: HabitWithDailyHabit, startOfWeek: LocalDate):BigDecimal{
    return habits.dailyHabits.filter {
        it.date in startOfWeek..startOfWeek.plusDays(6)
    }.sumOf {
        it.goalDone
    }
}

/**
 * Calculates the completion percentage as a ratio of completed progress over total goal.
 *
 * @param completed The total current progress achieved.
 * @param total The target goal value.
 * @return A [Float] representation of the percentage (e.g., 0.5f for 50%).
 */
fun calculatePercentage(completed: BigDecimal, total: BigDecimal): Float{
    return completed.divide(total).toFloat()
}