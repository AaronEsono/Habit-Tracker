package aeb.proyecto.addhabit.model

import aeb.proyecto.room.model.classes.TypeNotification
import java.time.LocalTime
import java.util.UUID

/**
 * Represents an isolated configuration profile metadata matrix for a specific habit alarm trigger.
 *
 * @property id The unique cryptographic identifier string assigned to prevent tracking key overlaps, defaulting to a random UUID.
 * @property time The localized [LocalTime] boundary instance chosen by the user to execute the system notification.
 * @property type The structural classification [TypeNotification] rule governing recurrence behaviors (e.g., Daily, Recurring).
 */
data class AddHabitNotification(
    val id:String = UUID.randomUUID().toString(),
    val time:LocalTime = LocalTime.now(),
    val type: TypeNotification = TypeNotification.Daily()
)

/**
 * Contextual sentinel initialization token used to mark completely fresh, uncommitted,
 * or blank reminder overlay configurations within interactive time-picker dialogue states.
 */
val DEFAULT_TIME = AddHabitNotification(
    id = "-1",
    time = LocalTime.now(),
    type = TypeNotification.Daily()
)