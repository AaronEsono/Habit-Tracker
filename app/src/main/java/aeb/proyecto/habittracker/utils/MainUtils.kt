package aeb.proyecto.habittracker.utils

import java.util.Locale

/**
 * Converts a duration given in seconds into a standardized, zero-padded time format string.
 *
 * This utility calculates the proportional hours, minutes, and seconds components from the
 * provided raw value, formatting the output using [Locale.US] to guarantee consistent presentation.
 *
 * **Example output:**
 * * `convertToHours(3665)` returns `"01:01:05"`
 * * `convertToHours(45)` returns `"00:00:45"`
 *
 * @param seconds The total duration represented as a [Long] integer.
 * @return A formatted [String] following the strict `HH:mm:ss` time representation pattern.
 */
fun convertToHours(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs)
}