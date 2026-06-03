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


/**
 * High-performance, multi-purpose structural grid matrix layout rendering a normalized 6x7 calendar sheet.
 * Pairs continuous mathematical position indexing loops with dynamic lifecycle animation anchor keys,
 * delegating the individual cell presentation design up to down-stream consumer layers via slot APIs.
 *
 * @param T The polymorphic business entity payload token tied to the grid calculations.
 * @param modifier The structural composition modifier layout adjustment token.
 * @param verticalPadding Inter-row layout bounding space separating calendar weeks.
 * @param horizontalPadding Inter-column structural gap separating calendar day columns.
 * @param dates The flat sequence list containing compiled structural matrix data cells.
 * @param itemContent Declarative architectural slot lambda tasked with rendering individual date nodes.
 */
@Composable
fun <T> CalendarContent(
    modifier:Modifier = Modifier,
    verticalPadding:Dp = 0.dp,
    horizontalPadding:Dp = 0.dp,
    dates: List<CalendarUIState.DateCalendar<T>>,
    itemContent: @Composable (CalendarUIState.DateCalendar<T>?,modifier: Modifier) -> Unit
){

    // Generate a fresh unique object key on every date data mutation to clean reset stagger animation loops
    val animationKey = remember(dates) { Any() }

    Column (
        modifier = modifier
    ){
        var index = 0

        // Construct a rigid 6-row matrix frame mapping calendar weeks uniformly
        repeat(6) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = verticalPadding),
                horizontalArrangement = Arrangement.spacedBy(horizontalPadding)){

                // Squeeze exactly 7 proportional columns filling individual days of the week row
                repeat(7) {
                    val currentIndex = index
                    val item = if (index < dates.size) dates[index] else null

                    // Encase every grid cell vertex into an isolated staggered transition framework
                    AnimatedCalendarItemContainer(
                        index = currentIndex,
                        key = animationKey,
                        modifier = Modifier.weight(1f) // Binds perfect proportional scaling across device scales
                    ) { animatedModifier ->
                        itemContent(item, animatedModifier)
                    }

                    index++
                }
            }
        }
    }
}

/**
 * Functional extension predicate evaluating whether a specific date falls within the matching
 * chronological year and month boundary profile.
 * Useful for filtering out boundary-overflow grid padding items.
 */
fun LocalDate.isInYearMonth(yearMonth: YearMonth): Boolean {
    return this.year == yearMonth.year && this.month == yearMonth.month
}

/**
 * High-performance animation enclosure designed to orchestrate a staggered cascading entrance
 * effect across calendar date items.
 * Utilizes low-level graphics layout layers to isolate transformation updates straight to GPU vectors,
 * bypassing CPU composition cycles during continuous interpolation loops.
 *
 * @param index The chronological flat location coordinates within the calendar matrix (0 to 41).
 * @param key Unique identification key context token used to trigger reset behaviors upon state switches.
 * @param modifier The structural composition modifier layout adjustment token.
 * @param content Functional composable inner slot container receiving targeted transformation properties.
 */
@Composable
private fun AnimatedCalendarItemContainer(
    index: Int,
    key: Any,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    // Track execution visibility bound explicitly to the structural grid key matrix instance
    var visible by remember(key) { mutableStateOf(false) }

    // Implement sequential sub-second staggering delays relative to individual grid entry positions
    LaunchedEffect(key) {
        delay(index * 5L) // Linear incremental progression (~210ms total sweep window for a 42-day sheet)
        visible = true
    }

    // Interpolate alpha opacity visibility tracks smoothly
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "calendar_item_alpha"
    )

    // Interpolate dynamic scale dimensions utilizing low-vibration spring kinematics
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.95f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "calendar_item_scale"
    )

    // Render contents inside an isolated hardware layer profile
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