package aeb.proyecto.timer

import aeb.proyecto.timer.components.horizontal.HorizontalTimerScreen
import aeb.proyecto.timer.components.vertical.VerticalTimerScreen
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel()
){

    val timerDataUIState = viewModel.timerData.collectAsStateWithLifecycle().value
    val timerStopWatchUIState = viewModel.timerStopWatchUIState.collectAsStateWithLifecycle().value
    val bottomSheetState = viewModel.bottomSheetState.collectAsStateWithLifecycle().value
    val timeEntryState = viewModel.historyEntries.collectAsStateWithLifecycle().value
    val triggerSegmentedTimer = viewModel.triggerSegmentedTimer

    val orientation = getOrientation()

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.timer_title), fontSize = 20.sp)
    }

    when(orientation){
        Orientation.Portrait -> {
            VerticalTimerScreen(
                timerUiState = timerDataUIState,
                timerStopWatchUIState = timerStopWatchUIState,
                listTimeEntryState = timeEntryState,
                bottomSheetState = bottomSheetState,
                triggerSegmentedTimer = triggerSegmentedTimer,
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
                onCancelButton = viewModel::cancelService,
                onClickHabitButton = viewModel::onClickHabitButton,
                onDismissHabitBottomSheet = viewModel::onDismissHabitBottomSheet,
                onAcceptBottomSheet = viewModel::onAcceptBottomSheetPickHabit,
                onClickCross = viewModel::cancelHabitLinked,
                onClickTimeEntry = viewModel::onClickTimeEntry,
                onClickFavorite = viewModel::onClickFavorite,
                onClickDelete = viewModel::onDeleteHistoryEntry
            )
        }
        Orientation.Landscape -> {
            HorizontalTimerScreen(
                timerUiState = timerDataUIState,
                timerStopWatchUIState = timerStopWatchUIState,
                listTimeEntryState = timeEntryState,
                triggerSegmentedTimer = triggerSegmentedTimer,
                bottomSheetState = bottomSheetState,
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
                onCancelButton = viewModel::cancelService,
                onClickHabitButton = viewModel::onClickHabitButton,
                onDismissHabitBottomSheet = viewModel::onDismissHabitBottomSheet,
                onAcceptBottomSheet = viewModel::onAcceptBottomSheetPickHabit,
                onClickCross = viewModel::cancelHabitLinked,
                onClickTimeEntry = viewModel::onClickTimeEntry,
                onClickFavorite = viewModel::onClickFavorite,
                onClickDelete = viewModel::onDeleteHistoryEntry
            )
        }
    }
}