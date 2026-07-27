package aeb.proyecto.habit

import aeb.proyecto.habit.components.common.bottomSheet.selectDate.SelectDateViewModel
import aeb.proyecto.language.provider.RegionFirstDayProvider
import aeb.proyecto.ui.calendar.source.CalendarDataSource
import app.cash.turbine.test
import com.google.common.base.CharMatcher.any
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.DayOfWeek
import java.time.YearMonth

class SelectDateViewModelTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val calendarDataSource: CalendarDataSource = CalendarDataSource()
    private val firstDayProvider = FakeRegionFirstDayProvider(DayOfWeek.MONDAY)

    private lateinit var viewModel: SelectDateViewModel
    private val initialYearMonth = YearMonth.now()

    @Before
    fun setUp() {
        viewModel = SelectDateViewModel(
            calendarDataSource = calendarDataSource,
            firstDayProvider = firstDayProvider
        )
    }

    @Test
    fun given_viewModel_initialized_then_yearMonth_is_current_month() {
        // --- GIVEN & WHEN (setUp) ---
        val currentYearMonth = viewModel.yearMonth.value

        // --- THEN ---
        assertEquals(initialYearMonth, currentYearMonth)
    }

    @Test
    fun given_new_yearMonth_when_onMonthButtonClicked_invoked_then_updates_yearMonth_and_recalculates_grid() = runTest {
        // --- GIVEN ---
        val targetMonth = YearMonth.of(2026, 12)

        viewModel.calendarUIState.test {
            skipItems(1)

            // --- WHEN ---
            viewModel.onMonthButtonClicked(targetMonth)

            // --- THEN ---
            assertEquals(targetMonth, viewModel.yearMonth.value)

            val updatedState = awaitItem()
            assertEquals(42, updatedState.dates.size)

            val containsTargetMonthDay = updatedState.dates.any {
                it.dateOfMonth.year == 2026 && it.dateOfMonth.monthValue == 12
            }
            assertTrue(containsTargetMonthDay)
        }
    }

    @Test
    fun given_different_month_selected_when_initMonth_invoked_then_resets_yearMonth_to_now() = runTest {
        // --- GIVEN ---
        val previousMonth = YearMonth.of(2025, 1)
        viewModel.onMonthButtonClicked(previousMonth)
        assertEquals(previousMonth, viewModel.yearMonth.value)

        // --- WHEN ---
        viewModel.initMonth()

        // --- THEN ---
        assertEquals(YearMonth.now(), viewModel.yearMonth.value)
    }

    private class FakeRegionFirstDayProvider(
        private val defaultFirstDay: DayOfWeek = DayOfWeek.MONDAY
    ) : RegionFirstDayProvider(context = mock()) {

        override fun getFirstDayOfWeekByLocale(): DayOfWeek {
            return defaultFirstDay
        }
    }
}