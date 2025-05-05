package aeb.proyecto.timer.components.typeSegmentedScreen

import aeb.proyecto.timer.R
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

@Composable
fun StopWatchSegmentedScreen(){

    LabelLargeText(
        stringResource(R.string.timer_stopwatch_start),
        fontSize = 48.sp
    )

}