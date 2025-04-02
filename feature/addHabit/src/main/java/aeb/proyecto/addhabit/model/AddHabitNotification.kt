package aeb.proyecto.addhabit.model

import aeb.proyecto.room.model.classes.TypeNotification
import java.time.LocalTime
import java.util.UUID

data class AddHabitNotification(
    val id:String = UUID.randomUUID().toString(),
    val time:LocalTime = LocalTime.now(),
    val type: TypeNotification = TypeNotification.Daily()
)

val DEFAULT_TIME = AddHabitNotification(
    id = "-1",
    time = LocalTime.now(),
    type = TypeNotification.Daily()
)