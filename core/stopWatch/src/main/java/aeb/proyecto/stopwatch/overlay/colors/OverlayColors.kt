package aeb.proyecto.stopwatch.overlay.colors

import androidx.compose.ui.graphics.Color

/**
 * Structural color palette token container tailored for the minimalist floating window subsystem.
 * Encapsulates dedicated monochromatic layout color parameters to decouple overlay drawing components
 * from global application Material Theme architectures.
 *
 * @property backgroundColor The root layout canvas surface background color boundary.
 * @property textColor The primary high-contrast typography color for digits and descriptive headers.
 * @property emptyBarColor The baseline or unfulfilled track layout color for progress indicators.
 * @property filledBarColor The active tracking progression or countdown indicator fill color.
 * @property pausedBarColor The neutralized status color applied to tracks when the ticking engine is suspended.
 */
data class OverlayColors(
    val backgroundColor: Color,
    val textColor: Color,
    val emptyBarColor: Color,
    val filledBarColor: Color,
    val pausedBarColor: Color
)

/**
 * High-contrast, monochromatic dark-mode theme asset profile targeting charcoal configurations.
 * Leverages deep dark gray matrices contrasted with stark paper white accents to reduce visual strain.
 */
val darkOverlayTheme = OverlayColors(
    backgroundColor = Color(0xFF1E1E1E),
    textColor = Color(0xFFFAFAFA),
    emptyBarColor = Color(0xFF3A3A3A),
    filledBarColor = Color(0xFFFAFAFA),
    pausedBarColor = Color(0xFF888888)
)

/**
 * High-contrast, monochromatic light-mode theme asset profile targeting pristine paper configurations.
 * Leverages crisp white foundations contrasted with ink-black indicators to maximize daylight legibility.
 */
val lightOverlayTheme = OverlayColors(
    backgroundColor = Color(0xFFFAFAFA),
    textColor = Color(0xFF1E1E1E),
    emptyBarColor = Color(0xFFE0E0E0),
    filledBarColor = Color(0xFF1E1E1E),
    pausedBarColor = Color(0xFF888888)
)