package aeb.proyecto.habit.components.vertical.components.screens.typeHabit

import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.separateGoal.SeparateMonthlyCard
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.uniqueGoal.UniqueMonthlyCard
import aeb.proyecto.habit.utils.cardHabitPadding
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import android.util.Log
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
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * A vertical list screen for displaying monthly habits, optimized for mobile devices.
 *
 * This screen renders a list of monthly habits using a [LazyColumn], ensuring
 * that cards for unique monthly goals and separate tracking are displayed
 * sequentially. It applies specific padding and staggered entry animations
 * to provide a smooth, consistent mobile user experience.
 *
 * @param timeRange Current monthly range state.
 * @param startDayOfWeek User-preferred start day of the week.
 * @param habits The list of monthly habits to display.
 * @param onClickCard Callback for primary card interactions.
 * @param onLongClick Callback for secondary interactions on calendar dates.
 * @param onClick Callback for direct progress updates.
 */
@Composable
fun VerticalMonthlyHabitScreen (
    timeRange: TimeRangeUiState.Monthly,
    startDayOfWeek: DayOfWeek? = DayOfWeek.MONDAY,
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
                if(habits[index].habit.typeHabit is TypeHabit.Monthly){
                    val habit = habits[index]
                    val typeHabit = habit.habit.typeHabit as TypeHabit.Monthly

                    if(typeHabit.monthlyGoal){
                        UniqueMonthlyCard(
                            habit = habit,
                            modifier = Modifier.cardHabitPadding(
                                index,
                                lastElement = index == habits.size - 1
                            ),
                            startOfMonth = timeRange.startOfMonth,
                            firstDayOfWeek = startDayOfWeek,
                            selectedDate = LocalDate.now(),
                            onClickCard = onClickCard,
                            onLongClick = onLongClick,
                            onClick = onClick
                        )
                    }else{
                        SeparateMonthlyCard(
                            habit = habit,
                            modifier = Modifier.cardHabitPadding(
                                index,
                                lastElement = index == habits.size - 1
                            ),
                            startOfMonth = timeRange.startOfMonth,
                            firstDayOfWeek = startDayOfWeek,
                            selectedDate = LocalDate.now(),
                            onClickCard = onClickCard,
                            onLongClick = onLongClick,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }


}
