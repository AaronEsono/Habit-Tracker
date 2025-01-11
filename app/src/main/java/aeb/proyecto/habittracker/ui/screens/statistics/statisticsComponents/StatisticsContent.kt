package aeb.proyecto.habittracker.ui.screens.statistics.statisticsComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.radioButton.RadioButtonStatistics
import aeb.proyecto.habittracker.ui.screens.statistics.StatisticsViewModel
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsContent(
    habits: List<Habit>,
    statisticsViewModel: StatisticsViewModel
) {

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(habits[0]) }

    LaunchedEffect (true){
        statisticsViewModel.getDailyHabits(selectedOption.id)
    }

    val dailyHabits = statisticsViewModel.dailyHabits.collectAsState().value
    val staticticsState = statisticsViewModel.statisticsState.collectAsState().value

    LazyColumn(modifier = Modifier.fillMaxSize()) {

        stickyHeader {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(spacing8)
                    .selectableGroup()
                    .wrapContentHeight()
            ) {
                items(habits) {
                    RadioButtonStatistics(it, selectedOption) { habit ->
                        if (habit != selectedOption) {
                            onOptionSelected(habit)
                            statisticsViewModel.getDailyHabits(habit.id)
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .height(1.5.dp)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        }

        item {
            AnimatedContent(targetState = selectedOption, label = "",
                transitionSpec = {
                    slideInVertically { height -> -height } togetherWith slideOutVertically { height -> height }
                }) { selectedOption ->
                StatisticsHeader(selectedOption)
            }
        }

        item {
            AnimatedContent(targetState = selectedOption, label = "",
                transitionSpec = {
                    scaleIn(tween(durationMillis = 300)) togetherWith scaleOut(tween(durationMillis = 300))
                }) { selectedOption ->
                StatisticsCalendar(
                    habitWithDailyHabit = HabitWithDailyHabit(
                        habit = selectedOption,
                        dailyHabits = dailyHabits.toMutableList()
                    )
                )
            }
        }

        item {
            AnimatedContent(targetState = selectedOption, label = "",
                transitionSpec = {
                    scaleIn(tween(durationMillis = 300)) togetherWith scaleOut(tween(durationMillis = 300))
                }) {
                Column {
                    StatisticsCard(
                        modifier = Modifier.fillMaxWidth(),
                        staticticsState.timesCompleted,
                        title = R.string.statistics_screen_times_completed
                    )
                    Row {
                        StatisticsCard(
                            modifier = Modifier.weight(1f),
                            staticticsState.streak,
                            title = R.string.statistics_screen_streak
                        )
                        StatisticsCard(
                            modifier = Modifier.weight(1f),
                            staticticsState.bestStreak.times,
                            title = R.string.statistics_screen_best_streak
                        )
                    }
                }
            }
        }
    }
}