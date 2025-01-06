package aeb.proyecto.habittracker.ui.screens.saveHabit

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun SaveHabit(navigateToOptions:() -> Unit){

    BackHandler { navigateToOptions() }

}