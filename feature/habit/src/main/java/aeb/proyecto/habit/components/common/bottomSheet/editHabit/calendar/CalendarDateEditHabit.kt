package aeb.proyecto.habit.components.common.bottomSheet.editHabit.calendar

import aeb.proyecto.habit.components.common.habitCards.utils.getTextTotal
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.BodySmallText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            Box(
                modifier = Modifier
                    .padding(bottom = spacing1)
                    .clip(RoundedCornerShape(spacing12))
                    .fillMaxWidth(0.85f)
                    .background(if(isToday) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.background)
                    .border(1.dp, if(isToday) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface, RoundedCornerShape(spacing12))
                    .align(Alignment.BottomCenter)
            ){
                Row (
                    modifier = Modifier.padding(horizontal = spacing4).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Box(
                        modifier = Modifier
                            .padding(spacing1)
                            .clip(CircleShape)
                            .size(5.dp)
                            .background(Color(habitWithDay.habit.color))
                    ){}

                    Spacer(modifier = Modifier.padding(horizontal = spacing1))

                    LabelSmallText(
                        text = getTextTotal(habitWithDay.day.goalDone,habitWithDay.habit.unit),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 9.sp,
                        color = if(isToday) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}