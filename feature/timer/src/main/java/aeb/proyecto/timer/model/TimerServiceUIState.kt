package aeb.proyecto.timer.model

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer

sealed class TimerServiceUIState {
    data object NoTimer: TimerServiceUIState()
    data class TimerRunning(
        val elapsedTime: Long = 0L,
        val typeTimer: TypeTimer = TypeTimer.STOPWATCH,
        val currentState: StopwatchState = StopwatchState.Idle,
        val hourString:String = "00:00:00",
        val habitLinked:HabitWithDay? = null
    ): TimerServiceUIState()
}