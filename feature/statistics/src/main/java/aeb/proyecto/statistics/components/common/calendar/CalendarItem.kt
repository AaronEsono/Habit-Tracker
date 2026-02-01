package aeb.proyecto.statistics.components.common.calendar

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.statistics.utils.getTextTotal
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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

// Poner la cantidad exacta, si se ha hecho, en los dias correspondientes

@Composable
fun CalendarItem(
    modifier: Modifier = Modifier,
    day: LocalDate,
    monthSelected: LocalDate = LocalDate.now(),
    habitWithDay: HabitWithDay? = null
){

    val goalDone = remember (habitWithDay){
        habitWithDay?.day?.goalDone?.toFloat() ?: 0f
    }

    val colorHabit = remember(habitWithDay) {
        val habit = habitWithDay?.habit
        val goal = habit?.goal?.toFloat() ?: 0f
        val baseColor = Color(habit?.color ?: 0)


        if (goalDone < goal)
            baseColor.copy(alpha = 0.5f)
        else
            baseColor
    }

    val isInMonth = remember (day,monthSelected){
        day.month == monthSelected.month
    }

    val dayWithGoal = remember (habitWithDay){
        goalDone > 0
    }

    val color = if (goalDone <= 0) MaterialTheme.colorScheme.primary else colorHabit

    Box(
        modifier = modifier
            .alpha(if (isInMonth) 1f else 0.20f)
            .aspectRatio(1f)
            .border(if (dayWithGoal) 2.dp else 0.dp, color, RoundedCornerShape(spacing12))
            .clip(RoundedCornerShape(spacing12))
            .background(color),
        contentAlignment = Alignment.Center
    ){
        LabelMediumText(
            day.dayOfMonth.toString(),
            color = getContrastColor(color)
        )

        if(goalDone > 0){
            Box(
                modifier = Modifier
                    .padding(bottom = spacing1)
                    .clip(RoundedCornerShape(spacing12))
                    .fillMaxWidth(0.8f)
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(spacing12))
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
                            .background(Color(habitWithDay?.habit?.color ?: 0))
                    ){}

                    Spacer(modifier = Modifier.padding(horizontal = spacing1))

                    LabelSmallText(
                        text = getTextTotal(habitWithDay?.day?.goalDone,habitWithDay?.habit?.unit ?: UnitHabit.SESSIONS),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

}