package aeb.proyecto.stopwatch.overlay.utils

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun getTitle(typeTimer: TypeTimer, state: StopwatchState):String{

    val title = when(typeTimer){
        is TypeTimer.INTERVAL -> {
            when(state){
                StopwatchState.Idle -> stringResource(R.string.service_resume)
                StopwatchState.Stopped -> stringResource(R.string.service_paused, stringResource(R.string.service_interval))
                StopwatchState.InProgress -> {
                    when(typeTimer.state){
                        IntervalState.Rest -> {
                            stringResource(R.string.service_interval_rest)
                        }
                        IntervalState.Work -> {
                            if(typeTimer.currentInterval == typeTimer.interval){
                                stringResource(R.string.service_interval_last_round)
                            }else{
                                stringResource(R.string.service_interval_work, typeTimer.currentInterval.toString(), typeTimer.interval.toString())
                            }
                        }
                    }
                }
                StopwatchState.Finished ->  stringResource(R.string.service_finished, stringResource(R.string.service_interval))
            }
        }
        TypeTimer.STOPWATCH -> {
            when(state){
                StopwatchState.Idle -> stringResource(R.string.service_resume)
                StopwatchState.Stopped -> stringResource(R.string.service_paused, stringResource(R.string.service_stopwatch))
                StopwatchState.InProgress -> stringResource(R.string.service_stopwatch)
                StopwatchState.Finished -> stringResource(R.string.service_finished, stringResource(R.string.service_stopwatch))
            }
        }
        is TypeTimer.TIMER -> {
            when(state){
                StopwatchState.Idle -> stringResource(R.string.service_resume)
                StopwatchState.Stopped -> stringResource(R.string.service_paused, stringResource(R.string.service_timer))
                StopwatchState.InProgress -> stringResource(R.string.service_timer)
                StopwatchState.Finished -> stringResource(R.string.service_finished, stringResource(R.string.service_timer))
            }
        }
    }

    return title
}