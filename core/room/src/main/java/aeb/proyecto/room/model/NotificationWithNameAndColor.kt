package aeb.proyecto.room.model

import aeb.proyecto.room.entities.Notification

data class NotificationWithNameAndColor(
    val notification: Notification,
    val name: String,
    val colorInt: Int
)