package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.habit.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.entities.notification.HabitNotification
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