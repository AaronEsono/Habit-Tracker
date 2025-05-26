package aeb.proyecto.stopwatch.utils

import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import kotlin.text.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}

fun longToHMS(milliseconds: Long): String {
    val seconds = milliseconds / 1000
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return "${hours.toInt().pad()}:${minutes.toInt().pad()}:${secs.toInt().pad()}"
}

fun toLongMillis(value:String): Long {
    val time = value.split(":")
    return (time[0].toLong() * 3600 * 1000) + (time[1].toLong() * 60 * 1000) + time[2].toLong() * 1000
}

fun getCurrentState(name:String?): StopwatchState {
    return when(name) {
        StopwatchState.Idle.name -> StopwatchState.Idle
        StopwatchState.InProgress.name -> StopwatchState.InProgress
        StopwatchState.Stopped.name -> StopwatchState.Stopped
        StopwatchState.Finished.name -> StopwatchState.Finished
        else -> StopwatchState.Idle
    }
}

fun calculateCurrentIntervalState(
    workDuration: Long,     // Duración trabajo (ms)
    restDuration: Long,     // Duración descanso (ms)
    totalIntervals: Int,    // Nº bloques de trabajo
    totalElapsed: Long      // Tiempo total transcurrido (ms)
): Pair<TypeTimer.INTERVAL, Long> {

    var accumulatedTime = 0L

    for (block in 1..totalIntervals) {
        // BLOQUE DE TRABAJO
        val workEnd = accumulatedTime + workDuration
        if (totalElapsed < workEnd) {
            val remaining = workEnd - totalElapsed
            return TypeTimer.INTERVAL(
                time = workDuration,
                rest = restDuration,
                interval = totalIntervals,
                currentInterval = block,
                state = IntervalState.Work
            ) to remaining
        }
        accumulatedTime = workEnd

        // BLOQUE DE DESCANSO (si no es el último)
        if (block < totalIntervals) {
            val restEnd = accumulatedTime + restDuration
            if (totalElapsed < restEnd) {
                val remaining = restEnd - totalElapsed
                return TypeTimer.INTERVAL(
                    time = workDuration,
                    rest = restDuration,
                    interval = totalIntervals,
                    currentInterval = block,
                    state = IntervalState.Rest
                ) to remaining
            }
            accumulatedTime = restEnd
        }
    }

    // Todos los bloques terminados
    return TypeTimer.INTERVAL(
        time = workDuration,
        rest = restDuration,
        interval = totalIntervals,
        currentInterval = totalIntervals,
        state = IntervalState.Work // o podrías crear un estado "Finished"
    ) to 0L
}