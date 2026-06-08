package aeb.proyecto.addhabit.navigation

import aeb.proyecto.addhabit.AddHabitScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation destination route contract targeting the Habit definition layout workspace.
 * Acts as the strict data payload container representing arguments parsed along routing transitions.
 *
 * @property id The unique record primary footprint key required to rehydrate form fields in edit mode,
 * null if launching a fresh creation pipeline.
 */
@Serializable
data class AddHabit(val id:Long?)

/**
 * Dispatches a formal navigation request targeting the structured AddHabit composition layout.
 * Packs argument keys inside strongly-typed routing signatures to guarantee compile-time verification checks.
 *
 * @param id The target persistent entity tracking identifier sequence to forward downstream.
 * @param optionsBuilder Optional custom [NavOptionsBuilder] configurations to mutate graph stack behavior (e.g., launchSingleTop).
 */
fun NavController.navigateToAddHabit(id:Long?,optionsBuilder: NavOptionsBuilder.() -> Unit = {}){
    navigate(AddHabit(id), optionsBuilder)
}

/**
 * Mounts the [AddHabitScreen] node onto the application's central structural navigation graph topology.
 * Intercepts platform routing intent, extracts navigation parameters safely via serializable type bindings,
 * and wires up terminal back-navigation presentation event handles.
 *
 * @param onHabitScreen Terminal callback lambda handler executing backward navigation steps out of the current scope.
 */
fun NavGraphBuilder.addHabitScreen(onHabitScreen: () -> Unit) {
    composable<AddHabit> {backStackEntry ->
        // Safely extract the compiled long token directly out of the serialization backstack bundle references
        val idHabit = backStackEntry.arguments?.getLong("id")

        AddHabitScreen(idHabit ?: -1L, onHabitScreen)
    }
}