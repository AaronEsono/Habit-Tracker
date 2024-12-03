package aeb.proyecto.habittracker.ui.screens.habits

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.utils.MainLocalViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HabitsScreen(habitsViewModel: HabitsViewModel = hiltViewModel()){

    val mainViewModel = MainLocalViewModel.current

    LaunchedEffect(Unit) {
        mainViewModel.setTitleTopBar(R.string.topbar_habit)
    }



}