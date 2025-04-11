package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.HabitNotification
import androidx.room.Embedded
import androidx.room.Relation

data class EntireHabit(
    @Embedded val habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "idHabit"
    )
    val dailyHabits: MutableList<HabitDay> = mutableListOf(),
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    var notifications: MutableList<HabitNotification> = mutableListOf()
)