package aeb.proyecto.habit.components.common.habitCards.weeklyCard.types.separateGoal

import aeb.proyecto.habit.R
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.date.utils.getAvr
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.math.RoundingMode
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun SeparateWeeklyDay(
    modifier: Modifier = Modifier,
    habitWithDay: HabitWithDay,
    onClick: (id:Long,date: LocalDate) -> Unit,
){

    val dayOfWeek = remember (habitWithDay){
        habitWithDay.day.date.dayOfWeek ?: DayOfWeek.MONDAY
    }

    val isToday = remember(habitWithDay){
        habitWithDay.day.date == LocalDate.now()
    }

    val currentProgress = remember(habitWithDay) {
        try {
            habitWithDay
                .day.goalDone
                .divide(habitWithDay.habit.goal, 4, RoundingMode.HALF_UP)
                ?.toFloat()
                ?.coerceIn(0f, 1f) ?: 0f
        } catch (e: ArithmeticException) {
            0f
        }
    }

    // Para animar el progreso
    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "progressAnimation"
    )

    val animatedColor by animateColorAsState(
        targetValue = if (currentProgress >= 1f) {
            Color(habitWithDay.habit.color).copy(alpha = 0.5f)
        } else {
            if(isToday){
                MaterialTheme.colorScheme.surfaceVariant
            }else{
                MaterialTheme.colorScheme.background
            }
        },
        label = "colorAnim"
    )

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(
            interactionSource = null,
            indication = null
        ) {
            onClick(habitWithDay.habit.id, habitWithDay.day.date)
        }
    ){

        LabelLargeText(
            stringResource(getAvr(dayOfWeek))
        )

        Spacer(modifier = Modifier.padding(vertical = spacing2))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(animatedColor)
                .aspectRatio(1f)
        ){
            CircularProgressIndicator(
                progress = { animatedProgress },
                color = Color(habitWithDay.habit.color),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                gapSize = 0.dp
            )

            AnimatedContent(
                targetState = animatedProgress,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) { animatedProgressAnim ->
                when(animatedProgressAnim){
                    1f -> {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "check habit",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier
                                .fillMaxSize(0.5f)
                                .align(Alignment.Center)
                        )
                    }
                    else -> {
                        LabelMediumText(
                            habitWithDay.day.date.dayOfMonth.toString(),
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = spacing4))

        LabelSmallText(
            stringResource(
                R.string.habit_week_day_goal,
                habitWithDay.day.goalDone,
                habitWithDay.habit.goal
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.scrim
        )
    }
}