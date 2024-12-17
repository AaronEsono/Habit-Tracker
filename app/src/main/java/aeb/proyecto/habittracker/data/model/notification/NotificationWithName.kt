package aeb.proyecto.habittracker.data.model.notification

import aeb.proyecto.habittracker.data.entities.Notification
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class NotificationWithName(
    val notification: Notification,
    val name: String,
    val color: Color
)