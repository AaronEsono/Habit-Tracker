package aeb.proyecto.ui.calendar.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

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

fun YearMonth.getCalendarDates(firstDayWeek: DayOfWeek): List<LocalDate> {
    val firstOfMonth = atDay(1)
    val firstDayOfWeek = firstDayWeek

    val start = firstOfMonth.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))

    return generateSequence(start) { it.plusDays(1) }
        .take(42) // 6 weeks * 7 days
        .toList()
}
