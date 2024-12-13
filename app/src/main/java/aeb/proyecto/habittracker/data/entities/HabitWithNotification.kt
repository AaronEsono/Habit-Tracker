package aeb.proyecto.habittracker.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithNotification (
    @Embedded
    var habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    var notifications: MutableList<Notification> = mutableListOf()
)