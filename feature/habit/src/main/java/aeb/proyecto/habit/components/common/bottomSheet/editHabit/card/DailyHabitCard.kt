package aeb.proyecto.habit.components.common.bottomSheet.editHabit.card

import aeb.proyecto.habit.R
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleMediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DailyHabitCard(
    habit: Habit,
    onDismissBottomSheet: () -> Unit,
){

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = spacing12, end = spacing12, top = spacing4),
        verticalAlignment = Alignment.CenterVertically,
    ){

        Icon(
            habit.icon,
            contentDescription = "icon habit bottomSheet",
            modifier = Modifier
                .padding(start = spacing8)
                .size(35.dp),
            tint = Color(habit.color)
        )

        Spacer(modifier = Modifier.padding(horizontal = spacing6))

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TitleMediumText(
                text = habit.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            habit.description?.let { text ->
                if(text.isNotEmpty()){
                    Spacer(modifier = Modifier.padding(vertical = spacing1))

                    LabelMediumText(
                        text = text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        LabelLargeText(
            text = stringResource(
                R.string.habit_day_bt,
                habit.goal.toString(),
                if (habit.goal.toInt() <= 1) stringResource(habit.unit.title) else stringResource(habit.unit.titlePlural)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.padding(horizontal = spacing6))

        Icon(
            Icons.Filled.Clear,
            contentDescription = "icon habit bottomSheet",
            modifier = Modifier
                .size(35.dp)
                .clickable(
                    interactionSource = null,
                    indication = null
                ) {
                    onDismissBottomSheet()
                },
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

}