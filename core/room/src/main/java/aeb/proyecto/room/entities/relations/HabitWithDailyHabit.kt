package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.Habit
import androidx.room.Embedded
import androidx.room.Relation

/**
 * Optimized relational data aggregate model representing a streamlined view of a habit and its progress history.
 *
 * Unlike [EntireHabit], this specific POJO purposefully excludes notification alarm structures, making it
 * the ideal lightweight data pipeline for driving analytical dashboards, calendar grids, and historical progress layouts
 * without introducing unnecessary query overhead.
 *
 * @property habit The core structural metadata and configuration foundation of the behavior.
 * @property dailyHabits The full historical collection of daily quantitative progress entries completed for this routine.
 */
data class HabitWithDailyHabit(
    @Embedded val habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "idHabit"
    )
    val dailyHabits: MutableList<HabitDay> = mutableListOf()
)