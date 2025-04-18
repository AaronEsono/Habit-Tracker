package aeb.proyecto.ui.calendar.model

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
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

    val start = firstOfMonth.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
    val end = atEndOfMonth().with(TemporalAdjusters.nextOrSame(firstDayOfWeek.plus(6)))

    return generateSequence(start) { it.plusDays(1) }
        .takeWhile { !it.isAfter(end) }
        .toList()
}
