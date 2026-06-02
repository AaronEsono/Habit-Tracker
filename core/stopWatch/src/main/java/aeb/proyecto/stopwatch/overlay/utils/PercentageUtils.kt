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

/**
 * Computes and fluidly interpolates structural tracking completion fractions based on the active device instrument.
 * Leverages defensive numeric validation fallbacks to neutralize division-by-zero crash vectors during
 * state transformations, wrapping output values in sub-second hardware animations to guarantee pristine
 * visual execution fluidly matched to execution ticks.
 *
 * @param typeTimer The operational configuration model specifying time thresholds.
 * @param elapsedTime The current chronological checkpoint milestone calculated in milliseconds.
 * @return A highly optimized, continuous floating-point progress scale factor ranging between 0.0f and 1.0f.
 */
@Composable
fun getPercentage(
    typeTimer: TypeTimer,
    elapsedTime: Long,
):Float{

    // Resolve targeted absolute completion fractions based on the active modality rules
    val targetProgress:Float = when(typeTimer){
        is TypeTimer.INTERVAL -> {
            // Secure fractional denominators defensively using fallback baseline metrics
            val totalTime = if (typeTimer.state == IntervalState.Work) {
                typeTimer.time.takeIf { it > 0 } ?: 1
            } else {
                typeTimer.rest.takeIf { it > 0 } ?: 1
            }

            // Cache division calculations optimizing resource consumption across redraw cycles
            remember(elapsedTime, totalTime) {
                (elapsedTime.toFloat() / totalTime).coerceIn(0f, 1f)
            }
        }
        TypeTimer.STOPWATCH -> 0f // Progressive stopwatches scale linearly upward without fixed upper target constraints
        is TypeTimer.TIMER -> {
            remember(elapsedTime, typeTimer.time) {
                (elapsedTime.toFloat() / typeTimer.time).coerceIn(0f, 1f)
            }
        }
    }

    // Interpolate steps dynamically at maximum frame rates to bypass visual stutter between 200ms updates
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "progressAnimation"
    )

    return animatedProgress
}