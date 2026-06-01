package aeb.proyecto.room.entities

import aeb.proyecto.room.converters.TypeNotificationConverter
import aeb.proyecto.room.model.classes.TypeNotification
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalTime

/**
 * Represents a scheduled reminder configuration framework tied to a specific parent habit tracking routine.
 *
 * This entity establishes relational bounds back to the upstream [Habit] registry, modeling the exact execution
 * timestamps and recurrence intervals used to dispatch local push notification system triggers.
 *
 * @property id Unique auto-generated tracking key identifier for this specific alert configuration.
 * @property habitId The relational foreign key referencing the upstream parent [Habit] identity token.
 * @property time The target day-time timestamp [LocalTime] when the notification alert should fire.
 * @property type The scheduling cadence rule profile configuration governing the system reminder dispatch intervals.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE, // Clean up associated alerts automatically if parent is deleted
            onUpdate = ForeignKey.CASCADE // Sync relational integrity state if primary keys shift
        )
    ]
)
data class HabitNotification(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    var habitId:Long = 0,
    val time:LocalTime = LocalTime.now(),
    @TypeConverters(TypeNotificationConverter::class)
    val type: TypeNotification = TypeNotification.Recurring(1)
)
