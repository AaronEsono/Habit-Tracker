package aeb.proyecto.stopwatch.overlay.utils

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Resolves high-fidelity, contextual string titles by cross-evaluating the active instrumentation
 * rules against macroscopic operational states.
 * Fully dissects complex internal interval sub-states to dynamically provision localized typography
 * layout text resources (e.g., set counters vs terminal segment alerts) during runtime.
 *
 * @param typeTimer The active structural configuration profile (Stopwatch, Timer, or Interval).
 * @param state The macroscopic runtime execution posture of the tracking engine.
 * @return A fully compiled, localized string title ready to be drawn onto the overlay typography nodes.
 */
@Composable
fun getTitle(typeTimer: TypeTimer, state: StopwatchState):String{

    // Evaluate structural matrix mappings across dual hierarchical state tracks
    val title = when(typeTimer){
        is TypeTimer.INTERVAL -> {
            when(state){
                StopwatchState.Idle -> stringResource(R.string.service_resume)
                StopwatchState.Stopped -> stringResource(R.string.service_paused, stringResource(R.string.service_interval))
                StopwatchState.InProgress -> {
                    // Dive deep into granular interval segment conditions
                    when(typeTimer.state){
                        IntervalState.Rest -> {
                            stringResource(R.string.service_interval_rest)
                        }
                        IntervalState.Work -> {
                            // Conditional breakthrough separating ordinary rounds from the terminal set
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