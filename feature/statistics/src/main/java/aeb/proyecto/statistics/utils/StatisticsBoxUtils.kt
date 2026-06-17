package aeb.proyecto.statistics.utils

import aeb.proyecto.statistics.R
import aeb.proyecto.statistics.model.BoxUIState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek

/**
 * Transforms a flat list of [BoxUIState] into a chunked list of weeks.
 * This function calculates the offset needed based on the [startDayOfWeek]
 * to ensure the calendar grid aligns correctly.
 *
 * @param boxUIState The list of daily habit states.
 * @param startDayOfWeek The preferred start day of the week (e.g., Monday or Sunday).
 * @return A list of lists, where each inner list represents a week of 7 days.
 */
@Composable
fun getWeeks(
    boxUIState: List<BoxUIState>,
    startDayOfWeek: DayOfWeek,
): List<List<BoxUIState>> {

    // Calculate the reference point and offsets for the calendar grid
    val today = remember (boxUIState){ boxUIState.first().day }

    val daysInLastWeek = remember (boxUIState, startDayOfWeek){ ((today.dayOfWeek.value - startDayOfWeek.value + 7) % 7) + 1 }

    val remainder = remember (boxUIState){ boxUIState.size % 7 }

    val daysToRemove = remember (remainder,daysInLastWeek){ (7 + remainder - daysInLastWeek) % 7 }

    // Align the list to start correctly at the beginning of the week
    val adjustedList = remember (boxUIState,daysToRemove){
        if (daysToRemove == 0) boxUIState
        else boxUIState.drop(daysToRemove)
    }

    // Return the grid structure
    return remember (adjustedList){ adjustedList.chunked(7) }
}

/**
 * Extension property for [DayOfWeek] to retrieve its localized label resource ID.
 *
 * @return The [StringRes] ID corresponding to the abbreviated day name.
 */
fun DayOfWeek.label(): Int {
    return when (this) {
        DayOfWeek.MONDAY -> R.string.statistics_abr_mon
        DayOfWeek.TUESDAY -> R.string.statistics_abr_tue
        DayOfWeek.WEDNESDAY -> R.string.statistics_abr_wed
        DayOfWeek.THURSDAY -> R.string.statistics_abr_thu
        DayOfWeek.FRIDAY -> R.string.statistics_abr_fri
        DayOfWeek.SATURDAY -> R.string.statistics_abr_sat
        DayOfWeek.SUNDAY -> R.string.statistics_abr_sun
    }
}