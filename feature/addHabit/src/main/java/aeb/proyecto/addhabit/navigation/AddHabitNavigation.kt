package aeb.proyecto.addhabit.navigation

import aeb.proyecto.addhabit.AddHabitScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class AddHabit(val id:Long?)

fun NavController.navigateToAddHabit(id:Long?,optionsBuilder: NavOptionsBuilder.() -> Unit = {}){
    navigate(AddHabit(id), optionsBuilder)
}

fun NavGraphBuilder.addHabitScreen(onHabitScreen: () -> Unit) {
    composable<AddHabit> {backStackEntry ->
        val idHabit = backStackEntry.arguments?.getLong("id")

        AddHabitScreen(idHabit ?: -1L, onHabitScreen)
    }
}