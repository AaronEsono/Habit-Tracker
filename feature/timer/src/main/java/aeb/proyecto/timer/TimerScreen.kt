package aeb.proyecto.timer

import aeb.proyecto.timer.components.loading.TimerLoading
import aeb.proyecto.timer.components.screens.RelojScreen
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel(),
    navigateToHabitScreen: () -> Unit
){

    val timerDataUIState = viewModel.timerData.collectAsStateWithLifecycle().value

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.timer_title), fontSize = 20.sp)
    }

    TimerScreen(
        timerUiState = timerDataUIState,
        onHourChange = viewModel::onHourChange,
        onMinuteChange = viewModel::onMinuteChange,
        onSecondChange = viewModel::onSecondChange,
        onTypeChange = viewModel::onTypeButtonChange,
        onStartService = viewModel::startService,
        onIntervalHourChange = viewModel::setIntervalHour
    )
}


@Composable
internal fun TimerScreen(
    timerUiState: TimerUiState,
    onHourChange:(String) -> Unit = {},
    onMinuteChange:(String) -> Unit = {},
    onSecondChange: (String) -> Unit = {},
    onTypeChange: (Int) -> Unit = {},
    onStartService: () -> Unit = {},
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit = {_,_ ->},
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = spacing12, start = spacing8, end = spacing8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (timerUiState) {
            is TimerUiState.Error -> Unit
            TimerUiState.Loading -> {
                TimerLoading()
            }

            is TimerUiState.Success -> {
                RelojScreen(
                    timerUIState = timerUiState,
                    onHourChange = onHourChange,
                    onMinuteChange = onMinuteChange,
                    onSecondChange = onSecondChange,
                    onTypeChange = onTypeChange,
                    onStartService = onStartService,
                    onIntervalHourChange = onIntervalHourChange
                )
            }
        }
    }
}