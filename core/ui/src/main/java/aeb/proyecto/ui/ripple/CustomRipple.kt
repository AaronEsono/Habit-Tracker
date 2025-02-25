package aeb.proyecto.ui.ripple

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun customRipple(color: Color = MaterialTheme.colorScheme.scrim): RippleConfiguration {
    return RippleConfiguration(
        color = color,
        rippleAlpha = RippleAlpha(0f, 0f, 0f, 1f)
    )
}