package aeb.proyecto.room.model

data class NotificationWithNameAndColor(
    val id:Long = 0,
    val hour:Int = 0,
    val minute:Int = 0,
    val name: String,
    val color: Int
)