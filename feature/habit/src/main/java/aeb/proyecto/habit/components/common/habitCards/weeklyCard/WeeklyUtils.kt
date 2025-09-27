package aeb.proyecto.habit.components.common.habitCards.weeklyCard

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
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