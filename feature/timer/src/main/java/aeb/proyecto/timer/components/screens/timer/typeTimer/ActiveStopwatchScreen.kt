package aeb.proyecto.timer.components.screens.timer.typeTimer

import aeb.proyecto.stopwatch.manager.TypeTimer
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ActiveStopwatchScreen(
    hour:String
){
    LabelMediumText(
        hour,
        fontSize = 60.sp
    )
}