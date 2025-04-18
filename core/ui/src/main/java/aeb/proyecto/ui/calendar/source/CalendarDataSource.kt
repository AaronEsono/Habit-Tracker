package aeb.proyecto.ui.calendar.source

import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.calendar.model.getCalendarDates
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class CalendarDataSource @Inject constructor() {
    fun <T> getDates(
        dayOfWeek: DayOfWeek,
        yearMonth: YearMonth,
        getData: (LocalDate) -> T?
    ): List<CalendarUIState.DateCalendar<T>> {
        return yearMonth.getCalendarDates(dayOfWeek)
            .map { date ->
                CalendarUIState.DateCalendar(
                    dateOfMonth = date,
                    isSelected = date.isEqual(LocalDate.now()) && date.monthValue == yearMonth.monthValue,
                    data = getData(date)
                )
            }
    }
}