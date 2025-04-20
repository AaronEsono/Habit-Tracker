package aeb.proyecto.habit.components.card.habit

import aeb.proyecto.habit.R
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.Row
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.math.BigDecimal

@Composable
fun CardHabit2(
    modifier: Modifier = Modifier,
    habit: HabitWithDailyHabit
) {

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.secondaryContainer,
                RoundedCornerShape(spacing12)
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
                    .background(Color(habit.habit.color)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    habit.habit.icon,
                    contentDescription = "habit icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxSize(0.6f)
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
                        "0", habit.habit.goal
                    )
                )

                LabelSmallText(
                    stringResource(
                        getUnitTitle2(habit.habit.unit, habit.habit.goal)
                    )
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(spacing8))
                    .background(Color(habit.habit.color))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "add unit icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }
        }
    }

}

fun getUnitTitle2(unitHabit: UnitHabit, timesDone: BigDecimal): Int {
    return if (timesDone == BigDecimal(1)) unitHabit.title else unitHabit.titlePlural
}