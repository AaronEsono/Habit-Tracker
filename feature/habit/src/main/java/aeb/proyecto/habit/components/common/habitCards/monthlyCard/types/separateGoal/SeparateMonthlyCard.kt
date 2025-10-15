package aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.separateGoal

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.daysCompletedOnAMonth
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.getDates
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.numberOfDaysToComplete
import aeb.proyecto.habit.components.common.habitCards.utils.getSelected
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.ui.calendar.content.CalendarContent
import aeb.proyecto.ui.calendar.content.CalendarDays
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
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
import java.time.DayOfWeek
import java.time.LocalDate


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeparateMonthlyCard(
    modifier: Modifier = Modifier,
    startOfMonth: LocalDate,
    firstDayOfWeek: DayOfWeek? = DayOfWeek.MONDAY,
    selectedDate: LocalDate,
    habit: HabitWithDailyHabit,
    onClick: (id:Long,date: LocalDate) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit
){

    val datesOfTheMonth: CalendarUIState<HabitWithDay> = remember(
        startOfMonth, firstDayOfWeek, habit
    ) {
        getDates(
            startOfMonth, firstDayOfWeek, habit
        )
    }

    val daysCompleted = remember(habit){
        daysCompletedOnAMonth(habit, startOfMonth)
    }

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

    // Para animar el progreso
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
            ),
        shape = RoundedCornerShape(spacing12),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint
        )
    ) {

        Column (
            verticalArrangement = Arrangement.Center
        ){
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
                            R.string.habit_week_goal_subtitle,
                            habit.habit.goal.toString(),
                            if(habit.habit.goal.toInt() <= 1)
                                stringResource(habit.habit.unit.title)
                            else
                                stringResource(habit.habit.unit.titlePlural)
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

            CalendarDays(
                Modifier.padding(start = spacing8, end = spacing8),
                startDay = firstDayOfWeek
            )

            CalendarContent(
                modifier = Modifier.padding(start = spacing12, end = spacing12, top = spacing2, bottom = spacing4),
                dates = datesOfTheMonth.dates
            ) { item, modifier ->
                if(item != null){
                    SeparateGoalMonthDayCard(
                        modifier = modifier,
                        day = item.dateOfMonth,
                        monthSelected = startOfMonth,
                        habitWithDay = item.data,
                        onClick = onClick,
                        onLongClick = onLongClick
                    )
                }
            }


            TitleSmallText(
                stringResource(
                    R.string.habit_week_goal_title,
                    daysCompleted.toString(),
                    numberOfDaysToComplete(
                        (habit.habit.typeHabit as TypeHabit.Monthly).numberTimes,
                        startOfMonth
                    ).toString()
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = spacing12).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}