package aeb.proyecto.timer.components.commom.typeTimer.intervalSegmented.utils

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

data class ResponsiveSizes(
    val fontSize: TextUnit,
    val labelFontSize: TextUnit,
    val buttonSize: Dp,
    val labelWidth: Dp
)

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