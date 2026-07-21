package aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.vertical

import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.model.TypePickState
import aeb.proyecto.timer.components.common.infinitePicker.AlertDialogPicker
import aeb.proyecto.timer.components.common.infinitePicker.DialogDataTimerScreen
import aeb.proyecto.timer.components.common.infinitePicker.getCenteredIndex
import aeb.proyecto.timer.components.common.timePicker.TimerPicker
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.timer.constants.hours
import aeb.proyecto.timer.constants.minutes
import aeb.proyecto.timer.constants.seconds
import aeb.proyecto.timer.model.HourSelectedState
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.TitleLargeText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Orchestrates the selection of time intervals using three independent
 * numeric wheels. Manages the state synchronization between the wheels and
 * the editing dialog for granular adjustments.
 *
 * @param modifier Applied to the picker container.
 * @param hourSelectedState Current state (Data or NoData) of the selection.
 * @param label The section title or description.
 * @param typePickState Defines the active picking mode.
 * @param onIntervalHourChange Callback to return the final selected time (HH, MM, SS)
 *        and the associated timer key.
 * @param onDismissRequest Action to trigger when closing the picker view.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalTimePicker(
    modifier:Modifier = Modifier,
    hourSelectedState: HourSelectedState,
    label:String,
    typePickState: TypePickState,
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit,
    onDismissRequest: () -> Unit,
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var dialogData by remember { mutableStateOf(DialogDataTimerScreen()) }

    var hourState by remember { mutableStateOf("00") }
    var minuteState by remember { mutableStateOf("00") }
    var secondState by remember { mutableStateOf("00") }

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

    CustomBottomSheet (
        modifier = modifier,
        sheetState = sheetState,
        onDismiss = onDismissRequest,
    ){

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing8, end = spacing8, bottom = spacing20),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing8)
            ) {
                TitleLargeText(
                    label,
                    modifier = Modifier.align(Alignment.Center)
                )

                CustomRipple{
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "close button",
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.TopEnd)
                            .clickable {
                                scope.launch {
                                    sheetState.hide()
                                    onDismissRequest()
                                }
                            },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            TimerPicker(
                modifier = Modifier.padding(top = spacing20),
                colorGradient = MaterialTheme.colorScheme.primaryContainer,
                hourListState = hourListState,
                minuteListState = minuteListState,
                secondListState = secondListState,
                onHourChange = { hour -> hourState = hour },
                onMinuteChange = { minute -> minuteState = minute },
                onSecondChange = { second -> secondState = second },
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


            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = spacing20, start = spacing12, end = spacing12)
            ) {

                CustomRipple {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("vertical_picker_cancel_button"),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                onDismissRequest()
                            }
                        },
                        shape = RoundedCornerShape(spacing8)
                    ) {
                        LabelLargeText(stringResource(R.string.timer_cancel))
                    }
                }

                Spacer(modifier = Modifier.padding(horizontal = spacing8))

                CustomRipple {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("vertical_picker_accept_button"),
                        onClick = {
                            scope.launch {
                                onIntervalHourChange(
                                    Triple(hourState,minuteState,secondState),
                                    typePickState.value
                                )
                                sheetState.hide()
                                onDismissRequest()
                            }
                        },
                        shape = RoundedCornerShape(spacing8),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        LabelLargeText(
                            stringResource(R.string.timer_accept),
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }

            }
        }
    }

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