package aeb.proyecto.timer.components.commom.typeTimer

import aeb.proyecto.timer.components.infinitePicker.AlertDialogPicker
import aeb.proyecto.timer.components.infinitePicker.DialogDataTimerScreen
import aeb.proyecto.timer.components.commom.infinitePicker.getCenteredIndex
import aeb.proyecto.timer.components.timerPicker.TimerPicker
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.timer.constants.hours
import aeb.proyecto.timer.constants.minutes
import aeb.proyecto.timer.constants.seconds
import aeb.proyecto.timer.model.HourSelectedState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

@Composable
fun TimerSegmentedScreen(
    hourSelectedState: HourSelectedState,
    onHourChange:(String) -> Unit,
    onMinuteChange:(String) -> Unit,
    onSecondChange: (String) -> Unit
){

    val scope = rememberCoroutineScope()
    var dialogData by remember { mutableStateOf(DialogDataTimerScreen()) }

    val firstTimer = remember {
        if (hourSelectedState is HourSelectedState.NoData) {
            Triple(0, 0,0)
        } else {
            hourSelectedState as HourSelectedState.Data
            Triple(hourSelectedState.data.first, hourSelectedState.data.second, hourSelectedState.data.third)
        }
    }

    val hourListState = rememberLazyListState(
        initialFirstVisibleItemIndex = getCenteredIndex(hours.size, firstTimer.first)
    )

    val minuteListState = rememberLazyListState(
        initialFirstVisibleItemIndex = getCenteredIndex(minutes.size, firstTimer.second)
    )

    val secondListState = rememberLazyListState(
        initialFirstVisibleItemIndex = getCenteredIndex(seconds.size, firstTimer.third)
    )

    TimerPicker(
        hourListState = hourListState,
        minuteListState = minuteListState,
        secondListState = secondListState,
        onHourChange = onHourChange,
        onMinuteChange = onMinuteChange,
        onSecondChange = onSecondChange,
        scrollToItemHour = {index -> scope.launch { hourListState.scrollToItem(index) } },
        scrollToItemMinute = {index -> scope.launch { minuteListState.scrollToItem(index) } },
        scrollToItemSecond = {index -> scope.launch { secondListState.scrollToItem(index) } },
        onClickCenterHour = {
            dialogData = dialogData.copy(
                showDialog = true,
                typeUnitDate = TypeUnitDate.Hours,
                initialText = it
            )
        },
        onClickCenterMinute = {
            dialogData = dialogData.copy(
                showDialog = true,
                typeUnitDate = TypeUnitDate.Minutes,
                initialText = it
            )
        },
        onClickCenterSecond = {
            dialogData = dialogData.copy(
                showDialog = true,
                typeUnitDate = TypeUnitDate.Seconds,
                initialText = it
            )
        }
    )

    if(dialogData.showDialog){
        AlertDialogPicker(
            typeList = dialogData.typeUnitDate,
            initialText = dialogData.initialText,
            onDismissRequest = {
                dialogData = dialogData.copy(showDialog = false)
            },
            onAccept = { index ->
                scope.launch {
                    when(dialogData.typeUnitDate){
                        TypeUnitDate.Hours -> {
                            hourListState.animateScrollToItem(getCenteredIndex(hours.size, index))
                        }
                        TypeUnitDate.Minutes -> {
                            minuteListState.animateScrollToItem(getCenteredIndex(minutes.size, index))
                        }
                        TypeUnitDate.Seconds -> {
                            secondListState.animateScrollToItem(getCenteredIndex(seconds.size, index))
                        }
                    }
                }
            }
        )
    }
}