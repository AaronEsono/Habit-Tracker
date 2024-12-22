package aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.model.state.AddHabitScreenState
import aeb.proyecto.habittracker.ui.components.card.CardInfoAddHabit
import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitViewModel
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun AddHabitNotifications(
    addHabitViewModel: AddHabitViewModel,
    uiState: AddHabitScreenState,
    notifications: List<Notification>
){

    BodySmallText(stringResource(R.string.add_habit_pick_date))

    CardInfoAddHabit(
        title = stringResource(R.string.add_habit_add_hour),
        icon = Icons.Filled.Add,
        finalIcon = Icons.Filled.KeyboardArrowRight,
        color = uiState.color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing8),
        onClick = {
            addHabitViewModel.openShowTimePicker(null)
        },
        onDelete = {
            addHabitViewModel.openShowTimePicker(null)
        }
    )

    notifications.forEach { notification ->
        CardInfoAddHabit(
            title = stringResource(
                R.string.add_habit_pick_time,
                notification.hour,
                if (notification.minute < 10) "0${notification.minute}" else notification.minute
            ),
            icon = Icons.Filled.AddAlert,
            finalIcon = Icons.Filled.Delete,
            color = uiState.color,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacing8),
            onClick = {
                addHabitViewModel.openShowTimePicker(notification)
            },
            onDelete = { addHabitViewModel.deleteNotificacion(notification) },
            colorInFinalIcon = true
        )
    }
}