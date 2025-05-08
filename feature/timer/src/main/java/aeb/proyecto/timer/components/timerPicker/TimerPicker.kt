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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun TimerPicker(
    modifier: Modifier = Modifier,
    colorGradient: Color = MaterialTheme.colorScheme.background,
    hourListState:LazyListState = rememberLazyListState(),
    minuteListState:LazyListState = rememberLazyListState(),
    secondListState:LazyListState = rememberLazyListState(),
    onHourChange:(String) -> Unit,
    onMinuteChange:(String) -> Unit,
    onSecondChange: (String) -> Unit,
    scrollToItemHour: (Int) -> Unit = {},
    scrollToItemMinute: (Int) -> Unit = {},
    scrollToItemSecond: (Int) -> Unit = {},
    onClickCenterHour: (String) -> Unit = {},
    onClickCenterMinute: (String) -> Unit = {},
    onClickCenterSecond: (String) -> Unit = {}
) {

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
                modifier = Modifier.padding(bottom = spacing6),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            InfinitePicker(
                items = hours,
                listState = hourListState,
                colorGradient = colorGradient,
                currentItemSelected = onHourChange,
                scrollToItem = scrollToItemHour,
                onClickCenter = onClickCenterHour
                )
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
                modifier = Modifier.padding(bottom = spacing6),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                InfinitePicker(
                    items = minutes,
                    listState = minuteListState,
                    colorGradient = colorGradient,
                    currentItemSelected = onMinuteChange,
                    scrollToItem = scrollToItemMinute,
                    onClickCenter = onClickCenterMinute
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

        // SEGUNDOS
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LabelLargeText(
                stringResource(R.string.timer_seconds),
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = spacing6),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            InfinitePicker(
                items = seconds,
                listState = secondListState,
                colorGradient = colorGradient,
                currentItemSelected = onSecondChange,
                scrollToItem = scrollToItemSecond,
                onClickCenter = onClickCenterSecond
            )
        }
    }
}
