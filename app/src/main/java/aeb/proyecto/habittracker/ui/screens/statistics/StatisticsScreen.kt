package aeb.proyecto.habittracker.ui.screens.statistics

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.screens.statistics.statisticsComponents.StatisticsContent
import aeb.proyecto.habittracker.utils.AppState
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun StatisticsScreen(statisticsViewModel: StatisticsViewModel = hiltViewModel()) {

    val habits = statisticsViewModel.habits.collectAsState().value

    if (statisticsViewModel.getSharedState().appState.collectAsState().value != AppState.Loading) {
        if (habits.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = spacing8),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabelMediumText(
                    text = stringResource(R.string.statistics_screen_no_habit)
                )
            }
        } else {
            StatisticsContent(habits, statisticsViewModel)
        }
    }
}