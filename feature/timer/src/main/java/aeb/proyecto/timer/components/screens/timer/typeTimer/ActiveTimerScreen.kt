package aeb.proyecto.timer.components.screens.timer.typeTimer

import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import aeb.proyecto.stopwatch.utils.longToHMS
import aeb.proyecto.timer.model.TimerServiceUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActiveTimerScreen(
    serviceState: TimerServiceUIState.TimerRunning,
    typeTimer: TypeTimer.TIMER
) {
    val targetProgress = remember(serviceState.elapsedTime, typeTimer.time) {
        (serviceState.elapsedTime.toFloat() / typeTimer.time).coerceIn(0f, 1f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 50, easing = LinearEasing),
        label = "progressAnimation"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (serviceState.currentState == StopwatchState.Stopped)
            MaterialTheme.colorScheme.surfaceContainer
        else
            MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(durationMillis = 300),
        label = "backgroundColorAnimation"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val circleSize = maxWidth * 0.75f
        val offsetY = circleSize * 0.30f

        CircularProgressIndicator(
            progress = { animatedProgress },
            strokeWidth = 10.dp,
            color = backgroundColor,
            trackColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(circleSize)
        )

        LabelLargeText(
            text = longToHMS(typeTimer.time),
            fontSize = 16.sp,
            color = backgroundColor,
            modifier = Modifier
                .offset(y = offsetY)
                .align(Alignment.TopCenter)
        )

        LabelLargeText(
            text = serviceState.hourString,
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            color = backgroundColor,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}