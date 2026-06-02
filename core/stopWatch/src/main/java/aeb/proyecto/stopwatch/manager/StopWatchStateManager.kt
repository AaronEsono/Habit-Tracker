package aeb.proyecto.stopwatch.manager

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.stopwatch.utils.longToHMS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The single reactive Source of Truth governing the entire chronometric tracking system.
 *
 * This singleton bridges the state boundary between background calculation engines ([StopWatchService])
 * and presentation layers (Jetpack Compose ViewModels/UI Components). By abstracting variables
 * into reactive primitives ([StateFlow]), it ensures unified synchronization across all system lifecycles.
 */
@Singleton
class StopWatchStateManager @Inject constructor() {

    /** The absolute system uptime timestamp (Unix Epoch) marking when the current chronometer started ticking. */
    var startTime = 0L

    /** Accumulated time slice metrics cached from historical runs prior to triggering a session pause directive. */
    var timeElapsedBeforePause = 0L

    private var _elapsedTime: MutableStateFlow<Long> = MutableStateFlow(0L)
    /** Emits the real-time calculated session tracking duration expressed in raw milliseconds. */
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()

    private val _runningTimer: MutableStateFlow<Boolean> = MutableStateFlow(false)
    /** Emits a boolean flag tracking whether the ticker loop is physically incrementing timestamps. */
    val runningTimer: StateFlow<Boolean> = _runningTimer.asStateFlow()

    private val _notificationTitle = MutableStateFlow("Stopwatch")
    /** Emits the dynamic heading text assigned to active system alerts and overlay frames. */
    val notificationTitle: StateFlow<String> = _notificationTitle.asStateFlow()

    private var _typeTimer = MutableStateFlow<TypeTimer>(TypeTimer.STOPWATCH)
    /** Emits the current structural behavior rule tracking mode (Stopwatch, Countdown, or Interval). */
    val typeTimer: StateFlow<TypeTimer> = _typeTimer.asStateFlow()

    private var _currentState = MutableStateFlow(StopwatchState.Idle)
    /** Emits the macroscopic execution state lifecycle posture of the core tracking system. */
    val currentState: StateFlow<StopwatchState> = _currentState.asStateFlow()

    private var _habitLinked = MutableStateFlow<HabitWithDay?>(null)
    /** Emits relational metadata matching the active tracking session to a specific database habit log sheet. */
    val habitLinked: StateFlow<HabitWithDay?> = _habitLinked.asStateFlow()

    /**
     * An upstream projected, ready-to-render string representation of the active timer pipeline (e.g., "00:04:23").
     * Uses a 5-second subscription downstream buffer to stop mapping overhead when presentation observers are inactive.
     */
    val timerString: StateFlow<String> = elapsedTime
        .map { millis -> longToHMS(millis) }
        .stateIn(
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "00:00:00"
        )

    // ============================================================================
    // State Modification API (Setters)
    // ============================================================================

    /** Updates the descriptive heading used by background alert indicators. */
    fun setNotificationTitle(title: String) {
        _notificationTitle.value = title
    }

    /** Transitions the overarching macroscopic tracking state machine to a new lifecycle posture. */
    fun setState(state: StopwatchState) {
        _currentState.value = state
    }

    /** Mutates the operational timing profile modality. */
    fun setTimerType(type: TypeTimer) {
        _typeTimer.value = type
    }

    /**
     * Updates the current session tracking duration millisecond metrics.
     * Enforces an absolute floor limit boundary of 0ms against negative values.
     */
    fun updateElapsedTime(elapsedTime: Long) {
        this._elapsedTime.value = maxOf(0L, elapsedTime)
    }

    /** Adjusts the structural ticking flag state. */
    fun setRunningTimer(running:Boolean){
        _runningTimer.value = running
    }

    /** Binds or unbinds a persistent database habit record context with the active execution matrix. */
    fun setHabitLinked(habitDay: HabitWithDay?){
        _habitLinked.value = habitDay
    }

}

/**
 * Specifies the global operational execution posture of the background time tracking engine.
 * Governs UI rendering logic, notification action layouts, and service lifecycle adjustments.
 */
enum class StopwatchState {

    /** The tracking engine is inactive, uninitialized, or completely reset. */
    Idle,

    /** Execution ticks are temporarily suspended/paused, holding current metrics in memory. */
    Stopped,

    /** The tracking loop is actively running, processing intervals, or incrementing ticks in a background thread. */
    InProgress,

    /** A countdown or interval-repetition matrix has reached its nominal target completion. */
    Finished,
}

/**
 * Represents the distinct behavioral tracking modalities supported by the timing architecture.
 *
 * Enforces compile-time structural polymorphic constraints, ensuring that parameters
 * unique to high-complexity interval training loops do not pollute baseline chronometers.
 */
sealed class TypeTimer {

    /** Represents a continuous, linear, upward-counting stopwatch tracking structure. */
    data object STOPWATCH : TypeTimer()

    /**
     * Represents a fixed-duration, downward-counting regressive countdown timer structure.
     *
     * @property time The total countdown duration limit threshold mapped in milliseconds.
     */
    data class TIMER(val time: Long) : TypeTimer()

    /**
     * Represents a complex, multi-segment iterative fitness/productivity interval routine layout structure.
     *
     * @property time Active exertion segment duration limit threshold values in milliseconds.
     * @property rest Passive recovery segment duration limit threshold values in milliseconds.
     * @property interval The aggregate total target execution cycles designated to complete the routine grid.
     * @property currentInterval The current progressive execution iteration loop pointer index (starts at 1).
     * @property state The active localized state sub-boundary pointing to either work or rest phases.
     */
    data class INTERVAL(
        val time: Long,
        val rest: Long,
        val interval: Int,
        val currentInterval: Int = 1,
        val state: IntervalState = IntervalState.Work
    ) : TypeTimer()
}

/**
 * Isolates the precise chronological tracking sub-boundaries operating within
 * an active multi-segment [TypeTimer.INTERVAL] loop execution.
 */
sealed class IntervalState{

    /** Specifies that the loop is executing its passive cooldown or recovery phase. */
    data object Rest: IntervalState()

    /** Specifies that the loop is executing its primary active exertion target phase. */
    data object Work: IntervalState()
}


