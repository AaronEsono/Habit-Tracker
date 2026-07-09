package aeb.proyecto.ui

import aeb.proyecto.ui.calendar.model.getCalendarDates
import aeb.proyecto.ui.calendar.source.CalendarDataSource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class CalendarUnitaryTest {

    private val dataSource = CalendarDataSource()

    @Test
    fun `given July 2026, when getDates starting on Monday, then returns exactly 42 days with correct padding`() {
        // --- GIVEN ---
        val yearMonth = YearMonth.of(2026, 7)
        val startDayOfWeek = DayOfWeek.MONDAY

        // --- WHEN ---
        val result = dataSource.getDates(yearMonth = yearMonth, dayOfWeek = startDayOfWeek) { date ->
            "Payload-$date"
        }

        // --- THEN ---
        assertEquals(42, result.size)

        assertEquals(LocalDate.of(2026, 6, 29), result.first().dateOfMonth)
        assertEquals(LocalDate.of(2026, 8, 9), result.last().dateOfMonth)
        assertEquals("Payload-2026-07-15", result[16].data)
    }

    @Test
    fun `given current month, when getDates is called, then only today is selected inside the target month`() {
        // --- GIVEN ---
        val currentMonth = YearMonth.now()
        val today = LocalDate.now()

        // --- WHEN ---
        val result = dataSource.getDates(yearMonth = currentMonth, dayOfWeek = DayOfWeek.MONDAY) { null }

        // --- THEN ---
        val todayCell = result.find { it.dateOfMonth.isEqual(today) }

        if (todayCell != null) {
            assertTrue("El día de hoy debería estar marcado como seleccionado", todayCell.isSelected)
        }
    }

    @Test
    fun `given February 2021, when getCalendarDates is called with Monday as start day, then returns exactly 42 days`() {
        // --- GIVEN ---
        val yearMonth = YearMonth.of(2021, 2)
        val startDayOfWeek = DayOfWeek.MONDAY

        // --- WHEN ---
        val dates = yearMonth.getCalendarDates(startDayOfWeek)

        // --- THEN ---
        assertEquals(42, dates.size)
        assertEquals(LocalDate.of(2021, 2, 1), dates.first())
        assertEquals(LocalDate.of(2021, 3, 14), dates.last())
    }

    @Test
    fun `given Month, when Week Starts On Sunday, then First Day of Grid Is Sunday`() {
        // --- GIVEN ---
        val yearMonth = YearMonth.of(2026, 7)
        val startDayOfWeek = DayOfWeek.SUNDAY

        // --- WHEN ---
        val dates = yearMonth.getCalendarDates(startDayOfWeek)

        // --- THEN ---
        assertEquals(42, dates.size)

        assertEquals(DayOfWeek.SUNDAY, dates.first().dayOfWeek)
        assertEquals(LocalDate.of(2026, 6, 27).plusDays(1), dates.first())
    }
}