package aeb.proyecto.habittracker.ui.components.timePicker

import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialExample(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Column {
        TimePicker(
            state = timePickerState,
        )
        Button(onClick = onDismiss) {
            LabelSmallText(text = "Cancel")
        }
        Button(onClick = onConfirm) {
            LabelSmallText(text = "Accept")
        }
    }
}