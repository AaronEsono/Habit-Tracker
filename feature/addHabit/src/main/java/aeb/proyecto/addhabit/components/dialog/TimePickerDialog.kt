package aeb.proyecto.addhabit.components.dialog

import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    modifier:Modifier = Modifier,
    notification: AddHabitNotification,
    color: Color,
    contrastColor: Color,
    onDismissRequest: () -> Unit = {},
    onConfirm: (LocalTime) -> Unit = {},
){

    val timePickerState = rememberTimePickerState(is24Hour = true,
        initialHour = notification.time.hour,
        initialMinute = notification.time.minute)

    var timeMode by rememberSaveable { mutableStateOf(true) }
    val icon = if (timeMode) Icons.Filled.Keyboard else Icons.Filled.AccessTime


    CustomDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Column (
            modifier = Modifier.padding(horizontal = spacing8, vertical = spacing10),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(timeMode){
                TimePicker(state = timePickerState,
                    colors = timePickerColors(color, contrastColor)
                )
            }else{
                TimeInput(state = timePickerState,
                    colors = timePickerColors(color, contrastColor))
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing4)
            ){

                CustomRipple {
                    IconButton(
                        onClick = { timeMode = !timeMode }
                    ) {
                        Icon(
                            icon,
                            contentDescription = "Date Picker Icon",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                CustomRipple {
                    TextButton(
                        onClick = onDismissRequest,
                    ) {
                        LabelLargeText(stringResource(R.string.add_habit_cancel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.padding(horizontal = spacing4))

                CustomRipple {
                    TextButton(
                        onClick = {
                            val selectedTime: LocalTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            onDismissRequest()
                            onConfirm(selectedTime)
                        },
                    ) {
                        LabelLargeText(stringResource(R.string.add_habit_accept),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun timePickerColors(color: Color, contrastColor: Color): TimePickerColors {
    return TimePickerDefaults.colors(
        selectorColor = color.copy(alpha = 0.8f),
        timeSelectorSelectedContainerColor = color,
        periodSelectorSelectedContainerColor = color.copy(alpha = 0.5f),

        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
        periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.onSurface,
        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,

        clockDialColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),

        clockDialSelectedContentColor = contrastColor,
        timeSelectorSelectedContentColor = contrastColor,
        periodSelectorSelectedContentColor = contrastColor,
    )
}