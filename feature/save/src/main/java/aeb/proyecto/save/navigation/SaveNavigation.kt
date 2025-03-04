package aeb.proyecto.save.navigation

import aeb.proyecto.save.SaveScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable object Save

fun NavController.navigateToSave(){
    navigate(Save)
}

fun NavGraphBuilder.saveScreen(onImportScreen: () -> Unit) {
    composable<Save> {
        SaveScreen(onImportScreen)
    }
}