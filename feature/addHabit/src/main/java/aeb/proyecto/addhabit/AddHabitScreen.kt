package aeb.proyecto.addhabit

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddHabitScreen(
    habitIt:Long?,
    navigateToHabit: () -> Unit,
    viewModel: AddHabitViewModel = hiltViewModel()
){

    AddHabitScreen()

}

@Composable
internal fun AddHabitScreen(){

}