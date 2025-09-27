package aeb.proyecto.habit.components.common.habitCards.weeklyCard

import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.date.utils.getAvr
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import java.math.RoundingMode
import java.time.DayOfWeek

@Composable
fun WeeklyDay(
    habitWithDay: HabitWithDay
){

    val dayOfWeek = remember (habitWithDay){
        habitWithDay.day.date.dayOfWeek ?: DayOfWeek.MONDAY
    }

    val currentProgress = remember(habitWithDay) {
        try {
            habitWithDay
                .habit.goal
                .divide(habitWithDay.day.goalDone, 4, RoundingMode.HALF_UP)
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
            MaterialTheme.colorScheme.background
        },
        label = "colorAnim"
    )

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        LabelMediumText(stringResource(getAvr(dayOfWeek)))

        Spacer(modifier = Modifier.padding(vertical = spacing2))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(animatedColor)
                .size(40.dp)
        ){
            CircularProgressIndicator(
                progress = { animatedProgress },
                color = Color(habitWithDay.habit.color),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 2.dp,
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
                gapSize = 0.dp
            )

            AnimatedContent(
                targetState = animatedProgress,
                modifier = Modifier.align(Alignment.Center)
            ) { animatedProgressAnim ->
                when(animatedProgressAnim){
                    1f -> {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "check habit",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier
                                .fillMaxSize(0.8f)
                                .align(Alignment.Center)
                        )
                    }
                    else -> {
                        LabelMediumText(habitWithDay.day.date.dayOfMonth.toString(),
                            modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}