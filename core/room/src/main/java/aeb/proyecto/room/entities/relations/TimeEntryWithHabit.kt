package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.TimeEntry
import androidx.room.Embedded
import androidx.room.Relation

/**
 * Flat relational database projection representing a stopwatch session paired with its optional parent habit.
 *
 * Unlike standard collections driven by relations, this model flattens a lookup structure where a [TimeEntry]
 * references an optional [Habit]. It leverages column prefixing to completely prevent compilation-level column
 * name collisions during advanced database JOIN operations.
 *
 * @property timeEntry The concrete time-tracking snapshot containing raw focus, rest, and configuration data.
 * @property habit The optional parent [Habit] configuration profile linked to this specific time session,
 * or null if the focus block was executed as a standalone/free-form timer session.
 */
data class TimeEntryWithHabit(
    @Embedded(prefix = "timeEntry_")
    val timeEntry: TimeEntry = TimeEntry(),
    @Embedded(prefix = "habit_")
    val habit: Habit? = null
)

