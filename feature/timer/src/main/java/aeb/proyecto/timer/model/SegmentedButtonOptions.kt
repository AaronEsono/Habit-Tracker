package aeb.proyecto.timer.model

import aeb.proyecto.timer.R
import aeb.proyecto.ui.timer.TimerIndex
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

enum class SegmentedButtonOptions(
    @StringRes val title: Int,
    val icon: ImageVector,
    val key: Int
) {
    StopWatch(R.string.timer_segmented_button_stopwatch, Icons.Filled.Timer, TimerIndex.STOPWATCH),

    Timer(R.string.timer_segmented_button_timer, Icons.Filled.HourglassBottom, TimerIndex.TIMER),

    Interval(R.string.timer_segmented_button_interval, Icons.Filled.Timelapse, TimerIndex.INTERVAL)
}

fun getSegmentedButtonOptions(index: Int): SegmentedButtonOptions {
    return  SegmentedButtonOptions.entries.first { it.key == index }
}