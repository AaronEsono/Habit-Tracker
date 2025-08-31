package aeb.proyecto.stopwatch.overlay

import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import aeb.proyecto.stopwatch.overlay.colors.darkOverlayTheme
import aeb.proyecto.stopwatch.overlay.colors.lightOverlayTheme
import aeb.proyecto.stopwatch.overlay.components.CancelButton
import aeb.proyecto.stopwatch.overlay.components.FinishButton
import aeb.proyecto.stopwatch.overlay.components.PauseButton
import aeb.proyecto.stopwatch.overlay.components.PercentageBar
import aeb.proyecto.stopwatch.overlay.components.ResumeButton
import aeb.proyecto.stopwatch.overlay.utils.getPercentage
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing30
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OverlayContent(
    stateManager: StopWatchStateManager,
    onDrag: (dx: Int, dy: Int) -> Unit,
    onCloseOverlay: () -> Unit,
    onOpenApp: () -> Unit,
    onPaused: () -> Unit,
    onResumed: () -> Unit,
    onFinished: () -> Unit,
    onCancel: () -> Unit
){

    val isDarkTheme = isSystemInDarkTheme()
    var expanded by remember { mutableStateOf(false) }

    val timeElapsed = stateManager.timerString.collectAsStateWithLifecycle().value
    val state = stateManager.currentState.collectAsStateWithLifecycle().value
    val elapsedTime = stateManager.elapsedTime.collectAsStateWithLifecycle().value
    val typeTimer = stateManager.typeTimer.collectAsStateWithLifecycle().value
    val habitLinked = stateManager.habitLinked.collectAsStateWithLifecycle().value

    val theme = remember (isDarkTheme){ if (isDarkTheme) darkOverlayTheme else lightOverlayTheme }
    val percentage = getPercentage(typeTimer, elapsedTime)
    val colorBar = remember (habitLinked){
        habitLinked?.habit?.let {
            Color(it.color)
        }?: theme.filledBarColor
    }

    Box(
        modifier = Modifier
            .background(theme.backgroundColor, RoundedCornerShape(spacing16))
            .pointerInput(true) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val (dx, dy) = dragAmount
                    onDrag(dx.toInt(), dy.toInt())
                }
            }
            .padding(horizontal = spacing12, vertical = spacing8)
            .width(if(!expanded) 100.dp else 150.dp)
            .clickable (
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ){ expanded = true }
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            if(expanded){
                //Titulo e iconos
                Row (modifier = Modifier.padding(bottom = spacing8)){
                    Icon(
                        Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "",
                        tint = theme.textColor,
                        modifier = Modifier.size(20.dp)
                            .clickable (
                                indication = null,
                                interactionSource = null
                            ){
                                expanded = false
                            }
                    )

                    Spacer(modifier = Modifier.padding(horizontal = spacing2))

                    Icon(
                        Icons.Filled.ArrowUpward,
                        contentDescription = "",
                        tint = theme.textColor,
                        modifier = Modifier.size(20.dp)
                            .clickable (
                                indication = null,
                                interactionSource = null
                            ){ onOpenApp() }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "",
                        tint = theme.textColor,
                        modifier = Modifier.size(20.dp)
                            .clickable (
                                indication = null,
                                interactionSource = null
                            ){ onCloseOverlay()}
                    )

                }

                //Falta darle el titulo
                LabelMediumText(
                    text = "Temporizador",
                    color = theme.textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.padding(vertical = spacing4))
            }

            LabelMediumText(
                text = timeElapsed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = theme.textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            if(typeTimer != TypeTimer.STOPWATCH){
                Spacer(modifier = Modifier.padding(vertical = spacing2))

                PercentageBar(
                    percentage = percentage,
                    filledColor = colorBar,
                    emptyColor = theme.emptyBarColor,
                    pausedColor = theme.pausedBarColor,
                    isPaused = state == StopwatchState.Stopped
                )
            }

            if(expanded){
                Row {
                    when (state) {
                        StopwatchState.Idle -> Unit
                        StopwatchState.Stopped, StopwatchState.InProgress -> {
                            AnimatedContent(
                                targetState = state
                            ) { anim ->
                                when (anim) {
                                    StopwatchState.Idle, StopwatchState.Finished -> Unit
                                    StopwatchState.Stopped -> {
                                        ResumeButton(
                                            color = theme.textColor,
                                            onClick = onResumed,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    StopwatchState.InProgress -> {
                                        PauseButton(
                                            color = theme.textColor,
                                            onClick = onPaused,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }

                            CancelButton(onClick = onCancel, modifier = Modifier.weight(1f))
                        }

                        StopwatchState.Finished -> {
                            FinishButton(color = theme.textColor, onClick = onFinished)
                        }
                    }
                }
            }
        }
    }
}