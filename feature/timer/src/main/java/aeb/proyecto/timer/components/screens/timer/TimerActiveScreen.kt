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
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//Arreglar pete de intervalos
@Composable
fun TimerActiveScreen(
    timerStopWatchUIState: TimerServiceUIState.TimerRunning,
    onStopService: () -> Unit,
    onCancelButton: () -> Unit,
    onResumeButton: () -> Unit,
    onFinishButton: () -> Unit
){

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val halfScreenHeight = maxHeight / 2

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            AnimatedContent(
                targetState = getTitleActiveScreen(timerStopWatchUIState)
            ) {titleAnim ->

                LabelLargeText(
                    titleAnim,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = spacing32, bottom = spacing24),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(halfScreenHeight),
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
                modifier = Modifier.fillMaxWidth().padding(top = spacing24),
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

}