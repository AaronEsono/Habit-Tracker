package aeb.proyecto.room.model

import aeb.proyecto.room.model.classes.TypeNotification
import java.time.LocalTime

/**
 * Lightweight presentation DTO (Data Transfer Object) optimized for notification rendering pipelines.
 *
 * This auxiliary model flattens relational fields across the habit and alert tables, capturing exclusively
 * the visual metadata and scheduling parameters required to populate user interface layouts, system tray dispatches,
 * or alarm management lists without dragging full entity models into memory.
 *
 * @property id Unique operational tracking identifier bound to the specific reminder rule.
 * @property time The target day-time timestamp [LocalTime] configuration when the system alert fires.
 * @property name The descriptive title or label of the associated habit, used directly as the display header.
 * @property color Hexadecimal integer color token utilized to dynamically theme notification icons or UI components.
 * @property typeNotification The structural recurring lifecycle rules profile governing this specific reminder.
 */
data class NotificationWithNameAndColor(
    val id:Long = 0,
    val time:LocalTime,
    val name: String,
    val color: Int,
    val typeNotification: TypeNotification
)