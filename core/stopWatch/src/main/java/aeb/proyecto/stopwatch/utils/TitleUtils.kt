package aeb.proyecto.stopwatch.utils

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.TypeTimer
import android.content.Context

/**
 * Computes and injects the precise contextual heading for an active interval progression session.
 * Evaluates the underlying [IntervalState] to switch between rest periods, active work sessions,
 * and dynamic "last round" micro-copy highlights.
 *
 * @param stateManager The centralized reactive single source of truth context provider.
 * @param context The framework-level context required to resolve localized string resources.
 */
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

/**
 * Resolves a tailored, human-readable paused status header string based on the active tracking profile modality.
 * Combines structural state identifiers into a single descriptive string token (e.g., "Paused: Timer").
 *
 * @param stateManager The centralized reactive single source of truth context provider.
 * @param context The framework-level context required to resolve localized string resources.
 * @return A fully resolved, humanized localized paused state descriptive [String].
 */
fun getPausedTitle( stateManager: StopWatchStateManager, context: Context): String {
    val labelRes = when(stateManager.typeTimer.value) {
        is TypeTimer.INTERVAL -> R.string.service_interval
        TypeTimer.STOPWATCH -> R.string.service_stopwatch
        is TypeTimer.TIMER -> R.string.service_timer
    }
    return context.getString(R.string.service_paused, context.getString(labelRes))
}

/**
 * Sets up and populates baseline structural properties and naming conventions within the state broker
 * prior to launching background execution threads.
 * Establishes absolute countdown ceilings or baseline zero indicators to prevent interface state flickering.
 *
 * @param stateManager The centralized reactive single source of truth context provider.
 * @param context The framework-level context required to resolve localized string resources.
 */
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