package aeb.proyecto.habit.components.horizontal.components.screens.typeHabit

import aeb.proyecto.habit.components.common.habitCards.dailyCard.DailyCard
import aeb.proyecto.habit.utils.cardHabitPadding
import aeb.proyecto.habit.utils.cardHabitPaddingHorizontal
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import java.time.LocalDate

/**
 * A responsive grid screen for displaying daily habits.
 *
 * This screen renders a list of [HabitWithDailyHabit] items in a two-column grid.
 * It features a staggered entry animation where each card slides and fades into view
 * sequentially, providing a polished user experience.
 *
 * @param selectedDate The currently active date for the habit view.
 * @param habits The list of daily habits to be displayed.
 * @param onClickCard Callback for main card interactions (e.g., viewing details).
 * @param onLongClick Callback for secondary interactions (e.g., context menus).
 * @param onClick Callback for direct progress updates on the habit.
 */
@Composable
fun HorizontalDailyHabitScreen(
    selectedDate: LocalDate,
    habits: List<HabitWithDailyHabit>,
    onClickCard: (id:Long) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit,
    onClick: (id: Long, date: LocalDate) -> Unit
){

    // State to track which items have completed their entrance animation
    val visibleItems = remember { mutableStateListOf<Int>() }

    // Re-trigger animation when the list of habits changes
    val habitsHash = habits.map { it.habit.id }.hashCode()

    LaunchedEffect(habitsHash) {
        visibleItems.clear()
        habits.indices.forEach { index ->
            delay(50)
            visibleItems.add(index)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
            .padding(start = spacing8, end = spacing8),
        horizontalArrangement = Arrangement.spacedBy(spacing10),
        verticalArrangement = Arrangement.spacedBy(spacing10)
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
                DailyCard(
                    habit = habits[index],
                    modifier = Modifier.cardHabitPaddingHorizontal(
                        index,
                        lastElement = index == habits.size - 1
                    ),
                    selectedDate = selectedDate,
                    onClickCard = onClickCard,
                    onLongClick = onLongClick,
                    onClick = onClick
                )
            }
        }
    }
}