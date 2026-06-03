package aeb.proyecto.ui.date.utils

import aeb.proyecto.ui.R
import aeb.proyecto.ui.date.DaysWeek
import aeb.proyecto.ui.date.DaysWeekAvr
import aeb.proyecto.ui.month.getAvrMonth
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Resolves the localized full-string resource identifier for a given platform day name string.
 * Establishes a safe boundary fallback to [DaysWeek.LUNES] if no matches are found inside the entries.
 *
 * @param dayOfWeek The raw string name of the target [DayOfWeek] constant (e.g., "MONDAY").
 * @return The functional compiler-guaranteed [androidx.annotation.StringRes] integer pointer.
 */
fun getDay(dayOfWeek:String):Int{
    return DaysWeek.entries.find { it.id.name == dayOfWeek }?.string ?: DaysWeek.LUNES.string
}

/**
 * Computes a cyclical array rotation over the week abbreviations vector to shift column alignments
 * based on a custom starting row anchor.
 * Uses pure high-order functional slicing operations to construct a seamless shifted calendar row.
 *
 * @param startDay The platform [DayOfWeek] anchor designated to lead the week row sequence.
 * @return A fresh re-aligned list of [DaysWeekAvr] tokens matching the structural matrix row columns.
 */
fun getOrderedDays(startDay: DayOfWeek): List<DaysWeekAvr> {
    val allDays = DaysWeekAvr.entries
    val startIndex = allDays.indexOfFirst { it.id == startDay }

    return if (startIndex != -1) {
        // Concatenate the remaining split tail segment with the extracted leading fragment cleanly
        allDays.drop(startIndex) + allDays.take(startIndex)
    } else {
        allDays
    }
}

/**
 * Resolves the localized abbreviation string resource identifier associated with a concrete platform [DayOfWeek].
 * Establishes a fallback guard back to [DaysWeekAvr.LUNES] to isolate runtime resource collection errors.
 *
 * @param day The target platform [DayOfWeek] instance.
 * @return The functional compiler-guaranteed [androidx.annotation.StringRes] integer pointer.
 */
fun getAvr(day: DayOfWeek):Int{
    return DaysWeekAvr.entries.find { it.id == day }?.string ?: DaysWeekAvr.LUNES.string
}

/**
 * Humanizes temporal [LocalDate] instances into relative conversational tokens (Today, Tomorrow, Yesterday).
 * Falls back to a localized, structurally formatted date string pattern if the instance shifts outside
 * the adjacent three-day operational window.
 *
 * @param date The physical platform [LocalDate] node targeted for evaluation.
 * @return A localized conversational or structured descriptive text string.
 */
@Composable
fun getTextToday(date: LocalDate):String{
    return when(date){
        LocalDate.now() -> stringResource(R.string.today)
        LocalDate.now().plusDays(1) ->  stringResource(R.string.tomorrow)
        LocalDate.now().minusDays(1) -> stringResource(R.string.yesterday)
        else -> {
            // Compile a structured formatting mask utilizing multi-parametric string resources injection
            stringResource(
                R.string.habit_action_icon,
                date.dayOfMonth.toString(),
                stringResource(getAvrMonth(date.month.value)),
                date.year.toString()
            )
        }
    }
}