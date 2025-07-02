package aeb.proyecto.stopwatch.model

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.stopwatch.manager.StopwatchState

data class NotificationInfo(
    val time:String,
    val currentState: StopwatchState,
    val title:String,
    val subText:HabitWithDay?
)