package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.TimeEntry
import androidx.room.Embedded
import androidx.room.Relation

data class TimeEntryWithHabit(
    @Embedded(prefix = "timeEntry_")
    val timeEntry: TimeEntry = TimeEntry(),
    @Embedded(prefix = "habit_")
    val habit: Habit? = null
)

