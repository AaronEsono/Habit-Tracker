package aeb.proyecto.timer.components.horizontal.components.screens

import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.components.commom.bottomSheet.pickHabit.PickHabitBottomSheet
import aeb.proyecto.timer.components.commom.button.AcceptButton
import aeb.proyecto.timer.components.commom.habitLinked.HabitLinkedButton
import aeb.proyecto.timer.components.commom.segmentedRow.SegmentedRow
import aeb.proyecto.timer.components.commom.timeEntry.TimeEntry
import aeb.proyecto.timer.components.commom.timeEntry.TimeEntryHeader
import aeb.proyecto.timer.components.commom.typeTimer.intervalSegmented.IntervalSegmentedScreen
import aeb.proyecto.timer.components.commom.typeTimer.StopWatchSegmentedScreen
import aeb.proyecto.timer.components.commom.typeTimer.TimerSegmentedScreen
import aeb.proyecto.timer.model.SegmentedButtonOptions
import aeb.proyecto.timer.model.TimeEntryState
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalDate

@Composable
fun HorizontalChoseTimerScreen(
    timerUIState: TimerUiState.Success,
    listTimeEntryState: TimeEntryState,
    triggerSegmentedTimer: Triple<Int,Int,Int>?,
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
    onAcceptBottomSheet: (Long, LocalDate) -> Unit,
    onClickCross:()->Unit = {},
    onClickTimeEntry: (Long) -> Unit = {},
    onClickFavorite: (Long,Boolean) -> Unit = {_,_ ->},
    onClickDelete: (Long) -> Unit = {_ -> },
){
    val segmentedOptions = remember { SegmentedButtonOptions.entries }

    Row {
        Column (
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(),
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

        Spacer(modifier = Modifier.padding(horizontal = spacing4))

        Column (
            modifier = Modifier.fillMaxSize()
                .padding(top = spacing8)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            SegmentedRow(
                modifier = Modifier.fillMaxWidth().padding(top = spacing8),
                segmentedList = segmentedOptions,
                onClickOption = onTypeChange,
                typeTimer = timerUIState.timerDataUIState.typeTimer,
            )

            HabitLinkedButton(
                modifier = Modifier.padding(top = spacing16),
                linkedState = timerUIState.timerDataUIState.habitLinked,
                onClickHabitLinkedButton = onClickHabitButton,
                onClickCross = onClickCross
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing16),
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