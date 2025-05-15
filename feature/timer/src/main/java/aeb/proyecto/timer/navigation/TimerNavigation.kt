package aeb.proyecto.timer.navigation

import aeb.proyecto.timer.TimerScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data object Timer

fun NavController.navigateToTimer(optionsBuilder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(Timer, optionsBuilder)
}

fun NavGraphBuilder.timerScreen(navigateToHabit: () -> Unit){
    composable<Timer>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "app://main/timer"
            }
        )
    ){
        TimerScreen(navigateToHabitScreen = navigateToHabit)
    }
}