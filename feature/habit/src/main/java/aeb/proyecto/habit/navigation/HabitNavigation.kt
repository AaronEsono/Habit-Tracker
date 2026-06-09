package aeb.proyecto.habit.navigation

import aeb.proyecto.habit.HabitScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Immutable compiled compile-time navigation route coordinate for the Habit Dashboard Module.
 * Annotated with [Serializable] to integrate natively within Android's type-safe navigation framework.
 */
@Serializable
data object Habit

/**
 * Executes a forward navigation dispatch targeting the Habit Dashboard Module.
 *
 * @param optionsBuilder Lambda configuration scope allowing downstream customization of execution
 * flags such as launchSingleTop, popUpTo, or state restoration behaviors.
 */
fun NavController.navigateToHabit(optionsBuilder: NavOptionsBuilder.() -> Unit = {}){
    navigate(Habit,optionsBuilder)
}

/**
 * Injects the Habit Dashboard viewport node into the centralized application graph matrix.
 * Decouples navigation controllers via inversion of control lambdas to preserve absolute modularity.
 *
 * @param onAddHabit Dynamic routing instruction triggering mutation paths; carries unique entity ID markers.
 * @param onClickTimer Directional callback forwarding the user workspace focus straight into the Chronometer module.
 */
fun NavGraphBuilder.habitScreen(
    onAddHabit: (Long) -> Unit,
    onClickTimer: () -> Unit
) {
    composable<Habit> {
        // Inflate the decoupled host screen, linking stateless routing triggers cleanly
        HabitScreen(navigateToAddHabit = onAddHabit, navigateToTimer = onClickTimer)
    }
}