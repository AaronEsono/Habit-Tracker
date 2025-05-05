package aeb.proyecto.timer.components.timerPicker

import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.infinitePicker.InfinitePicker
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.timer.constants.hours
import aeb.proyecto.timer.constants.minutes
import aeb.proyecto.timer.constants.seconds
import aeb.proyecto.timer.model.HourSelectedState
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp


@Composable
fun TimerPicker(
    modifier: Modifier = Modifier,
    timerSelected: HourSelectedState,
    onHourChange:(String) -> Unit,
    onMinuteChange:(String) -> Unit,
    onSecondChange: (String) -> Unit
) {


    var currentTimer = remember {
        if(timerSelected is HourSelectedState.NoData){
            Triple(0,0,0)
        }else{
            (timerSelected as HourSelectedState.Data).data
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically // <- los pickers y puntos alineados verticalmente
    ) {
        // HORAS
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LabelLargeText(
                stringResource(R.string.timer_hours),
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = spacing6)
            )

            InfinitePicker(
                items = hours,
                startIndex = currentTimer.first,
                typeList = TypeUnitDate.Hours,
                alertDialogTitle = stringResource(R.string.timer_hours),
                onTextSelected = onHourChange)
        }

        LabelMediumText(
            stringResource(R.string.timer_dots),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = spacing4)
                .offset(y = spacing8),
            fontSize = 50.sp
        )

        // MINUTOS
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LabelLargeText(
                stringResource(R.string.timer_minutes),
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = spacing6)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                InfinitePicker(
                    items = minutes,
                    startIndex = currentTimer.second,
                    typeList = TypeUnitDate.Minutes,
                    alertDialogTitle = stringResource(R.string.timer_minutes),
                    onTextSelected = onMinuteChange
                )
            }
        }

        LabelMediumText(
            stringResource(R.string.timer_dots),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = spacing4)
                .offset(y = spacing8),
            fontSize = 50.sp
        )

        // SEGUNDOS (sin puntos)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LabelLargeText(
                stringResource(R.string.timer_seconds),
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = spacing6)
            )

            // Solo el picker
            InfinitePicker(
                items = seconds,
                startIndex = currentTimer.third,
                typeList = TypeUnitDate.Seconds,
                alertDialogTitle = stringResource(R.string.timer_seconds),
                onTextSelected = onSecondChange
            )
        }
    }
}
