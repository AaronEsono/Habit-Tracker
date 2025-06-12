package aeb.proyecto.timer

import aeb.proyecto.timer.components.commom.loading.TimerLoading
import aeb.proyecto.timer.components.horizontal.HorizontalTimerScreen
import aeb.proyecto.timer.components.vertical.VerticalTimerScreen
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.timer.model.TimerServiceUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
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
    val timerStopWatchUIState = viewModel.timerStopWatchUIState.collectAsStateWithLifecycle().value
    val orientation = getOrientation()

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.timer_title), fontSize = 20.sp)
    }

    when(orientation){
        Orientation.Portrait -> {
            VerticalTimerScreen(
                timerUiState = timerDataUIState,
                timerStopWatchUIState = timerStopWatchUIState,
                onHourChange = viewModel::onHourChange,
                onMinuteChange = viewModel::onMinuteChange,
                onSecondChange = viewModel::onSecondChange,
                onTypeChange = viewModel::onTypeButtonChange,
                onStartService = viewModel::startService,
                onSetChange = viewModel::onSetChange,
                onIntervalHourChange = viewModel::setIntervalHour,
                onButtonIntervalWorkChange = viewModel::addHourTimer,
                onButtonIntervalRestChange = viewModel::addRestTimer,
                onFinishButton = viewModel::finishService,
                onResumeButton = viewModel::resumeService,
                onStopService = viewModel::stopService,
                onCancelButton = viewModel::cancelService
            )
        }
        Orientation.Landscape -> {
            HorizontalTimerScreen(
                timerUiState = timerDataUIState,
                timerStopWatchUIState = timerStopWatchUIState,
                onHourChange = viewModel::onHourChange,
                onMinuteChange = viewModel::onMinuteChange,
                onSecondChange = viewModel::onSecondChange,
                onTypeChange = viewModel::onTypeButtonChange,
                onStartService = viewModel::startService,
                onSetChange = viewModel::onSetChange,
                onIntervalHourChange = viewModel::setIntervalHour,
                onButtonIntervalWorkChange = viewModel::addHourTimer,
                onButtonIntervalRestChange = viewModel::addRestTimer,
                onFinishButton = viewModel::finishService,
                onResumeButton = viewModel::resumeService,
                onStopService = viewModel::stopService,
                onCancelButton = viewModel::cancelService
            )
        }
    }
}