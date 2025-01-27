package aeb.proyecto.room.model

import aeb.proyecto.room.entities.Notification
import androidx.compose.ui.graphics.Color

data class NotificationWithName(
    val notification: Notification,
    val name: String,
    val color: Color
)