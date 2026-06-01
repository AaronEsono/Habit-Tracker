package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import androidx.room.Embedded
import androidx.room.Relation

/**
 * Highly targeted relational data aggregate model representing a single-day snapshot of a habit.
 *
 * This POJO is designed for real-time tracking dashboards (like the daily checklist view), isolated
 * from the overhead of full historical logs. It couples a parent [Habit] configuration with a unique,
 * contextual [HabitDay] transaction record matching a specific calendar query boundary.
 *
 * @property habit The core structural metadata and configuration foundation of the behavior.
 * @property day The single, explicit daily progress entry matching the target query execution state.
 */
data class HabitWithDay(
    @Embedded val habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "idHabit"
    )
    val day: HabitDay = HabitDay()
)