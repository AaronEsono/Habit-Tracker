package aeb.proyecto.habit.components.common.habitCards.weeklyCard.types.separateGoal

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.common.habitCards.utils.getSelected
import aeb.proyecto.habit.components.common.habitCards.utils.getTextTotal
import aeb.proyecto.habit.components.common.habitCards.utils.getUnitTitle
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.daysCompletedOnAWeek
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.getHabitDayFromADate
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import aeb.proyecto.ui.text.TitleSmallText
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.unit.sp
import java.math.RoundingMode
import java.time.LocalDate

//poner racha
// poner el otro modo de la semana


/**
 * A comprehensive card component that visualizes a weekly habit's progress.
 *
 * This card displays habit metadata, an overall completion indicator, and a
 * horizontal row of individual day status indicators ([SeparateWeeklyDay]).
 * It calculates the weekly completion status and reacts to changes in daily
 * progress via provided callbacks.
 *
 * @param modifier Modifier to be applied to the card layout.
 * @param startOfWeek The [LocalDate] marking the start of the week.
 * @param endOfWeek The [LocalDate] marking the end of the week.
 * @param selectedDate The specific [LocalDate] currently selected/focused.
 * @param habit Data wrapper containing habit definitions and progress records.
 * @param onClickCard Callback invoked when the main card area is clicked.
 * @param onClick Callback invoked for primary actions on specific days.
 * @param onLongClick Callback invoked for secondary actions on specific days.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeparateWeeklyCard(
    modifier: Modifier = Modifier,
    startOfWeek: LocalDate,
    endOfWeek: LocalDate,
    selectedDate: LocalDate,
    habit: HabitWithDailyHabit,
    onClickCard: (id: Long) -> Unit,
    onClick: (id:Long,date: LocalDate) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit
){

    // State derived from the selected day for the primary progress indicator
    val habitDaySelected = remember (habit){
        getSelected(selectedDate,habit.dailyHabits)
    }

    val currentProgress = remember(habitDaySelected) {
        try {
            habitDaySelected
                ?.goalDone
                ?.divide(habit.habit.goal, 4, RoundingMode.HALF_UP)
                ?.toFloat()
                ?.coerceIn(0f, 1f) ?: 0f
        } catch (e: ArithmeticException) {
            0f
        }
    }

    // Aggregated weekly progress for the footer summary
    val daysCompleted = remember(habit){
        daysCompletedOnAWeek(habit, startOfWeek)
    }

    // Animation state for the main habit progress icon
    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "progressAnimation"
    )

    // Control de estado visual
    val visualState = when {
        animatedProgress == 0f -> "add"
        animatedProgress >= 1f -> "check"
        else -> "progress"
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(spacing12)
            )
            .clickable(
                interactionSource = null,
                indication = null
            ){
                onClickCard(habit.habit.id)
            },
        shape = RoundedCornerShape(spacing12),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint
        )
    ) {
        Column (
            verticalArrangement = Arrangement.Center
        ){
            // Header Row: Icon, Title, Description, and Goal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing8, vertical = spacing10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Icons
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

                // Goals
                Column(
                    modifier = Modifier
                        .padding(end = spacing12, start = spacing6),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    LabelMediumText(
                        stringResource(
                            R.string.habit_week_goal_subtitle,
                            getTextTotal(habit.habit.goal, habit.habit.unit),
                            stringResource(getUnitTitle(habit.habit.unit, habit.habit.goal)),
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Progression
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(spacing8))
                        .background(MaterialTheme.colorScheme.background)
                        .combinedClickable(
                            onClick = { onClick(habit.habit.id, selectedDate) },
                            onLongClick = { onLongClick(habit.habit.id, selectedDate) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = visualState,
                        transitionSpec = {
                            fadeIn(tween(250)) togetherWith fadeOut(tween(250))
                        },
                        label = "Content Transition"
                    ) { state ->
                        when (state) {
                            "add" -> {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "add habit",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.fillMaxSize(0.8f),
                                )
                            }

                            "check" -> {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "check habit",
                                    tint = Color(habit.habit.color),
                                    modifier = Modifier.fillMaxSize(0.8f)
                                )
                            }

                            "progress" -> {
                                CircularProgressIndicator(
                                    progress = { animatedProgress },
                                    color = Color(habit.habit.color),
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.fillMaxSize(0.8f)
                                )
                            }
                        }
                    }
                }
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing8, start = spacing12, end = spacing12),
                horizontalArrangement = Arrangement.spacedBy(spacing12),
            ){
                repeat(7) {
                    SeparateWeeklyDay(
                        modifier = Modifier.weight(1f),
                        getHabitDayFromADate(
                            habit,
                            startOfWeek.plusDays(it.toLong())
                        ),
                        onClick = onClick
                    )
                }
            }

            TitleSmallText(
                stringResource(
                    R.string.habit_week_goal_title,
                    daysCompleted.toString(),
                    (habit.habit.typeHabit as TypeHabit.Weekly).numberDays.toString(),
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = spacing4,bottom = spacing12).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }

}