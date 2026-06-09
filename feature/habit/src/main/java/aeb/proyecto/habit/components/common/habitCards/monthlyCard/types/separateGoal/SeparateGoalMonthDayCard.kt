package aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.separateGoal

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.math.RoundingMode
import java.time.LocalDate

// Poner modo desactivado

/**
 * A specialized calendar cell component that visualizes daily habit progress using a circular indicator.
 *
 * This component displays the day of the month and uses a [CircularProgressIndicator]
 * to show completion percentage. It includes state-based animations for color transitions
 * (based on goal completion) and content switching (showing a checkmark upon completion).
 *
 * @param modifier Modifier to be applied to the component layout.
 * @param day The [LocalDate] corresponding to this calendar cell.
 * @param monthSelected The month currently being viewed, used to dim days outside this range.
 * @param habitWithDay Data model containing the habit definition and daily progress.
 * @param horizontalPadding Horizontal spacing applied to the component.
 * @param onClick Callback triggered on a standard click, returning the habit ID and the date.
 * @param onLongClick Callback triggered on a long click, returning the habit ID and the date.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeparateGoalMonthDayCard(
    modifier: Modifier = Modifier,
    day: LocalDate? = LocalDate.now(),
    monthSelected: LocalDate = LocalDate.now(),
    habitWithDay: HabitWithDay?,
    horizontalPadding: Dp = spacing4,
    onClick: (id:Long,date: LocalDate) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit
){

    val habitWithDay = remember(habitWithDay){
        habitWithDay?: HabitWithDay()
    }

    val notInMonth = remember (day){
        monthSelected.month != day?.month
    }

    val isToday = remember(day){
        day == LocalDate.now()
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

    Box(
        modifier = modifier
            .padding(horizontal = horizontalPadding, vertical = spacing2)
            .clip(CircleShape)
            .alpha(if (notInMonth) 0.3f else 1f)
            .background(animatedColor)
            .aspectRatio(1f)
            .combinedClickable(
                onClick = { if(!notInMonth) {onClick(habitWithDay.habit.id, day ?: LocalDate.now())} },
                onLongClick = { if(!notInMonth){onLongClick(habitWithDay.habit.id, day ?: LocalDate.now())} }
            ),
    ){
        if(!notInMonth){
            CircularProgressIndicator(
                progress = { animatedProgress },
                color = Color(habitWithDay.habit.color),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 2.dp,
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
                gapSize = 0.dp
            )
        }

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
                    LabelMediumText(day?.dayOfMonth.toString(),
                        modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}