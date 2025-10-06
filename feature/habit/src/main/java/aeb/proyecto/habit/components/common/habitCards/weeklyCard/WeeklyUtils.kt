package aeb.proyecto.habit.components.common.habitCards.weeklyCard

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import java.math.BigDecimal
import java.time.LocalDate

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

fun daysCompletedOnAWeek(habits: HabitWithDailyHabit, startOfWeek: LocalDate):BigDecimal{
    return habits.dailyHabits.filter {
        it.date in startOfWeek..startOfWeek.plusDays(6)
                && it.goalDone >= habits.habit.goal
    }.size.toBigDecimal()
}

fun timesCompletedInAEntireWeek(habits: HabitWithDailyHabit, startOfWeek: LocalDate):BigDecimal{
    return habits.dailyHabits.filter {
        it.date in startOfWeek..startOfWeek.plusDays(6)
    }.sumOf {
        it.goalDone
    }
}

fun calculatePercentage(completed: BigDecimal, total: BigDecimal): Float{
    return completed.divide(total).toFloat()
}