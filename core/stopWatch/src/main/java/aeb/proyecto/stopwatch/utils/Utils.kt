package aeb.proyecto.stopwatch.utils

import kotlin.text.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun formatTime(seconds: String, minutes: String, hours: String): String {
    return "$hours:$minutes:$seconds"
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}

fun timeToDuration(time:String):Duration{
    val (hours, minutes, seconds) = time.split(":").map { it.toLong() }
    return hours.toDuration(DurationUnit.HOURS) +
            minutes.toDuration(DurationUnit.MINUTES) +
            seconds.toDuration(DurationUnit.SECONDS)
}

fun longToHMS(milliseconds: Long): String {
    val seconds = milliseconds / 1000
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return "${hours.toInt().pad()}:${minutes.toInt().pad()}:${secs.toInt().pad()}"
}