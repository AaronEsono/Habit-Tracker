package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.habit.Habit
import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithDailyHabit(
    @Embedded val habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "idHabit"
    )
    val dailyHabits: MutableList<DailyHabit> = mutableListOf()
)