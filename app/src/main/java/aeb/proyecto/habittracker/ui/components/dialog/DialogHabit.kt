package aeb.proyecto.habittracker.ui.components.dialog

import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.dailyHabit.CardDailyHabit
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun DialogHabit(
    habitWithDailyHabit: HabitWithDailyHabit,
    onDismissRequest: () -> Unit,
    onUnitClick: (Long) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismissRequest() }, content = {
        CardDailyHabit(
            habitWithDailyHabit,
            isInDialog = true,
            onCancelClick = { onDismissRequest() },
            onClick = { onUnitClick(it) },
            onDeleteClick = {onDeleteClick()},
            onEditClick = { onEditClick() },
            onCalendarClick = { onCalendarClick() })
    })
}