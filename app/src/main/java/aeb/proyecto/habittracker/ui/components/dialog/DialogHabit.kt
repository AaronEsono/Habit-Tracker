package aeb.proyecto.habittracker.ui.components.dialog

import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.card.CardDailyHabit
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogHabit(
    habitWithDailyHabit: HabitWithDailyHabit,
    onDismissRequest: () -> Unit,
    onUnitClick: (Long) -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismissRequest() }, content = {
        CardDailyHabit(
            habitWithDailyHabit,
            isInDialog = true,
            onCancelClick = { onDismissRequest() },
            onClick = { onUnitClick(it) },
            onDeleteClick = {onDeleteClick()})
    })
}