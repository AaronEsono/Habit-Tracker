package aeb.proyecto.habit.components.common.bottomSheet.editHabit.calendar

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarDateEditHabit(
    modifier: Modifier = Modifier,
    day: LocalDate? = LocalDate.now(),
    monthSelected: LocalDate = LocalDate.now(),
    habitWithDay: HabitWithDay?,
    onClick: (id:Long,date: LocalDate) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit
){

    val habitWithDay = remember(habitWithDay){
        habitWithDay?: HabitWithDay()
    }

    val isToday = remember(day){
        day == LocalDate.now()
    }

    val notInMonth = remember (day){
        monthSelected.month != day?.month
    }

    Box(
        modifier = modifier
            .padding(vertical = spacing2)
            .clip(RoundedCornerShape(spacing12))
            .aspectRatio(1f)
            .alpha(if (notInMonth) 0.3f else 1f)
            .background(if(isToday) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.background)
            .combinedClickable(
                onClick = {
                    if (!notInMonth) {
                        onClick(habitWithDay.habit.id, day ?: LocalDate.now())
                    }
                },
                onLongClick = {
                    if (!notInMonth) {
                        onLongClick(habitWithDay.habit.id, day ?: LocalDate.now())
                    }
                }
            ),
    ) {

        LabelMediumText(
            day?.dayOfMonth.toString(),
            modifier = Modifier.align(Alignment.Center),
            color = if(isToday) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface
        )


        if(habitWithDay.day.goalDone.toFloat() > 0){
            LabelSmallText(
                text = "Hola",
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = spacing1),
            )
        }
    }
}