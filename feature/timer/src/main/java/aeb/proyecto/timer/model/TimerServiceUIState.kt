package aeb.proyecto.timer.model

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer

/**
 * Represents the status of the background timer service as consumed by the UI.
 */
sealed class TimerServiceUIState {

    /** Indicates that no timer is currently active in the background. */
    data object NoTimer: TimerServiceUIState()

    /** Indicates an active timer session with its current progress and metadata. */
    data class TimerRunning(
        val elapsedTime: Long = 0L,
        val typeTimer: TypeTimer = TypeTimer.STOPWATCH,
        val currentState: StopwatchState = StopwatchState.Idle,
        val hourString:String = "00:00:00",
        val habitLinked:HabitWithDay? = null
    ): TimerServiceUIState()
}