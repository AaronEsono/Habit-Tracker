package aeb.proyecto.ui.calendar.source

import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.calendar.model.getCalendarDates
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

/**
 * Factory engine responsible for generating and mapping functional data structures for the custom calendar grid.
 * Fully decoupled from specific platform database layers, it utilizes high-order functional abstraction loops
 * to combine mathematical chronological sequences with polymorphic domain payloads.
 */
class CalendarDataSource @Inject constructor() {

    /**
     * Compiles and transforms a 42-day flat chronological matrix into a list of stateful calendar cell models.
     * Evaluates real-time current date anchors defensively against targeted month boundaries to prevent
     * multi-month duplicate selection artifacts.
     *
     * @param T The specialized domain payload token profile to embed inside individual days.
     * @param dayOfWeek The structural anchor mapping designated to align the leading week columns (e.g., Monday).
     * @param yearMonth The target year and month boundary slice chosen by the coordinator layer.
     * @param getData Callback high-order closure lambda tasked with fetching business data payloads associated with a date.
     * @return A list containing precisely 42 stateful [CalendarUIState.DateCalendar] cells matching the grid matrix.
     */
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