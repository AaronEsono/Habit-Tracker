package aeb.proyecto.stopwatch.overlay

import aeb.proyecto.stopwatch.R
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
import aeb.proyecto.stopwatch.overlay.utils.getTitle
import aeb.proyecto.ui.date.utils.getTextToday
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
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

/**
 * Root graphical user interface canvas rendering the interactive floating window overlay.
 * Monitors asynchronous runtime state pipelines safely across platform lifecycle switches, mapping
 * system gesture drag translations, dimensional mutations, and dynamic context-aware color schemes
 * into a highly optimized, single-purpose tracking controller panel.
 *
 * @param stateManager The centralized reactive state controller driving numerical tracking variables.
 * @param onDrag Functional callback tracking raw coordinate translations to shift the physical window boundaries.
 * @param onCloseOverlay Terminal callback teardown invocation to destroy the window canvas layout.
 * @param onOpenApp Intercept interaction routing focus back to the main application context pipeline.
 * @param onPaused Callback invocation pausing active chronological calculation loops.
 * @param onResumed Callback invocation re-activating suspended countdown timers or stopwatches.
 * @param onFinished Terminal validation callback finalizing successful time investment segments.
 * @param onCancel Destructive reset callback purging active session metrics.
 */
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

    // Stream-safe collection constraints linked directly to the host platform environment lifecycle
    val timeElapsed = stateManager.timerString.collectAsStateWithLifecycle().value
    val state = stateManager.currentState.collectAsStateWithLifecycle().value
    val elapsedTime = stateManager.elapsedTime.collectAsStateWithLifecycle().value
    val typeTimer = stateManager.typeTimer.collectAsStateWithLifecycle().value
    val habitLinked = stateManager.habitLinked.collectAsStateWithLifecycle().value

    // Resolve structural color layouts and smooth calculation progress vectors
    val theme = remember (isDarkTheme){ if (isDarkTheme) darkOverlayTheme else lightOverlayTheme }
    val percentage = getPercentage(typeTimer, elapsedTime)

    // Dynamic color matching engine mapping the tracking track to specific habit palette profiles
    val colorBar = remember (habitLinked){
        habitLinked?.habit?.let {
            Color(it.color)
        }?: theme.filledBarColor
    }

    // Smooth structural dimension animation translating boundaries between collapsed and expanded modes
    val animatedWidth by animateDpAsState(
        targetValue = if (expanded) 150.dp else 100.dp,
        animationSpec = tween(durationMillis = 300)
    )

    // Root physical frame layout capturing drag, click bounds, and managing canvas drawing clipping masks
    Box(
        modifier = Modifier
            .background(theme.backgroundColor, RoundedCornerShape(spacing16))
            .pointerInput(true) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // Halt gesture transmission to underlying software layers
                    val (dx, dy) = dragAmount
                    onDrag(dx.toInt(), dy.toInt()) // Dispatch delta translations to window managers
                }
            }
            .padding(horizontal = spacing12, vertical = spacing8)
            .width(animatedWidth)
            .clickable (
                indication = null, // Strip heavy material ripple effects to keep composition lightweight
                interactionSource = remember { MutableInteractionSource() }
            ){
                expanded = true
            }
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            if(expanded){
                // Extended contextual controls header panel
                Row (modifier = Modifier.padding(bottom = spacing8)){
                    Icon(
                        Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "",
                        tint = theme.textColor,
                        modifier = Modifier.size(22.dp)
                            .clickable (
                                indication = null,
                                interactionSource = null
                            ){
                                expanded = false
                            }
                    )

                    Spacer(modifier = Modifier.padding(horizontal = spacing4))

                    Icon(
                        Icons.Filled.ArrowUpward,
                        contentDescription = "",
                        tint = theme.textColor,
                        modifier = Modifier.size(22.dp)
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
                        modifier = Modifier.size(22.dp)
                            .clickable (
                                indication = null,
                                interactionSource = null
                            ){ onCloseOverlay()}
                    )

                }

                // Smoothly crossfade localized heading tags upon state transitions
                AnimatedContent(
                    targetState = getTitle(typeTimer,state)
                ) { titleAnim ->
                    LabelSmallText(
                        text = titleAnim,
                        color = theme.textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Optional tracking relationship indicator label
                if(habitLinked != null){
                    LabelSmallText(
                        text = stringResource(R.string.timer_title_habit,habitLinked.habit.name, getTextToday(habitLinked.day.date)),
                        color = theme.textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }


                Spacer(modifier = Modifier.padding(vertical = spacing2))
            }

            // High-visibility core timer character rendering node
            LabelMediumText(
                text = timeElapsed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = theme.textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Progressive stopwatches ignore target bounding boxes; suppress tracking bars for that configuration
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
                // Extended operational state interaction button row
                when (state) {
                    StopwatchState.Idle -> Unit
                    StopwatchState.Stopped, StopwatchState.InProgress -> {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            AnimatedContent(
                                targetState = state,
                                modifier = Modifier.fillMaxWidth(0.5f)
                            ) { anim ->
                                when (anim) {
                                    StopwatchState.Idle, StopwatchState.Finished -> Unit
                                    StopwatchState.Stopped -> {
                                        ResumeButton(
                                            color = theme.textColor,
                                            onClick = onResumed,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    StopwatchState.InProgress -> {
                                        PauseButton(
                                            color = theme.textColor,
                                            onClick = onPaused,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }

                            CancelButton(
                                onClick = onCancel,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                    StopwatchState.Finished -> {
                        FinishButton(color = theme.textColor, onClick = onFinished)
                    }
                }
            }
        }
    }
}