package aeb.proyecto.timer.components.vertical.components.screens

import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.model.TimeEntryState
import aeb.proyecto.timer.model.TimerServiceUIState
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.SharedFlow
import java.time.LocalDate

/**
 * The primary screen container for the Timer module in portrait mode.
 * Acts as a router that switches between the configuration dashboard and the
 * active timer session using [AnimatedContent].
 *
 * @param timerUIState The configuration state when the timer is idle.
 * @param timerStopWatchUIState The live state of the timer service (Idle, Running, Paused).
 * @param listTimeEntryState State for the history log.
 * @param triggerSegmentedTimer Flow to manage external resets.
 * @param bottomSheetState Visibility state for the habit selection sheet.
 * @param onHourChange/onMinuteChange/onSecondChange Configuration callbacks for input.
 * @param onIntervalHourChange Interval-specific duration adjustments.
 * @param onButtonIntervalWorkChange/onButtonIntervalRestChange Mode toggles for intervals.
 * @param onSetChange Callback for interval repetition counts.
 * @param onTypeChange Callback to switch between timer types.
 * @param onStartService Action to initialize the timer service.
 * @param onStopService Action to pause the timer service.
 * @param onResumeButton Action to resume the service.
 * @param onCancelButton Action to abort the session.
 * @param onFinishButton Action to complete the session manually.
 * @param onClickHabitButton Trigger for habit selection.
 * @param onDismissHabitBottomSheet Callback to close the habit sheet.
 * @param onAcceptBottomSheet Callback to persist habit association.
 * @param onClickCross Action for cancelling the screen/view.
 * @param onClickTimeEntry Callback for selecting a history entry.
 * @param onClickFavorite Callback for favoriting history items.
 * @param onClickDelete Callback for deleting history items.
 */
@Composable
fun VerticalSuccessTimerScreen(
    timerUIState: TimerUiState.Success,
    timerStopWatchUIState: TimerServiceUIState,
    listTimeEntryState: TimeEntryState,
    triggerSegmentedTimer: SharedFlow<Triple<Int, Int, Int>?>,
    bottomSheetState:Boolean,
    onHourChange:(String) -> Unit,
    onMinuteChange:(String) -> Unit,
    onSecondChange: (String) -> Unit,
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit,
    onButtonIntervalWorkChange: (Boolean) -> Unit,
    onButtonIntervalRestChange: (Boolean) -> Unit,
    onSetChange: (Int) -> Unit,
    onTypeChange: (Int) -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    onResumeButton: () -> Unit,
    onCancelButton: () -> Unit,
    onFinishButton: () -> Unit,
    onClickHabitButton: () -> Unit,
    onDismissHabitBottomSheet: () -> Unit,
    onAcceptBottomSheet: (Long, LocalDate) -> Unit,
    onClickCross:()->Unit = {},
    onClickTimeEntry: (Long) -> Unit = {},
    onClickFavorite: (Long,Boolean) -> Unit = {_,_ ->},
    onClickDelete: (Long) -> Unit = {_ -> },
){

    AnimatedContent(
        targetState = timerStopWatchUIState,
        contentKey = { it::class }
    ) { timerStopWatchUIStateAnim ->

        when(timerStopWatchUIStateAnim){
            TimerServiceUIState.NoTimer -> {
                VerticalChoseTimerScreen(
                    timerUIState = timerUIState,
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
                    onClickHabitButton = onClickHabitButton,
                    onDismissHabitBottomSheet = onDismissHabitBottomSheet,
                    onAcceptBottomSheet = onAcceptBottomSheet,
                    onClickCross = onClickCross,
                    onClickTimeEntry = onClickTimeEntry,
                    onClickFavorite = onClickFavorite,
                    onClickDelete = onClickDelete
                )
            }
            is TimerServiceUIState.TimerRunning -> {
                VerticalActiveTimerScreen(
                    timerStopWatchUIState = timerStopWatchUIStateAnim,
                    onStopService = onStopService,
                    onCancelButton = onCancelButton,
                    onResumeButton = onResumeButton,
                    onFinishButton = onFinishButton
                )
            }
        }
    }

}