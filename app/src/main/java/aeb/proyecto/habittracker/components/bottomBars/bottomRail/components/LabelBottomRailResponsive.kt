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
            WindowWidthSizeClass.EXPANDED -> 18.sp
            else -> 18.sp
        }
    }


    LabelSmallText(
        stringResource(title),
        fontSize = labelSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

}