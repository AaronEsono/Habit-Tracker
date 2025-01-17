package aeb.proyecto.habittracker.ui.screens.habits.habitsComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.state.HabitsScreenState
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetCalendar
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetChoseSteps
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetGeneral
import aeb.proyecto.habittracker.ui.components.dialog.DialogHabit
import aeb.proyecto.habittracker.ui.screens.habits.HabitsViewModel
import aeb.proyecto.habittracker.utils.cancelAlarm
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun HabitScreenStates(
    uiState: HabitsScreenState,
    habitsViewModel: HabitsViewModel,
    onEditClick: (Long) -> Unit
) {
    val context = LocalContext.current

    if (uiState.showDialog) {
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

    if (uiState.showGeneralDx) {
        BottomSheetGeneral(
            onDismiss = { habitsViewModel.closeGeneralDx() },
            colorButton = habitsViewModel.getColor(),
            title = R.string.general_dx_attention,
            subtitle = uiState.textAttention,
            showCancel = true,
            onCancel = { habitsViewModel.closeGeneralDx() },
            onAccept = {
                habitsViewModel.generalDxLogic() { id ->
                    id.forEach {
                        cancelAlarm(context, it)
                    }
                }
            }
        )
    }

    if (uiState.showCalendar) {
        BottomSheetCalendar(
            onDismiss = { habitsViewModel.closeCalendar() },
            color = habitsViewModel.getColor(),
            habit = habitsViewModel.getHabit()
        ) { date ->
            habitsViewModel.choseStep(habitsViewModel.getId(), date)
        }
    }

    if (uiState.showSteps) {
        BottomSheetChoseSteps(
            onDismiss = { habitsViewModel.closeSteps() },
            color = habitsViewModel.getColor(),
            titleUnit = habitsViewModel.getTitle(),
            restDays = habitsViewModel.getRestSteps(habitsViewModel.getId(), uiState.date),
            onAccept = { text ->
                habitsViewModel.plusOneHabit(
                    habitsViewModel.getId(),
                    uiState.date,
                    text.toInt()
                )
            }
        )
    }
}