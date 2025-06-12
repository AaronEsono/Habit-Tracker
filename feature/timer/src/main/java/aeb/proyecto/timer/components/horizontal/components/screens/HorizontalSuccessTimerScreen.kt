package aeb.proyecto.timer.components.horizontal.components.screens

import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.model.TimerServiceUIState
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable

@Composable
fun HorizontalSuccessTimerScreen(
    timerUIState: TimerUiState.Success,
    timerStopWatchUIState: TimerServiceUIState,
    onHourChange:(String) -> Unit,
    onMinuteChange:(String) -> Unit,
    onSecondChange: (String) -> Unit,
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit,
    onButtonIntervalWorkChange: (Boolean) -> Unit,
    onButtonIntervalRestChange: (Boolean) -> Unit,
    onSetChange: (Int) -> Unit,
    onTypeChange: (Int) -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    onResumeButton: () -> Unit,
    onCancelButton: () -> Unit,
    onFinishButton: () -> Unit,
){
    AnimatedContent(
        targetState = timerStopWatchUIState,
        contentKey = { it::class }
    ) { timerStopWatchUIStateAnim ->

        when(timerStopWatchUIStateAnim){
            TimerServiceUIState.NoTimer -> {
                HorizontalChoseTimerScreen(
                    timerUIState = timerUIState,
                    onHourChange = onHourChange,
                    onMinuteChange = onMinuteChange,
                    onSecondChange = onSecondChange,
                    onTypeChange = onTypeChange,
                    onStartService = onStartService,
                    onSetChange = onSetChange,
                    onIntervalHourChange = onIntervalHourChange,
                    onButtonIntervalWorkChange = onButtonIntervalWorkChange,
                    onButtonIntervalRestChange = onButtonIntervalRestChange
                )
            }
            is TimerServiceUIState.TimerRunning -> {
                HorizontalActiveTimerScreen(
                    timerStopWatchUIState = timerStopWatchUIStateAnim,
                    onStopService = onStopService,
                    onCancelButton = onCancelButton,
                    onResumeButton = onResumeButton,
                    onFinishButton = onFinishButton
                )
            }
        }
    }

}