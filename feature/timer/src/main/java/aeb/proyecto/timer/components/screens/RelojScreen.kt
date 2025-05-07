package aeb.proyecto.timer.components.screens

import aeb.proyecto.timer.R
import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.components.timerPicker.TimerPicker
import aeb.proyecto.timer.components.typeSegmentedScreen.IntervalSegmentedScreen
import aeb.proyecto.timer.components.typeSegmentedScreen.StopWatchSegmentedScreen
import aeb.proyecto.timer.components.typeSegmentedScreen.TimerSegmentedScreen
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.timer.model.SegmentedButtonOptions
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing26
import aeb.proyecto.ui.dimmens.Dimmens.spacing36
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing40
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleLargeText
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RelojScreen(
    timerUIState: TimerUiState.Success,
    onHourChange:(String) -> Unit,
    onMinuteChange:(String) -> Unit,
    onSecondChange: (String) -> Unit,
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit,
    onTypeChange: (Int) -> Unit,
    onStartService: () -> Unit,
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
                            onIntervalHourChange = onIntervalHourChange
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
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
                            activeContentColor = MaterialTheme.colorScheme.onSurface,
                            disabledActiveContentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        label = {

                            Column (
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Icon(
                                    imageVector = typeButton.icon,
                                    contentDescription = "icon type timer button",
                                )

                                LabelMediumText(
                                    stringResource(typeButton.title),
                                    modifier = Modifier.padding(top = spacing2)
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
                .padding(top = spacing26),
            horizontalArrangement = Arrangement.Center
        ){
            CustomRipple {
                Button(
                    onClick = onStartService,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(spacing12),
                ) {
                    TitleLargeText(
                        stringResource(R.string.timer_start),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = spacing12, vertical = spacing2)
                    )
                }
            }
        }
    }
}