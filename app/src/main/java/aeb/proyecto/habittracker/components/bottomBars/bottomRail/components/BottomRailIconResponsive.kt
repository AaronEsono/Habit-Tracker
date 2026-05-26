package aeb.proyecto.habittracker.components.bottomBars.bottomRail.components

import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass

/**
 * A responsive wrapper around the standard [Icon] component that dynamically scales its dimensions
 * based on the device's window size class and adaptive orientation.
 *
 * This utility ensures that core action targets remain visually proportional and ergonomically optimized
 * across a diverse spectrum of form factors, preventing graphical regression when transitioning from
 * compact mobile screens up to medium tablets or expanded desktop displays.
 *
 * @param icon The drawable framework resource identifier pointer for the vector asset.
 * @param windowSizeClass The active [WindowWidthSizeClass] specifying the current window width constraints.
 * @param orientation The current physical structural [Orientation] layout posture of the viewport.
 */
@Composable
fun BottomRailIconResponsive(
    icon:Int,
    windowSizeClass: WindowWidthSizeClass,
    orientation: Orientation
){

    val iconSize = remember {
        when (windowSizeClass) {
            WindowWidthSizeClass.COMPACT -> 20.dp
            WindowWidthSizeClass.MEDIUM -> {
                if (orientation == Orientation.Landscape) 20.dp else 25.dp
            }
            WindowWidthSizeClass.EXPANDED -> 30.dp
            else -> 30.dp
        }
    }

    Icon(
        painter = painterResource(icon),
        contentDescription = "bottom bar icon",
        modifier = Modifier.size(iconSize)
    )

}