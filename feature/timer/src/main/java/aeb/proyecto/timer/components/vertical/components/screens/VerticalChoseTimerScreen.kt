package aeb.proyecto.timer.components.vertical.components.screens

import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.components.common.bottomSheet.pickHabit.PickHabitBottomSheet
import aeb.proyecto.timer.components.common.button.AcceptButton
import aeb.proyecto.timer.components.common.habitLinked.HabitLinkedButton
import aeb.proyecto.timer.components.common.segmentedRow.SegmentedRow
import aeb.proyecto.timer.components.common.timeEntry.TimeEntry
import aeb.proyecto.timer.components.common.timeEntry.TimeEntryHeader
import aeb.proyecto.timer.components.common.typeTimer.StopWatchSegmentedScreen
import aeb.proyecto.timer.components.common.typeTimer.TimerSegmentedScreen
import aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.IntervalSegmentedScreen
import aeb.proyecto.timer.model.SegmentedButtonOptions
import aeb.proyecto.timer.model.TimeEntryState
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.SharedFlow
import java.time.LocalDate

/**
 * Vertical configuration screen for the Timer module.
 * Provides a stacked layout: the top half is dedicated to timer configuration
 * (mode selection, duration, interval settings), while the bottom half displays
 * the history of time entries, optimized for single-handed portrait interaction.
 *
 * @param timerUIState The current UI state representing the timer configuration.
 * @param listTimeEntryState The state holding the historical [TimeEntry] data.
 * @param triggerSegmentedTimer Flow to handle reset/update events for the pickers.
 * @param bottomSheetState Visibility state for the habit selection sheet.
 * @param onHourChange/onMinuteChange/onSecondChange Callbacks for time components.
 * @param onTypeChange Callback to change the active timer mode.
 * @param onStartService Action to initialize the background timer service.
 * @param onSetChange Callback to update the number of intervals.
 * @param onIntervalHourChange Callback to return the modified interval time and key.
 * @param onButtonIntervalWorkChange/onButtonIntervalRestChange Mode toggles for work/rest.
 * @param onClickHabitButton Trigger for the habit selection sheet.
 * @param onDismissHabitBottomSheet Callback to close the habit selection sheet.
 * @param onAcceptBottomSheet Callback to save the selected habit association.
 * @param onClickCross Action for the close button.
 * @param onClickTimeEntry/onClickFavorite/onClickDelete Management actions for history items.
 */
@Composable
fun VerticalChoseTimerScreen(
    timerUIState: TimerUiState.Success,
    listTimeEntryState: TimeEntryState,
    triggerSegmentedTimer: SharedFlow<Triple<Int, Int, Int>?>,
    bottomSheetState:Boolean,
    onHourChange:(String) -> Unit,
    onMinuteChange:(String) -> Unit,
    onSecondChange: (String) -> Unit,
    onTypeChange: (Int) -> Unit,
    onStartService: () -> Unit,
    onSetChange: (Int) -> Unit,
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit,
    onButtonIntervalWorkChange: (Boolean) -> Unit,
    onButtonIntervalRestChange: (Boolean) -> Unit,
    onClickHabitButton: () -> Unit,
    onDismissHabitBottomSheet: () -> Unit,
    onAcceptBottomSheet: (Long,LocalDate) -> Unit,
    onClickCross:()->Unit = {},
    onClickTimeEntry: (Long) -> Unit = {},
    onClickFavorite: (Long,Boolean) -> Unit = {_,_ ->},
    onClickDelete: (Long) -> Unit = {_ -> },
){
    val segmentedOptions = remember { SegmentedButtonOptions.entries }

    Column (){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            AnimatedContent(
                targetState = timerUIState.timerDataUIState.typeTimer,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith  ExitTransition.None
                }
            )
            { typeTimer ->

                when (typeTimer) {
                    SegmentedButtonOptions.StopWatch -> {
                        StopWatchSegmentedScreen()
                    }

                    SegmentedButtonOptions.Timer -> {
                        TimerSegmentedScreen(
                            hourSelectedState = timerUIState.timerDataUIState.hourSelected,
                            triggerSegmentedTimer = triggerSegmentedTimer,
                            onHourChange = onHourChange,
                            onMinuteChange = onMinuteChange,
                            onSecondChange = onSecondChange
                        )
                    }

                    SegmentedButtonOptions.Interval -> {
                        IntervalSegmentedScreen(
                            hourSelectedState = timerUIState.timerDataUIState.hourSelected,
                            restSelectedState = timerUIState.timerDataUIState.restHour,
                            setInterval = timerUIState.timerDataUIState.sets,
                            onSetIntervalChange = onSetChange,
                            onClickButtonWorkTime = onButtonIntervalWorkChange,
                            onIntervalHourChange = onIntervalHourChange,
                            onClickButtonRestTime = onButtonIntervalRestChange
                        )
                    }
                }
            }
        }

        Column (
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
        ){
            SegmentedRow(
                modifier = Modifier.fillMaxWidth().padding(top = spacing8),
                segmentedList = segmentedOptions,
                onClickOption = onTypeChange,
                typeTimer = timerUIState.timerDataUIState.typeTimer,
            )

            HabitLinkedButton(
                modifier = Modifier.padding(top = spacing16, bottom = spacing16),
                linkedState = timerUIState.timerDataUIState.habitLinked,
                onClickHabitLinkedButton = onClickHabitButton,
                onClickCross = onClickCross
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                AcceptButton(
                    timerUIState = timerUIState,
                    onStartService = onStartService
                )
            }

            val timeEntries = remember (listTimeEntryState){
                if (listTimeEntryState is TimeEntryState.TimeEntries) {
                    listTimeEntryState.timeEntries
                } else {
                    emptyList()
                }
            }

            AnimatedVisibility(
                visible = listTimeEntryState is TimeEntryState.TimeEntries,
            ) {
                val lastEntry = timeEntries.lastOrNull()

                Column {
                    TimeEntryHeader(modifier = Modifier.padding(top = spacing16))

                    timeEntries.forEach { timeEntry ->
                        key(timeEntry.timeEntry.id) {
                            TimeEntry(
                                timeEntry = timeEntry,
                                lastOne = timeEntry == lastEntry,
                                onClickTimeEntry = onClickTimeEntry,
                                onClickFavorite = onClickFavorite,
                                onClickDelete = onClickDelete
                            )
                        }
                    }
                }
            }
        }

    }

    if(bottomSheetState){
        PickHabitBottomSheet(
            onDismiss = onDismissHabitBottomSheet,
            habitLinkedState = timerUIState.timerDataUIState.habitLinked,
            onAccept = onAcceptBottomSheet
        )
    }

}