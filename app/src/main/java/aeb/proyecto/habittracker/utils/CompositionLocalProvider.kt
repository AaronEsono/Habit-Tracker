package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.MainViewModel
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val MainLocalViewModel = staticCompositionLocalOf<MainViewModel> {
    error("No ViewModel provided")
}

val LocalNavController = staticCompositionLocalOf<NavHostController?> {
    null
}