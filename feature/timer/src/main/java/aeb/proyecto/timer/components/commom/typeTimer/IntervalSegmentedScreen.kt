package aeb.proyecto.timer.components.commom.typeTimer

import aeb.proyecto.stopwatch.utils.pad
import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.bottomSheet.pickTime.PickTimeBottomSheet
import aeb.proyecto.timer.components.bottomSheet.pickTime.model.PickHourState
import aeb.proyecto.timer.components.bottomSheet.pickTime.model.TypeTimer
import aeb.proyecto.timer.components.button.InternalSegmentedButton
import aeb.proyecto.timer.components.textField.TimerTextField
import aeb.proyecto.timer.model.HourSelectedState
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.regexTextField.OneTo99
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleMediumText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IntervalSegmentedScreen(
    hourSelectedState: HourSelectedState,
    restSelectedState: HourSelectedState,
    setInterval:Int,
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit,
    onClickButtonWorkTime: (Boolean) -> Unit,
    onClickButtonRestTime: (Boolean) -> Unit,
    onSetIntervalChange: (Int) -> Unit
){

    var pickHourState by remember { mutableStateOf(PickHourState()) }
    var showSetDialog by remember { mutableStateOf(false) }

    val currentTimer = remember (hourSelectedState){
        if (hourSelectedState is HourSelectedState.NoData) {
            Triple(0, 0, 0)
        } else {
            (hourSelectedState as HourSelectedState.Data).data
        }
    }

    val currentRest = remember (restSelectedState){
        if (restSelectedState is HourSelectedState.NoData) {
            Triple(0, 0, 0)
        } else {
            (restSelectedState as HourSelectedState.Data).data
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val height = maxHeight
        val density = LocalDensity.current
        //Padding entre elementos y bordes ( 3 elementos = 2 entre elementos + 2 bordes)
        val padding = spacing8

        val baseHeight = remember (height, padding){
            val totalHeight = height - (padding * 4)
            //Separamos el width en 3 partes para cada infinitePicker
            (totalHeight / 3)
        }

        val (titleFontSize, contentFontSize,contentHeight) = remember(maxHeight, density) {
            val totalHeight = maxHeight - (padding * 4)
            val minHeightThreshold = 250.dp

            val titleHeight = if (totalHeight < minHeightThreshold) baseHeight * 0.25f
            else baseHeight * 0.25f * 0.8f

            val contentHeight = if (totalHeight < minHeightThreshold) baseHeight * 0.6f
            else baseHeight * 0.6f * 0.75f

            with(density) {
                Triple(titleHeight.toSp(), contentHeight.toSp(),contentHeight)
            }
        }

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Column(
                modifier = Modifier.fillMaxWidth().height(baseHeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                TitleMediumText(
                    stringResource(R.string.timer_interval_work),
                    fontSize = titleFontSize)

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ){
                    InternalSegmentedButton (
                        icon = Icons.Filled.Remove,
                        size = contentHeight
                    ){
                        onClickButtonWorkTime(false)
                    }

                    LabelMediumText(
                        stringResource(
                            R.string.timer_interval_work_time,
                            currentTimer.first.pad(),
                            currentTimer.second.pad(),
                            currentTimer.third.pad()
                        ),
                        textAlign = TextAlign.Center,
                        fontSize = contentFontSize,
                        modifier = Modifier.clickable(
                            interactionSource = null,
                            indication = null
                        ){
                            pickHourState = PickHourState(
                                showDialog = true,
                                typeTimer = TypeTimer.WORK_TIME,
                                hourState = HourSelectedState.Data(currentTimer)
                            )
                        }.wrapContentWidth()
                            .padding(horizontal = spacing12)
                    )

                    InternalSegmentedButton (
                        size = contentHeight
                    ){
                        onClickButtonWorkTime(true)
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().height(baseHeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                TitleMediumText(stringResource(R.string.timer_interval_rest),
                    fontSize = titleFontSize)

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    InternalSegmentedButton (
                        icon = Icons.Filled.Remove,
                        size = contentHeight
                    ){
                        onClickButtonRestTime(false)
                    }


                    LabelMediumText(
                        stringResource(R.string.timer_interval_rest_time,
                            currentRest.first.pad(),
                            currentRest.second.pad(),
                            currentRest.third.pad()),
                        fontSize = contentFontSize,
                        modifier = Modifier.clickable(
                            interactionSource = null,
                            indication = null
                        ){
                            pickHourState = PickHourState(
                                showDialog = true,
                                typeTimer = TypeTimer.REST_TIME,
                                hourState = HourSelectedState.Data(currentRest)
                            )
                        }.wrapContentWidth()
                            .padding(horizontal = spacing12)
                    )

                    InternalSegmentedButton (
                        size = contentHeight
                    ){
                        onClickButtonRestTime(true)
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().height(baseHeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                TitleMediumText(stringResource(R.string.timer_interval_sets),
                    fontSize = titleFontSize)

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    InternalSegmentedButton (
                        icon = Icons.Filled.Remove,
                        size = contentHeight
                    ){
                        onSetIntervalChange(setInterval - 1)
                    }

                    LabelMediumText(
                        setInterval.toString(),
                        fontSize = contentFontSize,
                        modifier = Modifier
                            .width(contentHeight * 2.5f)
                            .padding(horizontal = spacing20)
                            .clickable {
                                showSetDialog = true
                            },
                        textAlign = TextAlign.Center
                    )

                    InternalSegmentedButton (
                        size = contentHeight
                    ){
                        onSetIntervalChange(setInterval + 1)
                    }
                }
            }
        }
    }

    if(pickHourState.showDialog){
        PickTimeBottomSheet(
            hourSelectedState = pickHourState.hourState,
            typePickState = pickHourState.typeTimer.typePickState,
            label = stringResource(pickHourState.typeTimer.label),
            onIntervalHourChange = onIntervalHourChange,
            onDismissRequest = { pickHourState = pickHourState.copy(showDialog = false) }
        )
    }

    if(showSetDialog){
        SetDialog(
            initialText = setInterval.toString(),
            onDismissRequest = { showSetDialog = false },
            onAccept = { onSetIntervalChange(it) }
        )
    }

}


@Composable
fun SetDialog(
    initialText: String = "00",
    onDismissRequest: () -> Unit = {},
    onAccept: (Int) -> Unit = {}
){
    val textFieldState = rememberTextFieldState(initialText = initialText)

    OneTo99(textFieldState)

    CustomDialog(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(start = spacing12, end = spacing4, bottom = spacing8, top = spacing20),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                TimerTextField(
                    textFieldState = textFieldState,
                )

                LabelMediumText(
                    stringResource(R.string.timer_interval_sets),
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = spacing2)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing20),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                CustomRipple {
                    TextButton(
                        onClick = onDismissRequest,
                        shape = RoundedCornerShape(spacing12)
                    ) {
                        LabelLargeText(
                            stringResource(R.string.timer_cancel),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                CustomRipple {
                    TextButton(
                        onClick = {
                            val number = textFieldState.text.toString().toIntOrNull() ?: 0
                            onAccept(number)
                            onDismissRequest()
                        },
                        shape = RoundedCornerShape(spacing12)
                    ) {
                        LabelLargeText(
                            stringResource(R.string.timer_accept),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}