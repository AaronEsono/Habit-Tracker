package aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.uniqueGoal

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.getDates
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.timesCompletedInAEntireMonth
import aeb.proyecto.habit.components.common.habitCards.utils.getTextTotal
import aeb.proyecto.habit.components.common.habitCards.utils.getUnitTitle
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.calculatePercentage
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.calendar.content.CalendarContent
import aeb.proyecto.ui.calendar.content.CalendarDays
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
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
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * A monthly calendar card component tailored for habits with a unique monthly aggregate goal.
 *
 * This component visualizes the overall progress of a habit throughout a specific month
 * by calculating the total completed amount and displaying it via a linear progress
 * indicator. It manages the calendar grid and user interactions for individual dates.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param startOfMonth The [LocalDate] representing the first day of the month to display.
 * @param firstDayOfWeek The [DayOfWeek] used as the start of the week, defaulting to Monday.
 * @param horizontalDayPadding Spacing applied horizontally between individual day cells.
 * @param selectedDate The currently highlighted [LocalDate] in the calendar view.
 * @param habit The data wrapper containing the habit definition and all daily progress records.
 * @param onClickCard Callback invoked when the main card container is clicked.
 * @param onClick Callback invoked for a primary action on a specific date cell.
 * @param onLongClick Callback invoked for a long press on a specific date cell.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UniqueMonthlyCard(
    modifier: Modifier = Modifier,
    startOfMonth: LocalDate,
    firstDayOfWeek: DayOfWeek? = DayOfWeek.MONDAY,
    horizontalDayPadding: Dp = spacing4,
    selectedDate: LocalDate,
    habit: HabitWithDailyHabit,
    onClickCard: (id: Long) -> Unit,
    onClick: (id: Long, date: LocalDate) -> Unit,
    onLongClick: (id: Long, date: LocalDate) -> Unit
) {

    val goalMonthCompleted = timesCompletedInAEntireMonth(habit, startOfMonth)

    val animatedProgressLinear by animateFloatAsState(
        targetValue = calculatePercentage(goalMonthCompleted, habit.habit.goal),
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "progressAnimation"
    )

    // Control de estado visual
    val visualState = when {
        animatedProgressLinear == 0f -> "add"
        animatedProgressLinear >= 1f -> "check"
        else -> "progress"
    }

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
            )
            .clickable(
                indication = null,
                interactionSource = null
            ){
                onClickCard(habit.habit.id)
            },
        shape = RoundedCornerShape(spacing12),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {

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

                // Metas
                Column(
                    modifier = Modifier
                        .padding(end = spacing12, start = spacing6),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    LabelMediumText(
                        stringResource(
                            R.string.habit_month_unique_bt,
                            getTextTotal(habit.habit.goal, habit.habit.unit),
                            stringResource(getUnitTitle(habit.habit.unit, habit.habit.goal)),
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Progresion
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
                                    progress = { animatedProgressLinear },
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

            CalendarDays(
                Modifier.padding(start = spacing8, end = spacing8),
                startDay = firstDayOfWeek
            )

            CalendarContent(
                modifier = Modifier.padding(start = spacing12, end = spacing12, top = spacing2, bottom = spacing4),
                dates = datesOfTheMonth.dates
            ) { item, modifier ->
                if(item != null){
                    UniqueGoalMonthDayCard(
                        modifier = modifier,
                        day = item.dateOfMonth,
                        monthSelected = startOfMonth,
                        horizontalPadding = horizontalDayPadding,
                        habitWithDay = item.data,
                        onClick = onClick,
                        onLongClick = onLongClick
                    )
                }
            }

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = spacing16, end = spacing16, bottom = spacing8, top = spacing4)
                    .height(12.dp),
                progress = {animatedProgressLinear},
                color = Color(habit.habit.color),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                gapSize = (-10).dp,
                drawStopIndicator = {}
            )

            LabelLargeText(
                text = stringResource(
                    R.string.habit_week_unique_goal_completed,
                    goalMonthCompleted.toString(),
                    habit.habit.goal.toString(),
                    if(habit.habit.goal.toInt() <= 1){
                        stringResource(habit.habit.unit.title)
                    }else{
                        stringResource(habit.habit.unit.titlePlural)
                    }
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = spacing8),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}