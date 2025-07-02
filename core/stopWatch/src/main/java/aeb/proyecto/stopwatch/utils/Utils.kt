package aeb.proyecto.stopwatch.utils

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.LocalDate
import kotlin.text.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}

fun longToHMS(milliseconds: Long): String {
    val seconds = milliseconds / 1000
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return "${hours.toInt().pad()}:${minutes.toInt().pad()}:${secs.toInt().pad()}"
}


fun getTextToday(date: LocalDate,context:Context):String{
    return when(date){
        LocalDate.now() -> context.getString(R.string.timer_today)
        LocalDate.now().plusDays(1) ->  context.getString(R.string.timer_tomorrow)
        LocalDate.now().minusDays(1) -> context.getString(R.string.timer_yesterday)
        else -> {date.toString()}
    }
}
