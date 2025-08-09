package aeb.proyecto.timer.components.vertical

import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.components.commom.loading.TimerLoading
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
import java.time.LocalDate

@Composable
fun VerticalTimerScreen(
    timerUiState: TimerUiState,
    timerStopWatchUIState: TimerServiceUIState,
    listTimeEntryState: TimeEntryState,
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
                    onClickFavorite = onClickFavorite,
                    onClickDelete = onClickDelete
                )
            }
        }
    }

}