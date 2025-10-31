package aeb.proyecto.habit.components.common.habitCards.weeklyCard.types.uniqueGoal

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.common.habitCards.utils.getSelected
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.calculatePercentage
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.getHabitDayFromADate
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.timesCompletedInAEntireWeek
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.types.separateGoal.SeparateWeeklyDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UniqueWeeklyCard(
    modifier: Modifier = Modifier,
    startOfWeek: LocalDate,
    endOfWeek: LocalDate,
    selectedDate: LocalDate,
    habit: HabitWithDailyHabit,
    onClickCard: (id: Long) -> Unit,
    onClick: (id:Long,date: LocalDate) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit
){

    val goalWeekCompleted = timesCompletedInAEntireWeek(habit, startOfWeek)

    val animatedProgressLinear by animateFloatAsState(
        targetValue = calculatePercentage(goalWeekCompleted,habit.habit.goal),
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
                            R.string.habit_week_goal_title_unique,
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

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing8, start = spacing12, end = spacing12),
                horizontalArrangement = Arrangement.spacedBy(spacing12),
            ){
                repeat(7) {
                    UniqueWeeklyDay(
                        modifier = Modifier.weight(1f),
                        getHabitDayFromADate(
                            habit,
                            startOfWeek.plusDays(it.toLong())
                        ),
                        onClick = onClick
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
                    goalWeekCompleted.toString(),
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