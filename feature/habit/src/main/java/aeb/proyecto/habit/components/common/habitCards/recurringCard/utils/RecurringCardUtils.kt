package aeb.proyecto.habit.components.common.habitCards.recurringCard.utils

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.model.classes.TypeHabit
import java.time.LocalDate
import kotlin.math.absoluteValue

fun isDayActiveForRecurringHabit(habit: Habit, selectedDate: LocalDate): Int{
    val type = habit.typeHabit as? TypeHabit.Recurring ?: return -1

    val daysBetween = (selectedDate.toEpochDay() - type.date.toEpochDay()).toInt().absoluteValue
    val reminder = daysBetween % type.interval

    return if (reminder == 0) 0 else type.interval - reminder
}