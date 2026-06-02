package aeb.proyecto.stopwatch.utils

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import java.time.LocalDate
import kotlin.text.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Extension utility formatting integers into structured two-digit string patterns.
 * Core wrapper ensuring consistent visual layouts for time units (e.g., converts '9' to "09").
 *
 * @return A left-padded two-character [String] representation of the digit.
 */
fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}

/**
 * Transforms raw millisecond counts into an uniform human-readable chronometer text format.
 *
 * Used downstream by reactive state managers to feed presentation layers and persistent alerts.
 *
 * @param milliseconds Cumulative running timeline duration value.
 * @return A formatted [String] following the standard "HH:MM:SS" layout grid.
 */
fun longToHMS(milliseconds: Long): String {
    val seconds = milliseconds / 1000
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return "${hours.toInt().pad()}:${minutes.toInt().pad()}:${secs.toInt().pad()}"
}

/**
 * Computes the absolute historical time investment metrics expressed in raw seconds.
 * * Evaluates shifting polymorphic properties within [TypeTimer] structures to subtract
 * remaining ticks from initial targets, compounding interval iteration multipliers when necessary.
 *
 * @param milliseconds The current real-time remaining or progressive millisecond snapshot anchor.
 * @param typeTimer The active behavioral modality model rule defining the tracking run.
 * @return The accumulated total duration in seconds spent under active tracking.
 */
fun getSecondsPassed(milliseconds: Long, typeTimer: TypeTimer): Long {
    val timePassed:Long = when(typeTimer){
        TypeTimer.STOPWATCH -> {
            longToSeconds(milliseconds)
        }
        is TypeTimer.TIMER -> {
            longToSeconds(typeTimer.time - milliseconds)
        }
        is TypeTimer.INTERVAL -> {
            if(typeTimer.currentInterval == 1){
                longToSeconds(typeTimer.time - milliseconds)
            }else{
                longToSeconds((typeTimer.time - milliseconds) + (typeTimer.time * (typeTimer.currentInterval - 1)))
            }
        }
    }

    return timePassed
}

/**
 * Converts standard millisecond scalar values into dynamic seconds counters.
 *
 * @param milliseconds The absolute raw system timestamp duration value.
 * @return The equivalent calculated length in seconds.
 */
fun longToSeconds(milliseconds: Long): Long {
    return (milliseconds / 1000)
}

/**
 * Maps calendar deadlines into humanized colloquial strings using localized framework string assets.
 * Evaluates immediate relative temporal offsets before falling back to flat text conversions.
 *
 * @param date The targeted evaluation timeline checkpoint marker.
 * @param context The active Android framework context instance required to fetch string templates.
 * @return A humanized localized representation (e.g., "Today", "Yesterday").
 */
fun getTextToday(date: LocalDate,context:Context):String{
    return when(date){
        LocalDate.now() -> context.getString(R.string.timer_today)
        LocalDate.now().plusDays(1) ->  context.getString(R.string.timer_tomorrow)
        LocalDate.now().minusDays(1) -> context.getString(R.string.timer_yesterday)
        else -> {date.toString()}
    }
}

/**
 * Dispatches an explicit system-level navigation Intent token to bring the main application activity
 * back into the foreground focus space.
 * * Leverages deep linking paths to route the user directly into the active timer module tree grid,
 * resetting existing stacked task structures along the way.
 *
 * @param context The systemic non-leaking context handler required to trigger the start pipeline.
 */
fun openAppIntoTimer(context: Context){
    val clickIntent = Intent(
        Intent.ACTION_VIEW,
        "app://main/timer".toUri()
    ).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("destination", "timer")
    }

    context.startActivity(clickIntent)
}
