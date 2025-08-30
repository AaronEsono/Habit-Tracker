package aeb.proyecto.timer

import aeb.proyecto.timer.components.commom.infinitePicker.getCenteredIndex
import aeb.proyecto.timer.components.horizontal.HorizontalTimerScreen
import aeb.proyecto.timer.components.vertical.VerticalTimerScreen
import aeb.proyecto.timer.constants.hours
import aeb.proyecto.timer.constants.minutes
import aeb.proyecto.timer.constants.seconds
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.launch
import java.time.LocalDate

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