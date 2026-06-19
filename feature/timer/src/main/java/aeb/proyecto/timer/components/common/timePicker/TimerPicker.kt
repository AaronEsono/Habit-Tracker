package aeb.proyecto.timer.components.common.timePicker

import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.common.infinitePicker.InfinitePicker
import aeb.proyecto.timer.constants.hours
import aeb.proyecto.timer.constants.minutes
import aeb.proyecto.timer.constants.seconds
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A composite picker component that coordinates three [InfinitePicker] instances
 * (Hours, Minutes, Seconds) into a unified time selection interface.
 *
 * @param modifier Applied to the container.
 * @param colorGradient The background gradient color used for fading effects.
 * @param hourListState State for the hours list.
 * @param minuteListState State for the minutes list.
 * @param secondListState State for the seconds list.
 * @param onHourChange Callback when the hour changes.
 * @param onMinuteChange Callback when the minute changes.
 * @param onSecondChange Callback when the second changes.
 * @param scrollToItemHour/Minute/Second Functions to programmatically trigger scroll.
 * @param onClickCenterHour/Minute/Second Actions to perform when clicking the center of each wheel.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
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

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current

        val (rawFontSize, labelFontSize) = remember(maxWidth, maxHeight, density) {
            val totalWidth = maxWidth
            val totalHeight = maxHeight

            // Mínimo de height para reducir en casos muy pequeños
            val minHeightThreshold = 300.dp
            val isShortHeight = totalHeight < minHeightThreshold

            // Convertimos el ancho en Sp
            val baseFontSize = with(density) { (totalWidth / 3).toSp() }

            // Ajustamos el tamaño
            val adjustedBaseFontSize = if (isShortHeight) baseFontSize * 0.8f else baseFontSize

            val raw = adjustedBaseFontSize / 2.3f
            val label = raw * 0.3f

            raw to label
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // HORAS
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LabelLargeText(
                    stringResource(R.string.timer_hours),
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(bottom = spacing6),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = labelFontSize
                )

                InfinitePicker(
                    items = hours,
                    listState = hourListState,
                    colorGradient = colorGradient,
                    currentItemSelected = onHourChange,
                    scrollToItem = scrollToItemHour,
                    onClickCenter = onClickCenterHour,
                    fontSizeItem = rawFontSize
                )
            }

            LabelMediumText(
                stringResource(R.string.timer_dots),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = spacing4)
                    .offset(y = spacing8),
                fontSize = rawFontSize
            )

            // MINUTOS
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LabelLargeText(
                    stringResource(R.string.timer_minutes),
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(bottom = spacing6),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = labelFontSize
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    InfinitePicker(
                        items = minutes,
                        listState = minuteListState,
                        colorGradient = colorGradient,
                        currentItemSelected = onMinuteChange,
                        scrollToItem = scrollToItemMinute,
                        onClickCenter = onClickCenterMinute,
                        fontSizeItem = rawFontSize
                    )
                }
            }

            LabelMediumText(
                stringResource(R.string.timer_dots),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = spacing4)
                    .offset(y = spacing8),
                fontSize = rawFontSize
            )

            // SEGUNDOS
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LabelLargeText(
                    stringResource(R.string.timer_seconds),
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(bottom = spacing6),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = labelFontSize
                )

                InfinitePicker(
                    items = seconds,
                    listState = secondListState,
                    colorGradient = colorGradient,
                    currentItemSelected = onSecondChange,
                    scrollToItem = scrollToItemSecond,
                    onClickCenter = onClickCenterSecond,
                    fontSizeItem = rawFontSize
                )
            }
        }
    }
}
