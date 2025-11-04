package aeb.proyecto.habit.components.common.bottomSheet.editHabit.card

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DailyHabitCard(
    habit: Habit,
    onDismissBottomSheet: () -> Unit,
){

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = spacing8, end = spacing8, top = spacing6),
        horizontalArrangement = Arrangement.Center
    ){

        Icon(
            habit.icon,
            contentDescription = "icon habit bottomSheet",
            modifier = Modifier.padding(start = spacing8),
            tint = Color(habit.color)
        )

    }

}