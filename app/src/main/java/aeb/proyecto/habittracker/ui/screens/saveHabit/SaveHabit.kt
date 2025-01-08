package aeb.proyecto.habittracker.ui.screens.saveHabit

import aeb.proyecto.habittracker.ui.screens.saveHabit.saveHabitComponents.SaveHabitScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SaveHabit(
    saveHabitViewModel: SaveHabitViewModel = hiltViewModel(),
    navigateToImport: () -> Unit,
) {
    SaveHabitScreen(logOut = {
        navigateToImport()
        saveHabitViewModel.logOut()
    })
}