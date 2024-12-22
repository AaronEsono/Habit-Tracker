package aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.state.AddHabitScreenState
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetGeneral
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetPickUnit
import aeb.proyecto.habittracker.ui.components.timePicker.TimePickerHabit
import aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitViewModel
import androidx.compose.runtime.Composable

@Composable
fun AddHabitScreenState(
    addHabitViewModel: AddHabitViewModel,
    uiState: AddHabitScreenState
){

    if (uiState.showGeneralDx) {
        BottomSheetGeneral(
            color = uiState.color,
            titleAccept = R.string.buttons_accept,
            iconAccept = R.drawable.ic_check,
            title = R.string.general_dx_attention,
            subtitle = uiState.attentionText,
            onDismiss = { addHabitViewModel.closeGeneralDx() }
        )
    }

    if (uiState.showBottomSheet) {
        BottomSheetPickUnit(
            color = uiState.color,
            units = uiState.unitPicked,
            onDismiss = { addHabitViewModel.closeBottomSheet() },
            onConfirm = { seletectedUnit ->
                addHabitViewModel.setUnit(seletectedUnit)
            })
    }

    if (uiState.showTimePicker) {
        TimePickerHabit(
            color = uiState.color,
            onDismiss = { addHabitViewModel.closeShowTimePicker() },
            onConfirm = { notification ->
                addHabitViewModel.insertNotification(notification)
            },
            notification = addHabitViewModel.getNotification()
        )
    }
}
