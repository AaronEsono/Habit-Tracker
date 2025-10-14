package aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.uniqueGoal

import aeb.proyecto.habit.components.common.habitCards.monthlyCard.daysCompletedOnAMonth
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.getDates
import aeb.proyecto.habit.components.common.habitCards.utils.getSelected
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun UniqueMonthlyCard(
    modifier: Modifier = Modifier,
    startOfMonth: LocalDate,
    firstDayOfWeek: DayOfWeek? = DayOfWeek.MONDAY,
    selectedDate: LocalDate,
    habit: HabitWithDailyHabit,
    onClick: (id:Long,date: LocalDate) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit
){

    val datesOfTheMonth: CalendarUIState<HabitWithDay> = remember(
        startOfMonth, firstDayOfWeek, habit
    ) {
        getDates(
            startOfMonth, firstDayOfWeek, habit
        )
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
        Column (
            verticalArrangement = Arrangement.Center
        ){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing8, vertical = spacing10),
                verticalAlignment = Alignment.CenterVertically
            ) {

                //Icono
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(spacing8))
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        habit.habit.icon,
                        contentDescription = "habit icon",
                        tint = Color(habit.habit.color),
                        modifier = Modifier.fillMaxSize(0.75f)
                    )
                }

                // Nombre y descripcion
                Column(
                    modifier = Modifier
                        .padding(start = spacing12, end = spacing6)
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    LabelLargeText(
                        habit.habit.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 15.sp
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

            }

        }

    }
}