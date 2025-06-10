package aeb.proyecto.addhabit.components.commom.dialog

import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
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
    initialTimeMode:Boolean = true,
    onDismissRequest: () -> Unit = {},
    onConfirm: (LocalTime) -> Unit = {},
){
    val is24hoursMode = remember { mutableStateOf(true) }
    val selectedHour = remember { mutableIntStateOf(notification.time.hour) }
    val selectedMinute = remember { mutableIntStateOf(notification.time.minute) }

    val timePickerState = key(is24hoursMode.value) {
        rememberTimePickerState(
            is24Hour = is24hoursMode.value,
            initialHour = selectedHour.intValue,
            initialMinute = selectedMinute.intValue
        )
    }

    var timeMode by rememberSaveable { mutableStateOf(initialTimeMode) }
    val icon = if (timeMode) Icons.Filled.Keyboard else Icons.Filled.AccessTime

    CustomDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Column (
            modifier = Modifier.padding(horizontal = spacing8, vertical = spacing10)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(timeMode){
                TimePicker(state = timePickerState,
                    colors = timePickerColors(color, contrastColor),
                    layoutType = TimePickerLayoutType.Vertical
                )
            }else{
                TimeInput(state = timePickerState,
                    colors = timePickerColors(color, contrastColor)
                )
            }

            Row (
                modifier = Modifier.fillMaxWidth().padding(start = spacing8, bottom = spacing8),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Switch(
                    checked = is24hoursMode.value,
                    onCheckedChange = {
                        selectedHour.intValue = timePickerState.hour
                        selectedMinute.intValue = timePickerState.minute
                        is24hoursMode.value = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = color,
                        checkedBorderColor = color
                    )
                )

                LabelLargeText(stringResource(R.string.add_habit_switch_mode),
                    modifier = Modifier.padding(start = spacing8),
                    fontSize = 16.sp)
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

        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,

        clockDialColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),

        clockDialSelectedContentColor = contrastColor,
        timeSelectorSelectedContentColor = contrastColor,

        periodSelectorSelectedContainerColor = color,
        periodSelectorSelectedContentColor = contrastColor,

        periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,

        periodSelectorBorderColor = MaterialTheme.colorScheme.outline,
    )
}