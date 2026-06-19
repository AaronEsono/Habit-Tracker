package aeb.proyecto.timer.components.vertical

import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.components.common.loading.TimerLoading
import aeb.proyecto.timer.components.vertical.components.screens.VerticalSuccessTimerScreen
import aeb.proyecto.timer.model.TimeEntryState
import aeb.proyecto.timer.model.TimerServiceUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.SharedFlow
import java.time.LocalDate

/**
 * The top-level screen container for the Timer module in portrait mode.
 * Evaluates the [timerUiState] and delegates the UI to the appropriate
 * screen implementation (Loading vs. Success/Vertical Success Screen).
 *
 * @param timerUiState Current state (Loading, Success, Error).
 * @param timerStopWatchUIState Live service state (Idle/Running).
 * @param listTimeEntryState State for the history log.
 * @param triggerSegmentedTimer Flow for external configuration events.
 * @param bottomSheetState Visibility for the habit selection sheet.
 * @param onHourChange/onMinuteChange/onSecondChange Configuration callbacks.
 * @param onTypeChange Mode switching callback.
 * @param onStartService/onStopService/onResumeButton/onCancelButton/onFinishButton Lifecycle actions.
 * @param onIntervalHourChange Interval-specific duration adjustments.
 * @param onButtonIntervalWorkChange/onButtonIntervalRestChange Mode toggles.
 * @param onSetChange Callback for interval repetition counts.
 * @param onClickHabitButton/onDismissHabitBottomSheet/onAcceptBottomSheet Habit selection flow.
 * @param onClickCross/onClickTimeEntry/onClickFavorite/onClickDelete Management actions.
 */
@Composable
fun VerticalTimerScreen(
    timerUiState: TimerUiState,
    timerStopWatchUIState: TimerServiceUIState,
    listTimeEntryState: TimeEntryState,
    triggerSegmentedTimer: SharedFlow<Triple<Int,Int,Int>?>,
    bottomSheetState:Boolean,
    onHourChange:(String) -> Unit = {},
    onMinuteChange:(String) -> Unit = {},
    onSecondChange: (String) -> Unit = {},
    onTypeChange: (Int) -> Unit = {},
    onStartService: () -> Unit = {},
    onSetChange: (Int) -> Unit = {},
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit = {_,_ ->},
    onButtonIntervalWorkChange: (Boolean) -> Unit = {},
    onButtonIntervalRestChange: (Boolean) -> Unit = {},
    onFinishButton: () -> Unit = {},
    onResumeButton: () -> Unit = {},
    onStopService: () -> Unit = {},
    onCancelButton: () -> Unit = {},
    onClickHabitButton: () -> Unit = {},
    onDismissHabitBottomSheet: () -> Unit = {},
    onAcceptBottomSheet: (Long, LocalDate) -> Unit,
    onClickCross:()->Unit = {},
    onClickTimeEntry: (Long) -> Unit = {},
    onClickFavorite: (Long,Boolean) -> Unit = {_,_ ->},
    onClickDelete: (Long) -> Unit = {_ -> },
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = spacing8, end = spacing8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (timerUiState) {
            is TimerUiState.Error -> Unit
            TimerUiState.Loading -> {
                TimerLoading()
            }

            is TimerUiState.Success -> {
                VerticalSuccessTimerScreen(
                    timerUIState = timerUiState,
                    timerStopWatchUIState = timerStopWatchUIState,
                    listTimeEntryState = listTimeEntryState,
                    triggerSegmentedTimer = triggerSegmentedTimer,
                    bottomSheetState = bottomSheetState,
                    onHourChange = onHourChange,
                    onMinuteChange = onMinuteChange,
                    onSecondChange = onSecondChange,
                    onTypeChange = onTypeChange,
                    onStartService = onStartService,
                    onSetChange = onSetChange,
                    onIntervalHourChange = onIntervalHourChange,
                    onButtonIntervalWorkChange = onButtonIntervalWorkChange,
                    onButtonIntervalRestChange = onButtonIntervalRestChange,
                    onFinishButton = onFinishButton,
                    onResumeButton = onResumeButton,
                    onStopService = onStopService,
                    onCancelButton = onCancelButton,
                    onClickHabitButton = onClickHabitButton,
                    onDismissHabitBottomSheet = onDismissHabitBottomSheet,
                    onAcceptBottomSheet = onAcceptBottomSheet,
                    onClickCross = onClickCross,
                    onClickTimeEntry = onClickTimeEntry,
                    onClickFavorite = onClickFavorite,
                    onClickDelete = onClickDelete
                )
            }
        }
    }

}