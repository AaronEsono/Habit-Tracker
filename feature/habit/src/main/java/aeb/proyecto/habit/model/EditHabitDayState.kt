package aeb.proyecto.habit.model

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay

data class EditHabitDayState(
    val showEditHabitDayBT:Boolean = false,
    val habit:Habit = Habit(),
    val habitDay: HabitDay = HabitDay(),
)