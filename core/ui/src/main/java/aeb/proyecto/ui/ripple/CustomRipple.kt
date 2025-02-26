package aeb.proyecto.ui.ripple

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun CustomRipple(
    color: Color = MaterialTheme.colorScheme.scrim,
    content: @Composable () -> Unit
) {

    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(
            color = color,
            rippleAlpha = RippleAlpha(0f, 0f, 0f, 1f)
        )
    ) {
        content()
    }
}