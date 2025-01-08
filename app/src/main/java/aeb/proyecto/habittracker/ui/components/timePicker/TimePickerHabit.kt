package aeb.proyecto.habittracker.ui.components.timePicker

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.buttons.CustomOutlinedButton
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerHabit(
    shape: RoundedCornerShape = RoundedCornerShape(spacing8),
    defaultElevation:Dp = spacing8,
    modifier:Modifier = Modifier,
    principalColor: Color,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    clockDialSelectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
    clockDialUnselectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
    clockDialColor: Color =  MaterialTheme.colorScheme.secondaryContainer,
    periodSelectorUnselectedContentColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    timeSelectorUnselectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    periodSelectorSelectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
    periodSelectorUnselectedContainerColor: Color = MaterialTheme.colorScheme.onSurface,
    timeSelectorSelectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
    timeSelectorUnselectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
    onConfirm: (Notification) -> Unit = {},
    onDismiss: () -> Unit = {},
    notification: Notification?
) {
    val currentTime = remember {Calendar.getInstance()}

    val timePickerState = rememberTimePickerState(
        initialHour = notification?.hour ?: currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = notification?.minute ?: currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Dialog(onDismissRequest = { onDismiss() }) {

        Card(
            shape = shape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = defaultElevation
            ),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            )
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(spacing16)){
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        selectorColor = principalColor.copy(alpha = 0.8f),
                        timeSelectorSelectedContainerColor = principalColor,
                        clockDialSelectedContentColor = clockDialSelectedContentColor,
                        clockDialUnselectedContentColor = clockDialUnselectedContentColor,
                        periodSelectorSelectedContainerColor = principalColor.copy(alpha = 0.5f),
                        clockDialColor = clockDialColor,
                        periodSelectorUnselectedContentColor = periodSelectorUnselectedContentColor,
                        timeSelectorUnselectedContainerColor = timeSelectorUnselectedContainerColor,
                        periodSelectorSelectedContentColor = periodSelectorSelectedContentColor,
                        periodSelectorUnselectedContainerColor = periodSelectorUnselectedContainerColor,
                        timeSelectorSelectedContentColor = timeSelectorSelectedContentColor,
                        timeSelectorUnselectedContentColor = timeSelectorUnselectedContentColor,
                    )
                )

                Row(modifier = Modifier.fillMaxWidth()) {

                    CustomOutlinedButton(
                        title = R.string.buttons_cancel,
                        icon = R.drawable.ic_cancel,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onDismiss()
                        }
                    )

                    Spacer(modifier = Modifier.padding(horizontal = spacing8))

                    CustomFilledButton(
                        title = R.string.buttons_accept,
                        icon = R.drawable.ic_check,
                        color = principalColor,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onConfirm(Notification( hour = timePickerState.hour,minute =  timePickerState.minute))
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}