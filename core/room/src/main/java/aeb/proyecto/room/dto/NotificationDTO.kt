package aeb.proyecto.room.dto

import aeb.proyecto.room.converters.TypeNotificationConverter
import aeb.proyecto.room.entities.HabitNotification
import aeb.proyecto.room.model.classes.TypeNotification
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalTime

/**
 * Data transfer representation of a scheduled habit reminder alert configuration.
 *
 * This DTO flattens framework-specific clock timestamps and rule-driven sealed class hierarchies
 * into localized, scalar [String] values, formatting the structural state for safe cloud synchronization
 * or network transport operations.
 *
 * @property time Standard ISO-8601 clock string representation (e.g., "HH:mm:ss") capturing when the alert fires.
 * @property type Flat serialized string discriminator mapping to the explicit [TypeNotification] behavioral cadence rules.
 */
data class NotificationDTO(
    val time: String = "",
    val type: String = ""
)

/**
 * Transforms a local persistent [HabitNotification] database entity into a transport-safe [NotificationDTO].
 *
 * This mapper delegates structural state transformation to dedicated type converters to extract clean,
 * scalar transport strings.
 *
 * @return A fully serialized [NotificationDTO] instance.
 */
fun HabitNotification.convertToDTO(): NotificationDTO {
    return NotificationDTO(
        time = time.toString(),
        type = TypeNotificationConverter().fromTypeNotification(type)
    )
}

/**
 * Reconstructs a persistent [HabitNotification] entity from a transport [NotificationDTO] snapshot.
 *
 * This operation reverses the transport serialization, formatting plain text markers back into
 * type-safe [LocalTime] clocks and validated runtime polymorphism alerts.
 *
 * @return A fully populated [HabitNotification] entity ready for database caching and local operations.
 */
fun NotificationDTO.convertToHabitNotification(): HabitNotification {
    return HabitNotification(
        time = LocalTime.parse(time),
        type = TypeNotificationConverter().toTypeNotification(type)
    )
}