package aeb.proyecto.timer.components.vertical.components.screens

import aeb.proyecto.timer.R
import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.components.commom.typeTimer.IntervalSegmentedScreen
import aeb.proyecto.timer.components.commom.typeTimer.StopWatchSegmentedScreen
import aeb.proyecto.timer.components.commom.typeTimer.TimerSegmentedScreen
import aeb.proyecto.timer.model.SegmentedButtonOptions
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.TitleMediumText
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val haptic = LocalHapticFeedback.current
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

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = spacing8),
            horizontalArrangement = Arrangement.Center
        ) {
            SingleChoiceSegmentedButtonRow {
                segmentedOptions.forEachIndexed { index, typeButton ->
                    val shape = when (index) {
                        0 -> RoundedCornerShape(
                            topStart = spacing8,
                            bottomStart = spacing8,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp
                        )

                        segmentedOptions.lastIndex -> RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = spacing8,
                            bottomEnd = spacing8
                        )

                        else -> RoundedCornerShape(0.dp)
                    }

                    SegmentedButton(
                        shape = shape,
                        onClick = { onTypeChange(typeButton.key) },
                        selected = typeButton == timerUIState.timerDataUIState.typeTimer,
                        colors = SegmentedButtonDefaults.colors(
                            activeContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                            disabledActiveContentColor = MaterialTheme.colorScheme.onSurface,
                            activeContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                        ),
                        label = {
                            val colorSelected = if (typeButton == timerUIState.timerDataUIState.typeTimer) {
                                MaterialTheme.colorScheme.inverseOnSurface
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }


                            Column (
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Icon(
                                    imageVector = typeButton.icon,
                                    contentDescription = "icon type timer button",
                                    modifier = Modifier.size(20.dp)
                                )

                                LabelLargeText(
                                    stringResource(typeButton.title),
                                    modifier = Modifier.padding(top = spacing2),
                                    color = colorSelected,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        },
                        icon = {}
                    )
                }
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacing32),
            horizontalArrangement = Arrangement.Center
        ){
            CustomRipple {
                Button(
                    onClick = {
                        onStartService()
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(spacing12),
                    enabled = timerUIState.timerDataUIState.buttonEnabled
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Icon(
                            Icons.Filled.PlayCircleOutline,
                            contentDescription = "icon start",
                            tint = MaterialTheme.colorScheme.inverseOnSurface,
                            modifier = Modifier.size(20.dp)
                        )

                        TitleMediumText(
                            stringResource(R.string.timer_start),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            modifier = Modifier.padding(horizontal = spacing12, vertical = spacing2)
                        )
                    }
                }
            }
        }
    }
}