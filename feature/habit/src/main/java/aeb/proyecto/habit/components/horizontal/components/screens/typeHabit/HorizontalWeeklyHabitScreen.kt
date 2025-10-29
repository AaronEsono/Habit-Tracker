package aeb.proyecto.habit.components.horizontal.components.screens.typeHabit

import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.separateGoal.SeparateMonthlyCard
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.uniqueGoal.UniqueMonthlyCard
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.types.separateGoal.SeparateWeeklyCard
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.types.uniqueGoal.UniqueWeeklyCard
import aeb.proyecto.habit.utils.cardHabitPadding
import aeb.proyecto.habit.utils.cardHabitPaddingHorizontal
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
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

@Composable
fun HorizontalWeeklyHabitScreen(
    weekTimeRange: TimeRangeUiState.Weekly,
    habits: List<HabitWithDailyHabit>,
    onLongClick: (id: Long, date: LocalDate) -> Unit,
    onClick: (id: Long, date: LocalDate) -> Unit
) {

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

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
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
                if (habits[index].habit.typeHabit is TypeHabit.Weekly) {
                    val habit = habits[index]
                    val typeHabit = habit.habit.typeHabit as TypeHabit.Weekly

                    if (typeHabit.weeklyGoal) {
                        UniqueWeeklyCard(
                            habit = habit,
                            modifier = Modifier.cardHabitPaddingHorizontal(
                                index,
                                lastElement = index == habits.size - 1
                            ),
                            startOfWeek = weekTimeRange.startOfWeek,
                            endOfWeek = weekTimeRange.endOfWeek,
                            selectedDate = LocalDate.now(),
                            onLongClick = onLongClick,
                            onClick = onClick
                        )
                    } else {
                        SeparateWeeklyCard(
                            habit = habit,
                            modifier = Modifier.cardHabitPaddingHorizontal(
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
    }

}