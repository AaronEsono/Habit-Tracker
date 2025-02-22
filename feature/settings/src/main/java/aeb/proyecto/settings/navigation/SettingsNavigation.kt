package aeb.proyecto.settings.navigation

import aeb.proyecto.settings.SettingsScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object Settings

fun NavController.navigateToSettings(){
    navigate(Settings){}
}

fun NavGraphBuilder.settingsScreen(onImportScreen: () -> Unit, onSaveScreen: () -> Unit) {
    composable<Settings> {
        SettingsScreen(onImportScreen, onSaveScreen)
    }
}