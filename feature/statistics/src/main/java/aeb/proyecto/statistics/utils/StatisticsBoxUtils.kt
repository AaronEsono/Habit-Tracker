package aeb.proyecto.statistics.utils

import aeb.proyecto.statistics.R
import aeb.proyecto.statistics.model.BoxUIState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek

@Composable
fun getWeeks(
    boxUIState: List<BoxUIState>,
    startDayOfWeek: DayOfWeek,
): List<List<BoxUIState>> {

    val today = remember (boxUIState){ boxUIState.first().day }

    val daysInLastWeek = remember (boxUIState, startDayOfWeek){ ((today.dayOfWeek.value - startDayOfWeek.value + 7) % 7) + 1 }

    val remainder = remember (boxUIState){ boxUIState.size % 7 }

    val daysToRemove = remember (remainder,daysInLastWeek){ (7 + remainder - daysInLastWeek) % 7 }

    val adjustedList = remember (boxUIState,daysToRemove){
        if (daysToRemove == 0) boxUIState
        else boxUIState.drop(daysToRemove)
    }

    return remember (adjustedList){ adjustedList.chunked(7) }
}

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