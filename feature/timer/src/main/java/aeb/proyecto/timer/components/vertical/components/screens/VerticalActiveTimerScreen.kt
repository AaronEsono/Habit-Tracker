package aeb.proyecto.timer.components.vertical.components.screens

import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.common.button.CancelButton
import aeb.proyecto.timer.components.common.button.FinishButton
import aeb.proyecto.timer.components.common.button.ResumeButton
import aeb.proyecto.timer.components.common.button.StopButton
import aeb.proyecto.timer.components.common.typeActiveTimer.ActiveIntervalScreen
import aeb.proyecto.timer.components.common.typeActiveTimer.ActiveStopwatchScreen
import aeb.proyecto.timer.components.common.typeActiveTimer.ActiveTimerScreen
import aeb.proyecto.timer.model.TimerServiceUIState
import aeb.proyecto.timer.utils.getTitleActiveScreen
import aeb.proyecto.ui.date.utils.getTextToday
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

/**
 * Vertical layout for the active timer session.
 * Organizes the screen into a vertical stack, placing the title and timer
 * information at the top and interaction controls below.
 * Ideal for portrait orientation and mobile usage.
 *
 * @param timerStopWatchUIState The current service state of the timer.
 * @param onStopService Callback to pause/stop the timer service.
 * @param onCancelButton Callback to abort the current session.
 * @param onResumeButton Callback to resume a paused timer.
 * @param onFinishButton Callback to mark the session as manually completed.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun VerticalActiveTimerScreen(
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
                    modifier = Modifier.padding(top = spacing32),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            timerStopWatchUIState.habitLinked?.let { habitLinked ->
                LabelLargeText(
                    stringResource(R.string.timer_title_habit,habitLinked.habit.name,
                        getTextToday(habitLinked.day.date)
                    ),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = spacing8)
                        .testTag("timer_habit_linked_screen"),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.padding(bottom = spacing24))

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
                    StopwatchState.Stopped, StopwatchState.InProgress -> {
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