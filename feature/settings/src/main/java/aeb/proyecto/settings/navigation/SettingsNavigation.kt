package aeb.proyecto.settings.navigation

import aeb.proyecto.settings.SettingsScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Type-safe route definition for the Settings feature.
 */
@Serializable object Settings

/**
 * Extension function to navigate to the Settings destination.
 */
fun NavController.navigateToSettings(){
    navigate(Settings)
}

/**
 * Adds the Settings screen to the navigation graph.
 * @param onImportScreen Callback to navigate to the Import/Export module.
 * @param onSaveScreen Callback to navigate to the Save/Sync module.
 */
fun NavGraphBuilder.settingsScreen(onImportScreen: () -> Unit, onSaveScreen: () -> Unit) {
    composable<Settings> {
        SettingsScreen(onImportScreen, onSaveScreen)
    }
}