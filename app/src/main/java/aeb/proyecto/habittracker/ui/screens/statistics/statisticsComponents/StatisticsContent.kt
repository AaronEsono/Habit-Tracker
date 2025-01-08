package aeb.proyecto.habittracker.ui.screens.statistics.statisticsComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.radioButton.RadioButtonStatistics
import aeb.proyecto.habittracker.ui.screens.statistics.StatisticsViewModel
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsContent(
    habits: List<Habit>,
    statisticsViewModel: StatisticsViewModel
) {

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(habits[0]) }

    LaunchedEffect(true) {
        statisticsViewModel.getDailyHabits(selectedOption.id)
    }

    val dailyHabits = statisticsViewModel.dailyHabits.collectAsState().value
    val showData = statisticsViewModel.showData.collectAsState().value
    val statisticsState = statisticsViewModel.statisticsState.collectAsState().value

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
            StatisticsHeader(selectedOption)
        }

        item {
            StatisticsCalendar(
                habitWithDailyHabit = HabitWithDailyHabit(
                    habit = selectedOption,
                    dailyHabits = dailyHabits.toMutableList()
                )
            )
        }

        item {
            StatisticsCard(modifier = Modifier.fillMaxWidth(), statisticsState.timesCompleted, title = R.string.statistics_screen_times_completed)
            Row {
                StatisticsCard(modifier = Modifier.weight(1f), statisticsState.streak, title = R.string.statistics_screen_streak)
                StatisticsCard(modifier = Modifier.weight(1f), statisticsState.bestStreak.times, title = R.string.statistics_screen_best_streak)
            }
        }
    }
}