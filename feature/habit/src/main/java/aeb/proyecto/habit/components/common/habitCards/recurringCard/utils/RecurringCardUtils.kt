package aeb.proyecto.habit.components.common.habitCards.recurringCard.utils

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.model.classes.TypeHabit
import java.time.LocalDate
import kotlin.math.absoluteValue

/**
 * Determines the activity status of a recurring habit for a specific date based
 * on its interval configuration.
 *
 * This function calculates the temporal distance between the habit's starting
 * date and the target [LocalDate]. It returns 0 if the date is an "active" day
 * for the habit (i.e., it matches the interval), or the number of days
 * remaining until the next occurrence.
 *
 * @param habit The [Habit] object, expected to have a [TypeHabit.Recurring] configuration.
 * @param selectedDate The [LocalDate] to evaluate against the habit's recurrence cycle.
 * @return An [Int] representing:
 * - `0` if the habit is active/due on the [selectedDate].
 * - A positive integer representing days until the next occurrence.
 * - `-1` if the habit is not of type [TypeHabit.Recurring].
 */
fun isDayActiveForRecurringHabit(habit: Habit, selectedDate: LocalDate): Int{
    val type = habit.typeHabit as? TypeHabit.Recurring ?: return -1

    val daysBetween = (selectedDate.toEpochDay() - type.date.toEpochDay()).toInt().absoluteValue
    val reminder = daysBetween % type.interval

    return if (reminder == 0) 0 else type.interval - reminder
}