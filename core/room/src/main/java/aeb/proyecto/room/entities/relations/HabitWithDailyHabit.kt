package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.Habit
import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithDailyHabit(
    @Embedded val habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "idHabit"
    )
    val dailyHabits: MutableList<HabitDay> = mutableListOf()
)