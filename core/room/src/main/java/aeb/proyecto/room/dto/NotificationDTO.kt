package aeb.proyecto.room.dto

import aeb.proyecto.room.converters.TypeNotificationConverter
import aeb.proyecto.room.entities.HabitNotification
import aeb.proyecto.room.model.classes.TypeNotification
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalTime

data class NotificationDTO(
    val time: String = "",
    val type: String = ""
)

fun HabitNotification.convertToDTO(): NotificationDTO {
    return NotificationDTO(
        time = time.toString(),
        type = TypeNotificationConverter().fromTypeNotification(type)
    )
}

fun NotificationDTO.convertToHabitNotification(): HabitNotification {
    return HabitNotification(
        time = LocalTime.parse(time),
        type = TypeNotificationConverter().toTypeNotification(type)
    )
}