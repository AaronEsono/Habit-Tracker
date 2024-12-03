package aeb.proyecto.habittracker.ui.screens.habits

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.MainLocalViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HabitsScreen(habitsViewModel: HabitsViewModel = hiltViewModel()){

    Column (modifier = Modifier.fillMaxSize().padding(spacing16), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        LabelMediumText(text = stringResource(R.string.habits_screen_no_habit), textAlign = TextAlign.Center)
    }



}