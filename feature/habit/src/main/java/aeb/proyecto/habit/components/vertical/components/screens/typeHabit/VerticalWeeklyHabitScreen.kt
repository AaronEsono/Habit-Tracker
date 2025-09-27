package aeb.proyecto.habit.components.vertical.components.screens.typeHabit

import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.WeeklyCard
import aeb.proyecto.habit.utils.cardHabitPadding
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun VerticalWeeklyHabitScreen(
    weekTimeRange: TimeRangeUiState.Weekly,
    habits: List<HabitWithDailyHabit>,
    onLongClick: (id:Long,date: LocalDate) -> Unit,
    onClick: (id: Long, date: LocalDate) -> Unit
){

    val visibleItems = remember { mutableStateListOf<Int>() }

    // Hash de los IDs de los hábitos (solo cambia si cambian los hábitos que ves)
    val habitsHash = habits.map { it.habit.id }.hashCode()

    LaunchedEffect(habitsHash) {
        visibleItems.clear()
        habits.indices.forEach { index ->
            delay(50)
            visibleItems.add(index)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacing8)
    ) {
        items(
            count = habits.size,
            key = { habits[it].habit.id }
        ) { index ->

            AnimatedVisibility(
                visible = visibleItems.contains(index),
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut()
            ) {
                WeeklyCard(
                    habit = habits[index],
                    modifier = Modifier.cardHabitPadding(
                        index,
                        lastElement = index == habits.size - 1
                    ),
                    startOfWeek = weekTimeRange.startOfWeek,
                    endOfWeek = weekTimeRange.endOfWeek,
                    selectedDate = LocalDate.now(),
                    onLongClick = onLongClick,
                    onClick = onClick
                )
            }
        }
    }

}