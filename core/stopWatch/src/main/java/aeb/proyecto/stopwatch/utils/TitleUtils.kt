package aeb.proyecto.stopwatch.utils

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.TypeTimer
import android.content.Context

fun setIntervalTitle(stateManager: StopWatchStateManager, context: Context){
    val type =  stateManager.typeTimer.value as TypeTimer.INTERVAL

    when(type.state){
        IntervalState.Rest -> {
            stateManager.setNotificationTitle(context.getString(R.string.service_interval_rest))
        }
        IntervalState.Work -> {
            if(type.currentInterval == type.interval){
                stateManager.setNotificationTitle(context.getString(R.string.service_interval_last_round))
            }else{
                stateManager.setNotificationTitle(context.getString(
                    R.string.service_interval_work,
                    type.currentInterval.toString(), type.interval.toString()))
            }
        }
    }
}

fun getPausedTitle( stateManager: StopWatchStateManager, context: Context): String {
    val labelRes = when(stateManager.typeTimer.value) {
        is TypeTimer.INTERVAL -> R.string.service_interval
        TypeTimer.STOPWATCH -> R.string.service_stopwatch
        is TypeTimer.TIMER -> R.string.service_timer
    }
    return context.getString(R.string.service_paused, context.getString(labelRes))
}

fun prepareInitialTimerTitle( stateManager: StopWatchStateManager, context: Context) {
    when (val timer = stateManager.typeTimer.value) {
        is TypeTimer.INTERVAL -> {
            stateManager.updateElapsedTime(timer.time)
            setIntervalTitle(stateManager, context)
        }
        TypeTimer.STOPWATCH -> {
            stateManager.updateElapsedTime(0L)
            stateManager.setNotificationTitle(context.getString(R.string.service_stopwatch))
        }
        is TypeTimer.TIMER -> {
            stateManager.updateElapsedTime(timer.time)
            stateManager.setNotificationTitle(context.getString(R.string.service_timer))
        }
    }
}

fun getFinishedTitle(stateManager: StopWatchStateManager, context: Context){
    when(stateManager.typeTimer.value){
        is TypeTimer.INTERVAL -> {
            stateManager.setNotificationTitle(context.getString(R.string.service_finished,
                context.getString(R.string.service_interval)))
        }
        TypeTimer.STOPWATCH -> {
            stateManager.setNotificationTitle(context.getString(R.string.service_finished,
                context.getString(R.string.service_stopwatch)))
        }
        is TypeTimer.TIMER -> {
            stateManager.setNotificationTitle(context.getString(R.string.service_finished,
                context.getString(R.string.service_timer)))
        }
    }
}