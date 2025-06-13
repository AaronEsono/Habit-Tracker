package aeb.proyecto.timer.components.vertical.components.screens

import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.components.commom.button.AcceptButton
import aeb.proyecto.timer.components.commom.segmentedRow.SegmentedRow
import aeb.proyecto.timer.components.commom.typeTimer.intervalSegmented.IntervalSegmentedScreen
import aeb.proyecto.timer.components.commom.typeTimer.StopWatchSegmentedScreen
import aeb.proyecto.timer.components.commom.typeTimer.TimerSegmentedScreen
import aeb.proyecto.timer.model.SegmentedButtonOptions
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun VerticalChoseTimerScreen(
    timerUIState: TimerUiState.Success,
    onHourChange:(String) -> Unit,
    onMinuteChange:(String) -> Unit,
    onSecondChange: (String) -> Unit,
    onTypeChange: (Int) -> Unit,
    onStartService: () -> Unit,
    onSetChange: (Int) -> Unit,
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit,
    onButtonIntervalWorkChange: (Boolean) -> Unit,
    onButtonIntervalRestChange: (Boolean) -> Unit
){
    val segmentedOptions = remember { SegmentedButtonOptions.entries }

    Column {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            AnimatedContent(
                targetState = timerUIState.timerDataUIState.typeTimer,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith  ExitTransition.None
                }
            )
            { typeTimer ->

                when (typeTimer) {
                    SegmentedButtonOptions.StopWatch -> {
                        StopWatchSegmentedScreen()
                    }

                    SegmentedButtonOptions.Timer -> {
                        TimerSegmentedScreen(
                            hourSelectedState = timerUIState.timerDataUIState.hourSelected,
                            onHourChange = onHourChange,
                            onMinuteChange = onMinuteChange,
                            onSecondChange = onSecondChange
                        )
                    }

                    SegmentedButtonOptions.Interval -> {
                        IntervalSegmentedScreen(
                            hourSelectedState = timerUIState.timerDataUIState.hourSelected,
                            restSelectedState = timerUIState.timerDataUIState.restHour,
                            setInterval = timerUIState.timerDataUIState.sets,
                            onSetIntervalChange = onSetChange,
                            onClickButtonWorkTime = onButtonIntervalWorkChange,
                            onIntervalHourChange = onIntervalHourChange,
                            onClickButtonRestTime = onButtonIntervalRestChange
                        )
                    }
                }
            }
        }

        SegmentedRow(
            modifier = Modifier.fillMaxWidth().padding(top = spacing8),
            segmentedList = segmentedOptions,
            onClickOption = onTypeChange,
            typeTimer = timerUIState.timerDataUIState.typeTimer,
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacing32),
            horizontalArrangement = Arrangement.Center
        ){
            AcceptButton(
                timerUIState = timerUIState,
                onStartService = onStartService
            )
        }
    }
}