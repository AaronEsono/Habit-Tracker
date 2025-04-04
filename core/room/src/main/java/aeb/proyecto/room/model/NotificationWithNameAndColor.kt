package aeb.proyecto.room.model

import aeb.proyecto.room.converters.TypeNotificationConverter
import aeb.proyecto.room.model.classes.TypeNotification
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.TypeConverter
import java.time.LocalTime

data class NotificationWithNameAndColor(
    val id:Long = 0,
    val time:LocalTime,
    val name: String,
    val color: Int,
    val typeNotification: TypeNotification
)