package aeb.proyecto.habittracker.data.entities

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