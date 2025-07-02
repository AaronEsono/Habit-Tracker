package aeb.proyecto.timer.components.commom.typeActiveTimer

import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import aeb.proyecto.stopwatch.utils.longToHMS
import aeb.proyecto.timer.model.TimerServiceUIState
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "progressAnimation"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (serviceState.currentState == StopwatchState.Stopped)
            MaterialTheme.colorScheme.surfaceContainer
        else{
            if(serviceState.habitLinked != null)
                Color(serviceState.habitLinked.habit.color)
            else
                MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(durationMillis = 300),
        label = "backgroundColorAnimation"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val circleSize = maxWidth
        val offsetY = circleSize * 0.10f
        val density = LocalDensity.current

        val labelFontSize = with(density) { (circleSize * 0.05f).toSp() }
        val timeFontSize = with(density) { (circleSize * 0.20f).toSp() }

        CircularProgressIndicator(
            progress = { animatedProgress },
            strokeWidth = 10.dp,
            color = backgroundColor,
            trackColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(circleSize)
        )

        LabelLargeText(
            text = longToHMS(typeTimer.time),
            fontSize = labelFontSize,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .offset(y = offsetY)
                .align(Alignment.TopCenter)
        )

        LabelLargeText(
            text = serviceState.hourString,
            fontSize = timeFontSize,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}