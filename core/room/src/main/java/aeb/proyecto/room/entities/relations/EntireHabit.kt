package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.habit.Habit
import aeb.proyecto.room.entities.Notification
import androidx.room.Embedded
import androidx.room.Relation

data class EntireHabit(
    @Embedded val habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "idHabit"
    )
    val dailyHabits: MutableList<DailyHabit> = mutableListOf(),
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    var notifications: MutableList<Notification> = mutableListOf()
)