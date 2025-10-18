package aeb.proyecto.habit.components.common.habitCards.recurringCard.utils

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.model.classes.TypeHabit
import java.time.LocalDate
import kotlin.math.absoluteValue

fun isDayActiveForRecurringHabit(habit: Habit, selectedDate: LocalDate): Boolean{
    val type = habit.typeHabit

    return if(type is TypeHabit.Recurring){
        val startDate = type.date
        val interval = type.interval
        val intervalOfDays = (selectedDate.toEpochDay() - startDate.toEpochDay()).toInt().absoluteValue

        intervalOfDays % interval == 0
    }else{
        false
    }
}