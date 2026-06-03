package aeb.proyecto.ui.ripple

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

/**
 * Stateful design system override container that customizes the platform interactive ripple behavior.
 * Injects a specialized [RippleConfiguration] down the composition tree to strip ambient hovering/focus
 * noise, isolating visual feedback strictly to high-contrast, sharp execution presses.
 *
 * Useful for maintaining a cohesive tactile feedback language inside minimal, monochromatic UI segments.
 *
 * @param color The functional tint color applied to the expanding press wave. Defaults to the system's [scrim] token.
 * @param content The declarative architectural slot layout targeted to inherit this local interaction profile.
 */
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