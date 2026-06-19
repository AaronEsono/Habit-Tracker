package aeb.proyecto.timer.model

import aeb.proyecto.timer.R
import aeb.proyecto.ui.timer.TimerIndex
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines the available timer modes.
 * Maps each mode to its corresponding string resource, icon, and unique key.
 *
 * @property title Resource ID of the display label.
 * @property icon The [ImageVector] representing the mode visually.
 * @property key The unique integer key for data mapping.
 */
enum class SegmentedButtonOptions(
    @StringRes val title: Int,
    val icon: ImageVector,
    val key: Int
) {
    StopWatch(R.string.timer_segmented_button_stopwatch, Icons.Filled.Timer, TimerIndex.STOPWATCH),

    Timer(R.string.timer_segmented_button_timer, Icons.Filled.HourglassBottom, TimerIndex.TIMER),

    Interval(R.string.timer_segmented_button_interval, Icons.Filled.Timelapse, TimerIndex.INTERVAL)
}

/**
 * Utility to retrieve the [SegmentedButtonOptions] enum constant based on its integer key.
 * * @param index The key to look for.
 * @return The corresponding [SegmentedButtonOptions].
 * @throws NoSuchElementException if no match is found.
 */
fun getSegmentedButtonOptions(index: Int): SegmentedButtonOptions {
    return  SegmentedButtonOptions.entries.first { it.key == index }
}