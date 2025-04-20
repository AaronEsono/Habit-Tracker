package aeb.proyecto.habit.components.card.habit

import aeb.proyecto.habit.R
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardHabit(
    modifier: Modifier = Modifier,
    selectedDate:LocalDate,
    habit: HabitWithDailyHabit
) {

    val habitDaySelected = remember (habit){
        getSelected(selectedDate,habit.dailyHabits)
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(spacing12)
            ),
        shape = RoundedCornerShape(spacing12),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing8, vertical = spacing8),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(spacing8))
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    habit.habit.icon,
                    contentDescription = "habit icon",
                    tint = Color(habit.habit.color),
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = spacing12, end = spacing6)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                LabelLargeText(
                    habit.habit.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                habit.habit.description?.let { description ->
                    if (description.isNotEmpty()) {
                        LabelSmallText(
                            description,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(end = spacing12, start = spacing6),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabelMediumText(
                    stringResource(
                        R.string.habit_unit_card,
                        habitDaySelected?.goalDone ?: "0",
                        habit.habit.goal
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                LabelSmallText(
                    stringResource(
                        getUnitTitle(habit.habit.unit, habit.habit.goal)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            CustomRipple {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(spacing8))
                        .background(MaterialTheme.colorScheme.background)
                        .combinedClickable(
                            onClick = {  },
                            onLongClick = {  }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "add unit icon",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxSize(0.8f)
                    )
                }
            }
        }
    }

}

fun getUnitTitle(unitHabit: UnitHabit, timesDone: BigDecimal): Int {
    return if (timesDone == BigDecimal(1)) unitHabit.title else unitHabit.titlePlural
}

fun getSelected(dateSelected:LocalDate,dailyHabits:List<HabitDay>):HabitDay?{
    return dailyHabits.find {date -> date.date == dateSelected}
}