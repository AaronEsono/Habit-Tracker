package aeb.proyecto.habittracker.ui.components.timePicker

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.buttons.CustomOutlinedButtonButton
import aeb.proyecto.habittracker.ui.theme.DarKThemeText
import aeb.proyecto.habittracker.ui.theme.secondaryColorApp
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
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerHabit(
    onConfirm: (Notification) -> Unit = {},
    onDismiss: () -> Unit = {},
    color: Color,
    notification: Notification?
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = notification?.hour ?: currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = notification?.minute ?: currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Dialog(onDismissRequest = { onDismiss() }) {

        Card(
            shape = RoundedCornerShape(spacing8),
            elevation = CardDefaults.cardElevation(
                defaultElevation = spacing8
            ),
            colors = CardDefaults.cardColors(
                containerColor = secondaryColorApp
            )
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(spacing16)){
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        selectorColor = color.copy(alpha = 0.8f),
                        timeSelectorSelectedContainerColor = color,
                        clockDialSelectedContentColor = DarKThemeText,
                        clockDialUnselectedContentColor = DarKThemeText,
                        periodSelectorSelectedContainerColor = color.copy(alpha = 0.5f),
                        clockDialColor = secondaryColorApp,
                        periodSelectorUnselectedContentColor = secondaryColorApp,
                        timeSelectorUnselectedContainerColor = secondaryColorApp,
                        periodSelectorSelectedContentColor = DarKThemeText,
                        periodSelectorUnselectedContainerColor = DarKThemeText,
                        timeSelectorSelectedContentColor = DarKThemeText,
                        timeSelectorUnselectedContentColor = DarKThemeText,
                    )
                )

                Row(modifier = Modifier.fillMaxWidth()) {

                    CustomOutlinedButtonButton(
                        title = R.string.buttons_cancel, icon = R.drawable.ic_cancel,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onDismiss()
                        }
                    )

                    Spacer(modifier = Modifier.padding(horizontal = spacing8))

                    CustomFilledButton(
                        title = R.string.buttons_accept,
                        icon = R.drawable.ic_check,
                        color = color,
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