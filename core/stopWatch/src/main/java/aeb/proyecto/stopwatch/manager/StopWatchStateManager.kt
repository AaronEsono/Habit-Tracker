package aeb.proyecto.stopwatch.manager

import aeb.proyecto.stopwatch.service.StopwatchState
import aeb.proyecto.stopwatch.utils.pad
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Singleton
class StopWatchStateManager @Inject constructor() {
    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set
    var currentState = mutableStateOf(StopwatchState.Idle)
        private set

    var duration: Duration = Duration.ZERO

    fun tick() {
        duration = duration.plus(1.seconds)
        updateTimeUnits()
    }

    fun reset() {
        duration = Duration.ZERO
        updateTimeUnits()
        currentState.value = StopwatchState.Idle
    }

    fun setState(state: StopwatchState) {
        currentState.value = state
    }

    private fun updateTimeUnits() {
        duration.toComponents { h, m, s, _ ->
            hours.value = h.toInt().pad()
            minutes.value = m.pad()
            seconds.value = s.pad()
        }
    }
}
