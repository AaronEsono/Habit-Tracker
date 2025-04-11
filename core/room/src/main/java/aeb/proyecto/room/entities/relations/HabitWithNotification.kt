package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitNotification
import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithNotification (
    @Embedded
    var habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    var notifications: MutableList<HabitNotification> = mutableListOf()
)