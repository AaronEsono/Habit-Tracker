package aeb.proyecto.ui.calendar.model

import aeb.proyecto.language.model.getFirstDayOfWeekByLocale
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

data class CalendarUIState<T>(
    val dates: List<DateCalendar<T>>
){
    data class DateCalendar<T>(
        val dateOfMonth: LocalDate,
        val isSelected: Boolean,
        val data:T? = null
    )

    companion object {
        fun <T> init(): CalendarUIState<T> {
            return CalendarUIState(
                dates = emptyList()
            )
        }
    }
}

fun YearMonth.getCalendarDates(): List<LocalDate> {
    val firstOfMonth = atDay(1)
    val firstDayOfWeek = getFirstDayOfWeekByLocale()

    val start = firstOfMonth.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))

    return generateSequence(start) { it.plusDays(1) }
        .take(42) // 6 weeks * 7 days
        .toList()
}
