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