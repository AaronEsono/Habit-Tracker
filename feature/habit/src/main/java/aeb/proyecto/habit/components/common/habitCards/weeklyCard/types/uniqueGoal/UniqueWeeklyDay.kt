package aeb.proyecto.habit.components.common.habitCards.weeklyCard.types.uniqueGoal

import aeb.proyecto.habit.R
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.date.utils.getAvr
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.math.RoundingMode
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * A specialized day cell for habits with a unique (non-accumulative) weekly goal.
 *
 * Displays the day of the week and the calendar date, with specific visual
 * emphasis for the current day. Unlike its counterparts, this component displays
 * the raw `goalDone` value rather than a circular progress indicator.
 *
 * @param modifier Modifier for external layout customization.
 * @param habitWithDay Data wrapper containing habit configuration and daily progress.
 * @param onClick Callback triggered when the day cell is clicked, providing habit ID and date.
 */
@Composable
fun UniqueWeeklyDay(
    modifier: Modifier = Modifier,
    habitWithDay: HabitWithDay,
    onClick: (id:Long,date: LocalDate) -> Unit,
){

    val orientation = getOrientation()

    val dayOfWeek = remember (habitWithDay){
        habitWithDay.day.date.dayOfWeek ?: DayOfWeek.MONDAY
    }

    val isToday = remember(habitWithDay){
        habitWithDay.day.date == LocalDate.now()
    }


    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(
            interactionSource = null,
            indication = null
        ) {
            onClick(habitWithDay.habit.id, habitWithDay.day.date)
        }
    ){

        when(orientation){
            Orientation.Portrait -> {
                LabelLargeText(stringResource(getAvr(dayOfWeek)))
            }
            Orientation.Landscape -> {
                LabelMediumText(
                    stringResource(getAvr(dayOfWeek)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.padding(vertical = spacing2))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(
                    if (isToday) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                )
                .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                .aspectRatio(1f)
        ){
            LabelMediumText(
                habitWithDay.day.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.padding(top = spacing2))

        LabelSmallText(
            text = habitWithDay.day.goalDone.toString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.scrim
        )
    }

}