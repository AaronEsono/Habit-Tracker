package aeb.proyecto.habittracker.components.bottomBars.bottomRail.components

import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.text.LabelSmallText
import androidx.annotation.StringRes
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass

/**
 * A responsive text wrapper that dynamically scales navigation label typography dimensions
 * based on the device's window size constraints and display posture.
 *
 * To guarantee interface layout safety across localized string variations, this component enforces
 * a strict single-line ceiling restriction, utilizing elegant truncation fallback behavior
 * ([TextOverflow.Ellipsis]) if the text boundaries exceed the responsive viewport allocations.
 *
 * @param title The framework localization string resource identifier pointer for the label content.
 * @param windowSizeClass The active [WindowWidthSizeClass] defining width configuration metrics.
 * @param orientation The current physical layout [Orientation] posture of the screen viewport.
 */
@Composable
fun LabelBottomRailResponsive(
    @StringRes title:Int,
    windowSizeClass: WindowWidthSizeClass,
    orientation: Orientation
){
    val labelSize = remember {
        when (windowSizeClass) {
            WindowWidthSizeClass.COMPACT -> 10.sp
            WindowWidthSizeClass.MEDIUM -> {
                if (orientation == Orientation.Landscape) 10.sp else 15.sp
            }
            WindowWidthSizeClass.EXPANDED -> 16.sp
            else -> 16.sp
        }
    }


    LabelSmallText(
        stringResource(title),
        fontSize = labelSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

}