package aeb.proyecto.timer.components.common.typeActiveTimer

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