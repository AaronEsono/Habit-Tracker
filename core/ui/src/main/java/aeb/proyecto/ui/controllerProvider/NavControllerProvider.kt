package aeb.proyecto.ui.controllerProvider

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("NavController no está disponible en este contexto.")
}