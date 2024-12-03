package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.MainViewModel
import androidx.compose.runtime.staticCompositionLocalOf

val MainLocalViewModel = staticCompositionLocalOf<MainViewModel> {
    error("No ViewModel provided")
}