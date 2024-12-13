package aeb.proyecto.habittracker.ui.screens.habits

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.state.HabitsScreenState
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetCalendar
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetGeneral
import aeb.proyecto.habittracker.ui.components.dialog.DialogHabit
import androidx.compose.runtime.Composable
import java.time.LocalDate

@Composable
fun HabitScreenStates(
    uiState: HabitsScreenState,
    habitsViewModel: HabitsViewModel,
    onEditClick: (Long) -> Unit
) {

    if(uiState.showDialog){
        DialogHabit(
            habitWithDailyHabit = habitsViewModel.getHabit(),
            onDismissRequest = { habitsViewModel.closeDialog() },
            onUnitClick = { habitsViewModel.choseStep(habitsViewModel.getId()) },
            onDeleteClick = { habitsViewModel.showGeneralDx() },
            onEditClick = {
                habitsViewModel.closeDialog()
                onEditClick(habitsViewModel.getId())
            },
            onCalendarClick = { habitsViewModel.openCalendar() }
        )
    }

    if(uiState.showGeneralDx){
        BottomSheetGeneral(
            onDismiss = { habitsViewModel.closeGeneralDx() },
            color = habitsViewModel.getColor(),
            title = R.string.general_dx_attention,
            subtitle = R.string.general_dx_subtitle_delete,
            showCancel = true,
            onCancel = { habitsViewModel.closeGeneralDx() },
            onAccept = { habitsViewModel.deleteUnit(habitsViewModel.getId()) }
        )
    }

    if(uiState.showCalendar){
        BottomSheetCalendar(
            onDismiss = { habitsViewModel.closeCalendar() },
            color = habitsViewModel.getColor(),
            habit = habitsViewModel.getHabit()
        ) { date ->
            habitsViewModel.choseStep(habitsViewModel.getId(), date)
        }
    }
}