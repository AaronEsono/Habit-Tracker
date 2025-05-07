package aeb.proyecto.timer.components.typeSegmentedScreen

import aeb.proyecto.stopwatch.utils.pad
import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.bottomSheet.pickTime.PickTimeBottomSheet
import aeb.proyecto.timer.components.bottomSheet.pickTime.model.PickHourState
import aeb.proyecto.timer.components.bottomSheet.pickTime.model.TypePickState
import aeb.proyecto.timer.components.bottomSheet.pickTime.model.TypeTimer
import aeb.proyecto.timer.model.HourSelectedState
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleMediumText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

@Composable
fun IntervalSegmentedScreen(
    hourSelectedState: HourSelectedState,
    restSelectedState: HourSelectedState,
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit,
){

    var pickHourState by remember { mutableStateOf(PickHourState()) }

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

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            TitleMediumText(stringResource(R.string.timer_interval_work))

            LabelMediumText(
                stringResource(
                    R.string.timer_interval_work_time,
                    currentTimer.first.pad(),
                    currentTimer.second.pad(),
                    currentTimer.third.pad()
                ),
                fontSize = 44.sp,
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null
                ){
                    pickHourState = PickHourState(
                        showDialog = true,
                        typeTimer = TypeTimer.WORK_TIME,
                        hourState = HourSelectedState.Data(currentTimer)
                    )
                }
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            TitleMediumText(stringResource(R.string.timer_interval_rest))

            LabelMediumText(
                stringResource(R.string.timer_interval_rest_time,
                    currentRest.first.pad(),
                    currentRest.second.pad(),
                    currentRest.third.pad()),
                fontSize = 44.sp,
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null
                ){
                    pickHourState = PickHourState(
                        showDialog = true,
                        typeTimer = TypeTimer.REST_TIME,
                        hourState = HourSelectedState.Data(currentRest)
                    )
                }
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            TitleMediumText(stringResource(R.string.timer_interval_sets))

            LabelMediumText(
                "1",
                fontSize = 44.sp,
            )
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

}