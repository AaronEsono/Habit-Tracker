package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.utils.convertToHours
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ConvertHourTest {
    @Test
    fun `convertToHours given zero seconds should return zero formatted time`() {
        val result = convertToHours(0L)
        assertEquals("00:00:00", result)
    }

    @Test
    fun `convertToHours given seconds under one minute should format seconds correctly`() {
        val result = convertToHours(45L)
        assertEquals("00:00:45", result)
    }

    @Test
    fun `convertToHours given exact one minute should format minutes correctly`() {
        val result = convertToHours(60L)
        assertEquals("00:01:00", result)
    }

    @Test
    fun `convertToHours given minutes and seconds under one hour should format correctly`() {
        val result = convertToHours(309L)
        assertEquals("00:05:09", result)
    }

    @Test
    fun `convertToHours given exact one hour should format hours correctly`() {
        val result = convertToHours(3600L)
        assertEquals("01:00:00", result)
    }

    @Test
    fun `convertToHours given mixed hours minutes and seconds should format correctly`() {
        val result = convertToHours(12342L)
        assertEquals("03:25:42", result)
    }

    @Test
    fun `convertToHours given more than 24 hours should accumulate hours properly`() {
        val result = convertToHours(108000L)
        assertEquals("30:00:00", result)
    }

}