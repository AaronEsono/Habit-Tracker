package aeb.proyecto.habit.components.screen.typeHabits

import aeb.proyecto.habit.components.card.habit.CardHabit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing80
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
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
fun DailyHabitsScreen(
    selectedDate: LocalDate,
    habits: List<HabitWithDailyHabit>
) {
    val visibleItems = remember { mutableStateListOf<Int>() }

    // Efecto para mostrar los ítems con retraso uno por uno
    LaunchedEffect(habits, selectedDate) {
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
                CardHabit(
                    habit = habits[index],
                    modifier = Modifier.cardHabitPadding(
                        index,
                        lastElement = index == habits.size - 1
                    ),
                    selectedDate = selectedDate
                )
            }
        }
    }
}

fun Modifier.cardHabitPadding(index:Int, lastElement:Boolean = false):Modifier{
    return if(lastElement){
        if(index == 0)
            padding(bottom = spacing80, top = spacing12)
        else
            padding(bottom = spacing80)
    }else{
        if (index == 0) padding(top = spacing12, bottom = spacing8) else padding(bottom = spacing8)
    }
}