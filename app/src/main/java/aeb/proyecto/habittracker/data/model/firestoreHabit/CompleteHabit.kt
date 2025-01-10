package aeb.proyecto.habittracker.data.model.firestoreHabit

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.Notification
import androidx.room.Embedded
import androidx.room.Relation

data class CompleteHabit(
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