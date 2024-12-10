package aeb.proyecto.habittracker.ui.screens.habits

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.card.CardDailyHabit
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HabitsScreen(habitsViewModel: HabitsViewModel = hiltViewModel()) {

    val habits = habitsViewModel.habits.observeAsState().value

    if (habits?.isEmpty() == true) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = spacing8),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LabelMediumText(
                text = stringResource(R.string.habits_screen_no_habit)
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = spacing8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            habits?.forEach {
                CardDailyHabit(it) { id ->
                    habitsViewModel.plusOneHabit(id)
                }
            }
        }
    }

}