package aeb.proyecto.habittracker.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithNotification (
    @Embedded
    val habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val notifications: MutableList<Notification> = mutableListOf()
)