package aeb.proyecto.ui

import aeb.proyecto.ui.date.utils.getTextToday
import androidx.compose.ui.test.junit4.createComposeRule
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class DateUtilsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenDateIsToday_whenGetTextTodayIsCalled_thenReturnsLocalizedTodayString() {
        // --- GIVEN ---
        val dateToday = LocalDate.now()
        var resultText = ""

        // --- WHEN ---
        composeTestRule.setContent {
            // Ejecutamos el composable y guardamos el string que devuelve
            resultText = getTextToday(date = dateToday)
        }

        composeTestRule.waitForIdle()

        // --- THEN ---
        assertEquals("Today", resultText)
    }

    @Test
    fun givenDateIsTomorrow_whenGetTextTodayIsCalled_thenReturnsTomorrowString() {
        // --- GIVEN ---
        val dateTomorrow = LocalDate.now().plusDays(1)
        var resultText = ""

        // --- WHEN ---
        composeTestRule.setContent {
            resultText = getTextToday(date = dateTomorrow)
        }

        composeTestRule.waitForIdle()

        // --- THEN ---
        assertEquals("Tomorrow", resultText)
    }

    @Test
    fun givenDateIsYesterday_whenGetTextTodayIsCalled_thenReturnsTomorrowString() {
        // --- GIVEN ---
        val dateTomorrow = LocalDate.now().minusDays(1)
        var resultText = ""

        // --- WHEN ---
        composeTestRule.setContent {
            resultText = getTextToday(date = dateTomorrow)
        }

        composeTestRule.waitForIdle()

        // --- THEN ---
        assertEquals("Yesterday", resultText)
    }

    @Test
    fun givenDateIsAnyOtherDay_whenGetTextTodayIsCalled_thenReturnsFormattedString() {
        // --- GIVEN ---
        val targetDate = LocalDate.of(2025, 7, 15)
        var resultText = ""

        // --- WHEN ---
        composeTestRule.setContent {
            resultText = getTextToday(date = targetDate)
        }

        composeTestRule.waitForIdle()

        // --- THEN ---
        val expectedString = "Jul 15 2025"

        assertEquals(expectedString, resultText)
    }
}