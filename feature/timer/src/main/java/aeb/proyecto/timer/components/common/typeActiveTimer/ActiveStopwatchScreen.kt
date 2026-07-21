package aeb.proyecto.timer.components.common.typeActiveTimer

import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp

/**
 * A simple, high-visibility display component for the stopwatch timer.
 * Focuses on readability by centering the time string with a large font size.
 *
 * @param hour The formatted time string to display (e.g., "00:00:00").
 */
@Composable
fun ActiveStopwatchScreen(
    hour:String
){
    LabelMediumText(
        hour,
        fontSize = 60.sp,
        modifier = Modifier.testTag("timer_stopwatch_label")
    )
}