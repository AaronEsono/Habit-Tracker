package aeb.proyecto.timer.components.typeSegmentedScreen

import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.components.timerPicker.TimerPicker
import aeb.proyecto.timer.model.HourSelectedState
import androidx.compose.runtime.Composable

@Composable
fun TimerSegmentedScreen(
    hourSelectedState: HourSelectedState,
    onHourChange:(String) -> Unit,
    onMinuteChange:(String) -> Unit,
    onSecondChange: (String) -> Unit
){

    TimerPicker(
        timerSelected = hourSelectedState,
        onHourChange = onHourChange,
        onMinuteChange = onMinuteChange,
        onSecondChange = onSecondChange
    )

}