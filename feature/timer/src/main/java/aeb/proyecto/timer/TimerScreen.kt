package aeb.proyecto.timer

import aeb.proyecto.timer.components.horizontal.HorizontalTimerScreen
import aeb.proyecto.timer.components.vertical.VerticalTimerScreen
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel(),
    navigateToHabitScreen: () -> Unit
){

    val timerDataUIState = viewModel.timerData.collectAsStateWithLifecycle().value
    val timerStopWatchUIState = viewModel.timerStopWatchUIState.collectAsStateWithLifecycle().value
    val bottomSheetState = viewModel.bottomSheetState.collectAsStateWithLifecycle().value
    val historyEntries = viewModel.historyEntries.collectAsStateWithLifecycle().value

    val orientation = getOrientation()

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.timer_title), fontSize = 20.sp)
    }

    LaunchedEffect (Unit){
        Log.e("History","$historyEntries")
    }

    when(orientation){
        Orientation.Portrait -> {
            VerticalTimerScreen(
                timerUiState = timerDataUIState,
                timerStopWatchUIState = timerStopWatchUIState,
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
                onClickCross = viewModel::cancelHabitLinked
            )
        }
        Orientation.Landscape -> {
            HorizontalTimerScreen(
                timerUiState = timerDataUIState,
                timerStopWatchUIState = timerStopWatchUIState,
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
                onClickCross = viewModel::cancelHabitLinked
            )
        }
    }
}