package aeb.proyecto.habittracker.components.bottomBars.bottomRail.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun BottomRailIconResponsive(
    icon:Int,
    windowSizeClass: WindowWidthSizeClass
){
    val iconSize = when (windowSizeClass) {
        WindowWidthSizeClass.COMPACT -> 20.dp
        WindowWidthSizeClass.MEDIUM -> 25.dp
        WindowWidthSizeClass.EXPANDED -> 35.dp
        else -> 30.dp
    }

    Icon(
        painter = painterResource(icon),
        contentDescription = "bottom bar icon",
        modifier = Modifier.size(iconSize)
    )

}