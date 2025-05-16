package aeb.proyecto.stopwatch.utils

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