package aeb.proyecto.habit.components.vertical.components.screens.typeHabit

import aeb.proyecto.habit.components.common.habitCards.dailyCard.DailyCard
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

/**
 * A vertical list screen for displaying daily habits, optimized for mobile devices.
 *
 * This screen renders a list of [HabitWithDailyHabit] items using a [LazyColumn].
 * It mirrors the staggered entry animation logic found in the horizontal variant,
 * ensuring a consistent and fluid user experience across different form factors.
 *
 * @param selectedDate The currently active date for the habit view.
 * @param habits The list of daily habits to be displayed.
 * @param onClickCard Callback for main card interactions.
 * @param onLongClick Callback for secondary interactions on habit cards.
 * @param onClick Callback for direct progress updates.
 */
@Composable
fun VerticalDailyHabitScreen(
    selectedDate: LocalDate,
    habits: List<HabitWithDailyHabit>,
    onClickCard: (id: Long) -> Unit,
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
                DailyCard(
                    habit = habits[index],
                    modifier = Modifier.cardHabitPadding(
                        index,
                        lastElement = index == habits.size - 1
                    ),
                    selectedDate = selectedDate,
                    onLongClick = onLongClick,
                    onClickCard = onClickCard,
                    onClick = onClick
                )
            }
        }
    }

}