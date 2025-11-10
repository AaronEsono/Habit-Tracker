package aeb.proyecto.habit.components.common.bottomSheet.editHabit.card.recurring

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.common.habitCards.utils.getTextTotal
import aeb.proyecto.habit.components.common.habitCards.utils.getUnitTitle
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing5
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleMediumText
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun RecurringHabitCard(
    habit: Habit,
    onDismissBottomSheet: (typeBottomSheet: TypeBottomSheet) -> Unit,
){

    val typeHabit = remember(habit) {
        habit.typeHabit as TypeHabit.Recurring
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = spacing12, end = spacing12, top = spacing2),
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

        Spacer(modifier = Modifier.padding(horizontal = spacing5))

        Column (
            verticalArrangement = Arrangement.Center
        ){
            TitleMediumText(
                text = habit.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            LabelMediumText(
                if(typeHabit.interval == 1){
                    stringResource(R.string.habit_recurring_singular_bt,
                        getTextTotal(habit.goal, habit.unit),
                        stringResource(getUnitTitle(habit.unit, habit.goal)),
                    )
                }else {
                    stringResource(
                        R.string.habit_recurring_plural_bt,
                        getTextTotal(habit.goal, habit.unit),
                        stringResource(getUnitTitle(habit.unit, habit.goal)),
                        typeHabit.interval.toString()
                    )
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = spacing1)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            Icons.Filled.Clear,
            contentDescription = "icon habit bottomSheet",
            modifier = Modifier
                .size(35.dp)
                .clickable(
                    interactionSource = null,
                    indication = null
                ) {
                    onDismissBottomSheet(TypeBottomSheet.EditHabit())
                },
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

}