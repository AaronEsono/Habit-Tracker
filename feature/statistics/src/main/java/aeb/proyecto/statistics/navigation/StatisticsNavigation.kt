package aeb.proyecto.statistics.navigation

import aeb.proyecto.statistics.StatisticsScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Route definition for the Statistics screen.
 */
@Serializable object Statistics

/**
 * Navigates to the Statistics screen.
 * Uses the [NavController] to trigger the navigation action using the [Statistics] route.
 */
fun NavController.navigateToStatistics(){
    navigate(Statistics)
}

/**
 * Adds the Statistics screen to the [NavGraphBuilder].
 * This defines the destination using the [Statistics] object and
 * provides the [StatisticsScreen] composable as the UI implementation.
 */
fun NavGraphBuilder.statisticsScreen() {
    composable<Statistics> {
        StatisticsScreen()
    }
}