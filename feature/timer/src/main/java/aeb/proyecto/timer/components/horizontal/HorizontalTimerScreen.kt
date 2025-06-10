package aeb.proyecto.timer.components.horizontal

import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.model.TimerServiceUIState
import androidx.compose.runtime.Composable

@Composable
fun HorizontalTimerScreen(
    timerUiState: TimerUiState,
    timerStopWatchUIState: TimerServiceUIState,
    onHourChange:(String) -> Unit = {},
    onMinuteChange:(String) -> Unit = {},
    onSecondChange: (String) -> Unit = {},
    onTypeChange: (Int) -> Unit = {},
    onStartService: () -> Unit = {},
    onSetChange: (Int) -> Unit = {},
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit = {_,_ ->},
    onButtonIntervalWorkChange: (Boolean) -> Unit = {},
    onButtonIntervalRestChange: (Boolean) -> Unit = {},
    onFinishButton: () -> Unit = {},
    onResumeButton: () -> Unit = {},
    onStopService: () -> Unit = {},
    onCancelButton: () -> Unit = {},
){

}