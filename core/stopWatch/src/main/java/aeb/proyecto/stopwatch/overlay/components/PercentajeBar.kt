package aeb.proyecto.stopwatch.overlay.components

import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * High-performance, ultra-lightweight linear progress indicator optimized for rapid rendering loops.
 * Utilizes a dual overlapping structural [Box] composition model to clip track vectors,
 * leveraging defensive boundaries to filter mathematical rounding anomalies during continuous updates.
 *
 * @param modifier The structural composition modifier layout layout adjustment token.
 * @param percentage The raw floating-point progress scale factor ranging nominally between 0.0 (empty) and 1.0 (full).
 * @param filledColor The active monochromatic paint color asset applied to the progress indicator track.
 * @param emptyColor The baseline background track surface color defining unfulfilled time allotments.
 * @param pausedColor The neutralized mid-tone gray color applied to the track when the ticking mechanism suspends.
 * @param isPaused State flag instructing the canvas to swap active track color palettes to signify a freeze.
 */
@Composable
fun PercentageBar(
    modifier: Modifier = Modifier,
    percentage: Float,
    filledColor: Color,
    emptyColor: Color,
    pausedColor: Color,
    isPaused : Boolean = false
) {

    // Cache the runtime track color computation using state-key tracking optimization guardrails
    val color = remember (isPaused){ if (isPaused) pausedColor else filledColor }

    // Root background track casing bounding the component physical geometry
    Box(
        modifier = modifier
            .height(5.dp)
            .width(85.dp)
            .background(emptyColor, RoundedCornerShape(spacing20))
    ) {
        // Active foreground fill indicator track adjusting its bounds dynamically
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = percentage.coerceIn(0f, 1f))
                .background(color, RoundedCornerShape(spacing20))
        )
    }
}