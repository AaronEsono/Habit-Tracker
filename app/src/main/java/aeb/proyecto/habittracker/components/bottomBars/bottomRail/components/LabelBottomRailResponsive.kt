package aeb.proyecto.habittracker.components.bottomBars.bottomRail.components

import aeb.proyecto.ui.text.LabelSmallText
import androidx.annotation.StringRes
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun LabelBottomRailResponsive(
    @StringRes title:Int,
    windowSizeClass: WindowWidthSizeClass
){
    val labelSize = when (windowSizeClass) {
        WindowWidthSizeClass.COMPACT -> 10.sp
        WindowWidthSizeClass.MEDIUM -> 14.sp
        WindowWidthSizeClass.EXPANDED -> 20.sp
        else -> 20.sp
    }


    LabelSmallText(
        stringResource(title),
        fontSize = labelSize
    )

}