package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.HabitNotification
import androidx.room.Embedded
import androidx.room.Relation

/**
 * Relational data aggregate model representing a holistic structural view of a habit ecosystem.
 *
 * This composite POJO does not map directly to a database table itself; instead, it orchestrates Room's
 * compile-time query generation pipeline to fetch a parent [Habit] registry embedded alongside all
 * its associated historical progress track logs ([HabitDay]) and alert rules ([HabitNotification])
 * in a single atomic transaction.
 *
 * @property habit The core structural metadata and configuration foundation of the behavior.
 * @property dailyHabits The full historical collection of daily quantitative progress entries completed for this routine.
 * @property notifications The complete list of scheduled system push notification alarms linked to this behavior.
 */
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