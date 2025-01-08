package aeb.proyecto.habittracker.ui.screens.saveHabit

import aeb.proyecto.habittracker.ui.screens.saveHabit.saveHabitComponents.SaveHabitScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SaveHabit(
    saveHabitViewModel: SaveHabitViewModel = hiltViewModel(),
    navigateToImport: () -> Unit,
) {

    val name = remember { saveHabitViewModel.getName() }

    SaveHabitScreen(name, logOut = {
        navigateToImport()
        saveHabitViewModel.logOut()
    })
}