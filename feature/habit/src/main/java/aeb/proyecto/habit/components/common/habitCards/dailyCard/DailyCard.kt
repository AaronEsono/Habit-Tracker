package aeb.proyecto.habit.components.common.habitCards.dailyCard

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.common.habitCards.utils.getSelected
import aeb.proyecto.habit.components.common.habitCards.utils.getTextTotal
import aeb.proyecto.habit.components.common.habitCards.utils.getUnitTitle
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.RoundingMode
import java.time.LocalDate

/**
 * A display card representing a daily habit and its progress for a specific date.
 *
 * This component visualizes habit completion status, manages progress calculation
 * using [BigDecimal] logic, and handles interaction events like clicks and
 * long-clicks for daily habit logs.
 *
 * @param modifier Modifier to be applied to the card container.
 * @param selectedDate The [LocalDate] for which the habit progress is displayed.
 * @param habit The data wrapper containing the [Habit] definition and its associated [DailyHabit] list.
 * @param onClickCard Callback invoked when the main card body is clicked, providing the habit ID.
 * @param onClick Callback invoked on a standard click, providing the habit ID and date.
 * @param onLongClick Callback invoked on a long click, providing the habit ID and date.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DailyCard(
    modifier: Modifier = Modifier,
    selectedDate:LocalDate,
    habit: HabitWithDailyHabit,
    onClickCard: (id: Long) -> Unit,
    onClick: (id:Long,date: LocalDate) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit
){

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

    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "progressAnimation"
    )

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing8, vertical = spacing10),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Icon display container
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

            // Name and description section
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

            // Goal summary section
            Column(
                modifier = Modifier
                    .padding(end = spacing12, start = spacing6),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                LabelMediumText(
                    stringResource(
                        R.string.habit_unit_card,
                        getTextTotal(habitDaySelected?.goalDone, habit.habit.unit),
                        getTextTotal(habit.habit.goal, habit.habit.unit)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                LabelSmallText(
                    stringResource(
                        getUnitTitle(habit.habit.unit, habit.habit.goal)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Progress interaction area
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(spacing8))
                    .background(MaterialTheme.colorScheme.background)
                    .combinedClickable(
                        onClick = { onClick(habit.habit.id, selectedDate) },
                        onLongClick = { onLongClick(habit.habit.id, selectedDate) }
                    )
                    .testTag("habit_progress_area"),
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
    }
}