package aeb.proyecto.timer.utils

import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import aeb.proyecto.timer.R
import aeb.proyecto.timer.model.TimerServiceUIState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.util.Locale

@Composable
fun getTitleActiveScreen(timerStopWatchUIState: TimerServiceUIState.TimerRunning):String{
    return when(timerStopWatchUIState.typeTimer){
        is TypeTimer.INTERVAL -> {
            when(timerStopWatchUIState.currentState){
                StopwatchState.Stopped -> stringResource(R.string.timer_title_interval_paused)
                StopwatchState.Finished -> stringResource(R.string.timer_title_interval_finish)
                else -> {
                    if(timerStopWatchUIState.typeTimer.state is IntervalState.Work){
                        if(timerStopWatchUIState.typeTimer.currentInterval == timerStopWatchUIState.typeTimer.interval){
                            stringResource(R.string.timer_title_interval_last)
                        }else{
                            stringResource(
                                R.string.timer_title_interval,
                                timerStopWatchUIState.typeTimer.currentInterval.toString(),
                                timerStopWatchUIState.typeTimer.interval.toString())
                        }
                    }else{
                        stringResource(R.string.timer_title_interval_rest)
                    }
                }
            }
        }
        TypeTimer.STOPWATCH -> {
            when(timerStopWatchUIState.currentState){
                StopwatchState.Stopped -> stringResource(R.string.timer_title_stopWatch_paused)
                StopwatchState.Finished -> stringResource(R.string.timer_title_stopWatch_finish)
                else ->  stringResource(R.string.timer_title_stopWatch)
            }
        }
        is TypeTimer.TIMER -> {
            when(timerStopWatchUIState.currentState){
                StopwatchState.Stopped -> stringResource(R.string.timer_title_timer_paused)
                StopwatchState.Finished -> stringResource(R.string.timer_title_timer_finish)
                else ->  stringResource(R.string.timer_title_timer)
            }
        }
    }
}

fun convertToHours(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return if(hours.toInt() == 0){
        String.format(Locale.US, "%02d:%02d", minutes, secs)
    }else{
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs)
    }
}