package aeb.proyecto.stopwatch.manager

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.stopwatch.utils.longToHMS
import aeb.proyecto.stopwatch.utils.pad
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Singleton
class StopWatchStateManager @Inject constructor() {

    var startTime = 0L
    var timeElapsedBeforePause = 0L

    private var _elapsedTime: MutableStateFlow<Long> = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()

    private val _runningTimer: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val runningTimer: StateFlow<Boolean> = _runningTimer.asStateFlow()

    private val _notificationTitle = MutableStateFlow("Stopwatch")
    val notificationTitle: StateFlow<String> = _notificationTitle.asStateFlow()

    private var _typeTimer = MutableStateFlow<TypeTimer>(TypeTimer.STOPWATCH)
    val typeTimer: StateFlow<TypeTimer> = _typeTimer.asStateFlow()

    private var _currentState = MutableStateFlow(StopwatchState.Idle)
    val currentState: StateFlow<StopwatchState> = _currentState.asStateFlow()

    private var _habitLinked = MutableStateFlow<HabitWithDay?>(null)
    val habitLinked: StateFlow<HabitWithDay?> = _habitLinked.asStateFlow()

    val timerString: StateFlow<String> = elapsedTime
        .map { millis -> longToHMS(millis) }
        .stateIn(
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "00:00:00"
        )

    // --- Public setters ---

    fun setNotificationTitle(title: String) {
        _notificationTitle.value = title
    }

    fun setState(state: StopwatchState) {
        _currentState.value = state
    }

    fun setTimerType(type: TypeTimer) {
        _typeTimer.value = type
    }

    fun updateElapsedTime(elapsedTime: Long) {
        this._elapsedTime.value = maxOf(0L, elapsedTime)
    }

    fun setRunningTimer(running:Boolean){
        _runningTimer.value = running
    }

    fun setHabitLinked(habitDay: HabitWithDay?){
        _habitLinked.value = habitDay
    }

}

enum class StopwatchState {
    Idle,
    Stopped,
    InProgress,
    Finished,
}

sealed class TypeTimer {
    data object STOPWATCH : TypeTimer()
    data class TIMER(val time: Long) : TypeTimer()
    data class INTERVAL(
        val time: Long,
        val rest: Long,
        val interval: Int,
        val currentInterval: Int = 1,
        val state: IntervalState = IntervalState.Work
    ) : TypeTimer()
}

sealed class IntervalState{
    data object Rest: IntervalState()
    data object Work: IntervalState()
}


