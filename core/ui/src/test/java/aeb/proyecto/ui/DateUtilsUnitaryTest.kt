package aeb.proyecto.ui

import aeb.proyecto.ui.date.DaysWeek
import aeb.proyecto.ui.date.DaysWeekAvr
import aeb.proyecto.ui.date.utils.getAvr
import aeb.proyecto.ui.date.utils.getDay
import aeb.proyecto.ui.date.utils.getOrderedDays
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.DayOfWeek

class DateUtilsUnitaryTest {

    @Test
    fun `given day of week when getDay is Called then returns correct string resource`() {
        // --- GIVEN ---
        val dayString = "THURSDAY"

        // --- WHEN ---
        val result = getDay(dayString)

        // --- THEN ---
        // Verificamos que devuelve el ID del recurso esperado (puedes comparar con tu enum)
        assertEquals(DaysWeek.JUEVES.string, result)
    }

    @Test
    fun `given day of week when getAvr is Called then returns correct string resource`() {
        // --- GIVEN ---
        val dayString = DayOfWeek.THURSDAY

        // --- WHEN ---
        val result = getAvr(dayString)

        // --- THEN ---
        // Verificamos que devuelve el ID del recurso esperado (puedes comparar con tu enum)
        assertEquals(DaysWeekAvr.JUEVES.string, result)
    }

    @Test
    fun `given start day when getOrderedDays is called then rotates correctly`() {
        // --- GIVEN ---
        val startDay = DayOfWeek.WEDNESDAY // Miércoles

        // --- WHEN ---
        val orderedList = getOrderedDays(startDay)

        // --- THEN ---
        assertEquals(DayOfWeek.WEDNESDAY, orderedList.first().id)
        assertEquals(DayOfWeek.TUESDAY, orderedList.last().id)
        assertEquals(7, orderedList.size)
    }

}