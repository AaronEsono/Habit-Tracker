package aeb.proyecto.timer.components.screens.timer

import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import aeb.proyecto.timer.components.button.CancelButton
import aeb.proyecto.timer.components.button.FinishButton
import aeb.proyecto.timer.components.button.ResumeButton
import aeb.proyecto.timer.components.button.StopButton
import aeb.proyecto.timer.components.screens.timer.typeTimer.ActiveIntervalScreen
import aeb.proyecto.timer.components.screens.timer.typeTimer.ActiveStopwatchScreen
import aeb.proyecto.timer.components.screens.timer.typeTimer.ActiveTimerScreen
import aeb.proyecto.timer.components.screens.timer.utils.getTitleActiveScreen
import aeb.proyecto.timer.model.TimerServiceUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun TimerActiveScreen(
    timerStopWatchUIState: TimerServiceUIState.TimerRunning,
    onStopService: () -> Unit,
    onCancelButton: () -> Unit,
    onResumeButton: () -> Unit,
    onFinishButton: () -> Unit
){

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        AnimatedContent(
            targetState = getTitleActiveScreen(timerStopWatchUIState)
        ) {titleAnim ->

            LabelLargeText(
                titleAnim,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = spacing32).basicMarquee(),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )

        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(vertical = spacing12),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            when(timerStopWatchUIState.typeTimer){
                is TypeTimer.INTERVAL -> ActiveIntervalScreen(timerStopWatchUIState, timerStopWatchUIState.typeTimer)
                TypeTimer.STOPWATCH -> ActiveStopwatchScreen(timerStopWatchUIState.hourString)
                is TypeTimer.TIMER -> ActiveTimerScreen(timerStopWatchUIState, timerStopWatchUIState.typeTimer)
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth().padding(top = spacing8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            when (timerStopWatchUIState.currentState) {
                StopwatchState.Idle -> Unit
                StopwatchState.Stopped,StopwatchState.InProgress -> {
                    AnimatedContent(
                        targetState = timerStopWatchUIState.currentState
                    ) { targetState ->
                        if(targetState == StopwatchState.Stopped){
                            ResumeButton{
                                onResumeButton()
                            }
                        }
                        else {
                            StopButton{
                                onStopService()
                            }
                        }
                    }

                    CancelButton(
                        modifier = Modifier.padding(start = spacing12)
                    ) {
                        onCancelButton()
                    }
                }

                StopwatchState.Finished -> {
                    FinishButton{
                        onFinishButton()
                    }
                }
            }
        }
    }
}