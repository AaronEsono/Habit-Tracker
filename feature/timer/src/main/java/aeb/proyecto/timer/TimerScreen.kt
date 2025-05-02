package aeb.proyecto.timer

import aeb.proyecto.timer.components.loading.TimerLoading
import aeb.proyecto.timer.components.screens.RelojScreen
import aeb.proyecto.ui.dimmens.Dimmens
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.navigationIcon.NavigationIcon
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel(),
    dayHabitId: Pair<Long,LocalDate>,
    navigateToHabitScreen: () -> Unit
){

    val timerUiState = viewModel.timerUIState.collectAsStateWithLifecycle().value
    val timerSelected = viewModel.timerSelected.collectAsStateWithLifecycle().value
    val timeLeft = viewModel.timeLeft.collectAsStateWithLifecycle().value

    ProvideAppBarNavigationIcon {
        NavigationIcon()
    }

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.timer_title), fontSize = 20.sp)
    }

    LaunchedEffect (Unit){
        viewModel.getData(dayHabitId)
    }

    TimerScreen(
        timerUIState = timerUiState,
        timerSelected = timerSelected,
        timeLeft = timeLeft,
        onHourChange = viewModel::onHourChange,
        onMinuteChange = viewModel::onMinuteChange,
        onSecondChange = viewModel::onSecondChange
    )
}


@Composable
internal fun TimerScreen(
    timerUIState: TimerUiState,
    timerSelected: TimerSelectedState,
    timeLeft: String,
    onHourChange: (String) -> Unit = {},
    onMinuteChange: (String) -> Unit = {},
    onSecondChange: (String) -> Unit = {}
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = spacing12, start = spacing8, end = spacing8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (timerUIState) {
            is TimerUiState.Error -> Unit
            TimerUiState.Loading -> {
                TimerLoading()
            }

            is TimerUiState.Success -> {
                RelojScreen(
                    timerUIState.habitWithDay,
                    timerSelected,
                    timeLeft = timeLeft,
                    onHourChange = onHourChange,
                    onMinuteChange = onMinuteChange,
                    onSecondChange = onSecondChange
                )
            }
        }
    }
}