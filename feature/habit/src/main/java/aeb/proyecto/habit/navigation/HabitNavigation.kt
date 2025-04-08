package aeb.proyecto.habit.navigation

import aeb.proyecto.habit.HabitScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Habit

fun NavController.navigateToHabit(optionsBuilder: NavOptionsBuilder.() -> Unit = {}){
    navigate(Habit,optionsBuilder)
}

fun NavGraphBuilder.habitScreen(onAddHabit: (Long) -> Unit) {
    composable<Habit> {
        HabitScreen(navigateToAddHabit = onAddHabit)
    }
}