package aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.model.state.AddHabitScreenState
import aeb.proyecto.habittracker.ui.components.card.CardInfoAddHabit
import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitViewModel
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AddHabitNotifications(
    addHabitViewModel: AddHabitViewModel,
    uiState: AddHabitScreenState,
    notifications: List<Notification>
){

    val interactionSource = remember { MutableInteractionSource() }
    val height = remember {50.dp}

    BodySmallText(stringResource(R.string.add_habit_pick_date))

    CardInfoAddHabit(
        title = stringResource(R.string.add_habit_add_hour),
        icon = Icons.Filled.Add,
        finalIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        colorStartIcon = uiState.color,
        modifierCard = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(top = spacing8),
        modifierRow = Modifier
            .clickable {
                addHabitViewModel.openShowTimePicker(null)
            }
            .height(height)
            .padding(horizontal = spacing8, vertical = spacing4),
        modifierFinalIcon = Modifier.clickable(
            indication = null,
            interactionSource = interactionSource
        ) {
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
            colorStartIcon = uiState.color,
            colorFinalIcon = uiState.color,
            modifierCard = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(top = spacing8),
            modifierRow = Modifier
                .clickable {
                    addHabitViewModel.openShowTimePicker(notification)
                }
                .height(height)
                .padding(horizontal = spacing8, vertical = spacing4),
            modifierFinalIcon = Modifier.clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                addHabitViewModel.deleteNotificacion(notification)
            }
        )
    }
}