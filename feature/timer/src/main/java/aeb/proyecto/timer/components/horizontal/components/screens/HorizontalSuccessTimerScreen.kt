package aeb.proyecto.timer.components.horizontal.components.screens

import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.model.TimeEntryState
import aeb.proyecto.timer.model.TimerServiceUIState
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import java.time.LocalDate

@Composable
fun HorizontalSuccessTimerScreen(
    timerUIState: TimerUiState.Success,
    timerStopWatchUIState: TimerServiceUIState,
    listTimeEntryState: TimeEntryState,
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
                HorizontalChoseTimerScreen(
                    timerUIState = timerUIState,
                    listTimeEntryState = listTimeEntryState,
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
                HorizontalActiveTimerScreen(
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