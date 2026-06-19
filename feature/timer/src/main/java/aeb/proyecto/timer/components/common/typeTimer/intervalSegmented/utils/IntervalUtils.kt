package aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/**
 * Encapsulates responsive metrics calculated based on the available screen constraints.
 * Used to ensure UI consistency across different device sizes.
 *
 * @param fontSize Font size for primary titles.
 * @param labelFontSize Font size for content labels.
 * @param buttonSize Dimension for interactive button elements.
 * @param labelWidth Constraint width for text labels to prevent overflow.
 */
data class ResponsiveSizes(
    val fontSize: TextUnit,
    val labelFontSize: TextUnit,
    val buttonSize: Dp,
    val labelWidth: Dp
)

/**
 * Calculates adaptive UI sizes based on available [maxWidth] and [maxHeight].
 * Applies a scaling factor if the screen is too narrow to ensure ergonomic touch targets.
 *
 * @param maxWidth The available horizontal space.
 * @param maxHeight The available vertical space.
 * @return A [ResponsiveSizes] object containing calculated dimensions and font sizes.
 */
@Composable
fun calculateResponsiveSizes(maxWidth: Dp, maxHeight: Dp): ResponsiveSizes {
    val density = LocalDensity.current

    val titleHeight = maxHeight * 0.2f
    val contentHeight = maxHeight * 0.65f

    val isVeryNarrow = maxWidth < 300.dp
    val scaleFactor = if (isVeryNarrow) 0.7f else 1f

    return with(density) {
        ResponsiveSizes(
            fontSize = (titleHeight * scaleFactor).toSp(),
            labelFontSize = (contentHeight * 0.7f * scaleFactor).toSp(),
            buttonSize = contentHeight * 0.6f * scaleFactor,
            labelWidth = maxWidth * 0.4f
        )
    }
}