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

                    val up = waitForUpOrCancellation()
                    heldButtonJob.cancel()

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