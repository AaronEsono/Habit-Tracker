package aeb.proyecto.ui.orientation

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Structural design system token representing the physical display layout vector of the device.
 * Used to orchestrate adaptive UI layout mutations dynamically based on spatial aspect ratios.
 */
enum class Orientation {
    /** Vertical screen alignment profile. */
    Portrait,

    /** Horizontal screen alignment profile. */
    Landscape
}

/**
 * Dynamic environmental composition hook tracking real-time device orientation shifts.
 * Registers implicitly against the platform [LocalConfiguration] pipeline to trigger layout
 * recompositions instantly when the physical hardware rotates, supplying a clean semantic [Orientation] token.
 *
 * Defaults defensively to [Orientation.Portrait] to bypass short-lived undefined configuration steps.
 *
 * @return The active stateful [Orientation] boundary governing the display layout matrix.
 */
@Composable
fun getOrientation():Orientation{
    val localization = LocalConfiguration.current
    val orientation = localization.orientation

    return when(orientation){
        Configuration.ORIENTATION_PORTRAIT -> Orientation.Portrait
        Configuration.ORIENTATION_LANDSCAPE -> Orientation.Landscape
        // Guard clause ensuring stability during transient configuration cycles (e.g., ORIENTATION_UNDEFINED)
        else -> Orientation.Portrait
    }
}