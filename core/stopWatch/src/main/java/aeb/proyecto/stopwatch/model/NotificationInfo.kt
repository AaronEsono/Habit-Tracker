package aeb.proyecto.stopwatch.model

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.stopwatch.manager.StopwatchState

/**
 * Immutable state distribution model capturing synchronized runtime metrics
 * to feed and refresh the persistent Foreground Service notification layout.
 *
 * @property time The pre-formatted, human-readable temporal ticker string (e.g., "00:14:52").
 * @property currentState The active operational lifecycle posture of the tracking loop.
 * @property title The main contextual heading text displayed inside the system alert frame.
 * @property subText Optional structural relationship model containing the active habit profile
 * and its daily progress metrics, utilized to compute dynamic contextual descriptions.
 */
data class NotificationInfo(
    val time:String,
    val currentState: StopwatchState,
    val title:String,
    val subText:HabitWithDay?
)