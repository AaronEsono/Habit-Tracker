package aeb.proyecto.statistics.navigation

import aeb.proyecto.statistics.StatisticsScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object Statistics

fun NavController.navigateToStatistics(){
    navigate(Statistics)
}

fun NavGraphBuilder.statisticsScreen() {
    composable<Statistics> {
        StatisticsScreen()
    }
}