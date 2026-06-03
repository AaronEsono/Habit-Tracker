package aeb.proyecto.ui.repeatingClick

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Custom layout modifier that injects an accelerated repeating click gesture pipeline onto a visual node.
 * As long as the physical pointer input remains pressed, it continuously fires execution triggers
 * while exponentially decaying the interval window to achieve a high-performance progressive counting acceleration curve.
 *
 * Fully integrated with the platform [MutableInteractionSource] to feed stateful visual indicators
 * cleanly without breaking event cancellation scopes.
 *
 * @param interactionSource The stateful event tracker pipeline tasked with emitting press and release tokens.
 * @param enabled Boundary flag to ignore gesture captures if the host element enters an inactive state.
 * @param maxDelayMillis Initial baseline delay window applied between the first consecutive click sequences.
 * @param minDelayMillis The absolute safety floor speed limit allowed for click cycles during full acceleration.
 * @param delayDecayFactor Percentage fraction (0.0f to 1.0f) stripped away from the active delay buffer on every tick.
 * @param onClick The functional callback closure invoked on every execution tick cycle.
 */
@SuppressLint("SuspiciousModifierThen")
fun Modifier.repeatingClickable(
    interactionSource: MutableInteractionSource,
    enabled: Boolean = true,
    maxDelayMillis: Long = 1000,
    minDelayMillis: Long = 5,
    delayDecayFactor: Float = .20f,
    onClick: () -> Unit
): Modifier = this.then(
    composed{
        val currentClickListener by rememberUpdatedState(onClick)

        pointerInput(interactionSource, enabled){
            coroutineScope {
                awaitEachGesture {
                    val dowm = awaitFirstDown(requireUnconsumed = false)
                    val downPress = PressInteraction.Press(dowm.position)

                    val heldButtonJob = launch {
                        // Send the press through the interaction source
                        interactionSource.emit(downPress)
                        var currentDelayMillis = maxDelayMillis

                        while (enabled && dowm.pressed) {
                            currentClickListener()
                            delay(currentDelayMillis)
                            val nextMillis = currentDelayMillis - (currentDelayMillis * delayDecayFactor)
                            currentDelayMillis = nextMillis.toLong().coerceAtLeast(minDelayMillis)
                        }
                    }

                    // Standby and evaluate terminal boundary events (User lifting finger or moving past active tracking frames)
                    val up = waitForUpOrCancellation()
                    heldButtonJob.cancel()

                    // Resolve the semantic gesture outcome cleanly to update design indicators
                    val releaseOrCancel = when (up) {
                        null -> PressInteraction.Cancel(downPress)
                        else -> PressInteraction.Release(downPress)
                    }
                    launch {
                        // Send the result through the interaction source
                        interactionSource.emit(releaseOrCancel)
                    }
                }
            }
        }.indication(
            interactionSource = interactionSource,
            indication =  null
        )
    }
)