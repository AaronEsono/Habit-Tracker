package aeb.proyecto.timer.navigation

import aeb.proyecto.timer.TimerScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable
import java.time.LocalDate

/**
 * Route definition for the Timer feature using Kotlin Serialization.
 */
@Serializable
data object Timer

/**
 * Extension function to navigate to the Timer screen.
 * * @param optionsBuilder Lambda to configure navigation options (e.g., popUpTo, launchSingleTop).
 */
fun NavController.navigateToTimer(optionsBuilder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(Timer, optionsBuilder)
}

/**
 * Extension function to register the Timer screen in the navigation graph.
 * Includes deep link support for the URI pattern `app://main/timer`.
 */
fun NavGraphBuilder.timerScreen(){
    composable<Timer>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "app://main/timer"
            }
        )
    ){
        TimerScreen()
    }
}