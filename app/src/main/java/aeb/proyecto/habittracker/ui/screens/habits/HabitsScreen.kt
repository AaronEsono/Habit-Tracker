package aeb.proyecto.habittracker.ui.screens.habits

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.dailyHabit.CardDailyHabit
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.screens.habits.habitsComponents.HabitScreenStates
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HabitsScreen(
    habitsViewModel: HabitsViewModel = hiltViewModel(),
    onEditClick: (Long) -> Unit = {}
) {

    val habits = habitsViewModel.habits.collectAsState().value
    val uiState = habitsViewModel.uiState.collectAsState().value

    val shouldBlur = remember(uiState.showDialog) { uiState.showDialog }

    LaunchedEffect (true){
        Log.d("HabitsScreen", "HabitsScreen: $habits")
    }

    if (habits.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = spacing8),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LabelMediumText(
                text = stringResource(R.string.habits_screen_no_habit)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .blur(if (shouldBlur) 10.dp else 0.dp)
                .padding(horizontal = spacing8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(habits.size, key = { habits[it].habit.id }) { habit ->
                CardDailyHabit(
                    habitWithDailyHabit = habits[habit],
                    onClick = { id -> habitsViewModel.choseStep(id) },
                    onClickCard = { id -> habitsViewModel.showDialog(id) }
                )
            }
        }

        HabitScreenStates(
            uiState = uiState,
            habitsViewModel = habitsViewModel,
            onEditClick = onEditClick
        )
    }
}