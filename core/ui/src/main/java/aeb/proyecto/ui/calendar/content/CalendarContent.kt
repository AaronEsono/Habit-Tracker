package aeb.proyecto.ui.calendar.content

import aeb.proyecto.ui.calendar.model.CalendarUIState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun <T> CalendarContent(
    modifier:Modifier = Modifier,
    verticalPadding:Dp = 0.dp,
    horizontalPadding:Dp = 0.dp,
    dates: List<CalendarUIState.DateCalendar<T>>,
    itemContent: @Composable (CalendarUIState.DateCalendar<T>?,modifier: Modifier) -> Unit
){

    val animationKey = remember(dates) { Any() }

    Column (
        modifier = modifier
    ){
        var index = 0
        repeat(6) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = verticalPadding),
                horizontalArrangement = Arrangement.spacedBy(horizontalPadding)){
                repeat(7) {
                    val currentIndex = index
                    val item = if (index < dates.size) dates[index] else null

                    // Creamos un contenedor animado para cada item
                    AnimatedCalendarItemContainer(
                        index = currentIndex,
                        key = animationKey,
                        modifier = Modifier.weight(1f)
                    ) { animatedModifier ->
                        itemContent(item, animatedModifier)
                    }

                    index++
                }
            }
        }
    }
}

fun LocalDate.isInYearMonth(yearMonth: YearMonth): Boolean {
    return this.year == yearMonth.year && this.month == yearMonth.month
}


@Composable
private fun AnimatedCalendarItemContainer(
    index: Int,
    key: Any,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    var visible by remember(key) { mutableStateOf(false) }

    // El delay aumenta según el índice (efecto escalera)
    LaunchedEffect(key) {
        delay(index * 5L) // 5ms por item = ~200ms para llenar el calendario
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "calendar_item_alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.95f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "calendar_item_scale"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha
                this.scaleX = scale
                this.scaleY = scale
            }
    ) {
        content(Modifier)
    }
}