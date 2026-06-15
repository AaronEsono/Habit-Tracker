package aeb.proyecto.save.navigation

import aeb.proyecto.save.SaveScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import java.io.Serial

/**
 * Route definition for the Save/Sync feature module.
 * Used for type-safe navigation via the Navigation Compose library.
 */
@Serializable object Save

/**
 * Navigates to the Save screen.
 * * @param optionsBuilder Optional configuration block for navigation options
 * (e.g., popUpTo, launchSingleTop).
 */
fun NavController.navigateToSave(optionsBuilder: NavOptionsBuilder.() -> Unit = {}){
    navigate(Save, optionsBuilder)
}

/**
 * Registers the Save destination in the navigation graph.
 * * @param onImportScreen Callback function to handle navigation events
 * directed to the import/export screens.
 */
fun NavGraphBuilder.saveScreen(onImportScreen: () -> Unit) {
    composable<Save> {
        SaveScreen(onImportScreen)
    }
}