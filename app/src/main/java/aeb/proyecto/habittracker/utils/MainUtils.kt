package aeb.proyecto.habittracker.utils

import androidx.compose.runtime.Composable
import java.util.Locale

fun convertToHours(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs)
}