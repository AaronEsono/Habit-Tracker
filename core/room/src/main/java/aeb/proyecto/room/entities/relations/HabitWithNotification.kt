package aeb.proyecto.room.entities.relations

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitNotification
import androidx.room.Embedded
import androidx.room.Relation

/**
 * Optimized relational data aggregate model decoupling habit metadata from historical performance metrics.
 *
 * This specific POJO is engineered strictly for alert-scheduling operations, push notification sync tasks,
 * and reminder management layouts. By purposefully excluding [HabitDay] records, it ensures lightning-fast
 * database queries when initializing local system alarms.
 *
 * @property habit The core structural metadata and configuration foundation of the behavior.
 * @property notifications The collection of scheduled dynamic system reminder alert triggers linked to this behavior.
 */
data class HabitWithNotification (
    @Embedded
    var habit: Habit = Habit(),
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    var notifications: MutableList<HabitNotification> = mutableListOf()
)