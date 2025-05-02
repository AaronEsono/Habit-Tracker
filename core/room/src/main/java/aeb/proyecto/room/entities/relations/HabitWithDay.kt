package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithDay(
    @Embedded val habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "idHabit"
    )
    val day: HabitDay = HabitDay()
)