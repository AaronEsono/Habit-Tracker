package aeb.proyecto.stopwatch.overlay.utils

import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.TypeTimer
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun getPercentage(
    typeTimer: TypeTimer,
    elapsedTime: Long,
):Float{

    val targetProgress:Float = when(typeTimer){
        is TypeTimer.INTERVAL -> {
            val totalTime = if (typeTimer.state == IntervalState.Work) {
                typeTimer.time.takeIf { it > 0 } ?: 1
            } else {
                typeTimer.rest.takeIf { it > 0 } ?: 1
            }

            remember(elapsedTime, totalTime) {
                (elapsedTime.toFloat() / totalTime).coerceIn(0f, 1f)
            }
        }
        TypeTimer.STOPWATCH -> 0f
        is TypeTimer.TIMER -> {
            remember(elapsedTime, typeTimer.time) {
                (elapsedTime.toFloat() / typeTimer.time).coerceIn(0f, 1f)
            }
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "progressAnimation"
    )

    return animatedProgress
}