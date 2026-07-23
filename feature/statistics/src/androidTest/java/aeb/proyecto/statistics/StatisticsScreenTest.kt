package aeb.proyecto.statistics

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.statistics.components.common.boxDays.StatisticsBoxDays
import aeb.proyecto.statistics.components.common.card.HeaderCard
import aeb.proyecto.statistics.components.common.donutChart.PieChartData
import aeb.proyecto.statistics.components.common.donutChart.PieChartState
import aeb.proyecto.statistics.components.common.graphics.monthGraphics.MonthGraphics
import aeb.proyecto.statistics.components.vertical.VerticalStatisticsScreen
import aeb.proyecto.statistics.components.vertical.donutChart.VerticalPieChart
import aeb.proyecto.statistics.components.vertical.screens.ContentVerticalStatisticsScreen
import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.statistics.model.DayBoxState
import aeb.proyecto.statistics.model.GoalsDoneState
import aeb.proyecto.statistics.model.GraphicsState
import aeb.proyecto.statistics.model.StatisticsState
import aeb.proyecto.statistics.model.StatisticsSuccessState
import aeb.proyecto.statistics.utils.getWeeks
import aeb.proyecto.ui.calendar.model.CalendarUIState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@RunWith(AndroidJUnit4::class)
@SmallTest
class StatisticsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun given_a_state_when_loading_then_show_the_loading(){

        composeTestRule.setContent {
            VerticalStatisticsScreen(
                statisticsState = StatisticsState.Loading,
                boxUIState = emptyList(),
                graphicsState = GraphicsState(),
                hourlyGraphicsState = GraphicsState(),
                goalDoneState = GoalsDoneState(),
                pieChartState = emptyList(),
                yearMonth = YearMonth.now(),
                yearGraphicsSelected = LocalDate.now().year,
                yearHourlyGraphicsSelected = 1,
                startDayOfWeek = DayOfWeek.MONDAY,
                calendarUIState = CalendarUIState<HabitWithDay>(emptyList()),
                onCLickCard = {},
                onMonthChange = {},
                onYearSelected = {},
                onHourYearSelected = {}
            )
        }

        composeTestRule.onNodeWithTag("statistics_loading").assertIsDisplayed()
    }

    @Test
    fun given_a_state_when_success_but_no_habits_then_show_the_no_content_screen(){

        composeTestRule.setContent {
            VerticalStatisticsScreen(
                statisticsState = StatisticsState.Success(state = StatisticsSuccessState.Empty),
                boxUIState = emptyList(),
                graphicsState = GraphicsState(),
                hourlyGraphicsState = GraphicsState(),
                goalDoneState = GoalsDoneState(),
                pieChartState = emptyList(),
                yearMonth = YearMonth.now(),
                yearGraphicsSelected = LocalDate.now().year,
                yearHourlyGraphicsSelected = 1,
                startDayOfWeek = DayOfWeek.MONDAY,
                calendarUIState = CalendarUIState<HabitWithDay>(emptyList()),
                onCLickCard = {},
                onMonthChange = {},
                onYearSelected = {},
                onHourYearSelected = {}
            )
        }

        composeTestRule.onNodeWithTag("statistics_no_content_screen").assertIsDisplayed()
    }

    @Test
    fun given_a_state_when_success_with_habits_then_show_the_content_screen(){
        val listHabit = listOf(Habit())
        val habitSelected = HabitWithDailyHabit()

        composeTestRule.setContent {
            VerticalStatisticsScreen(
                statisticsState = StatisticsState.Success(state = StatisticsSuccessState.Habits(
                    habits = listHabit,
                    habitSelected = habitSelected
                )),
                boxUIState = emptyList(),
                graphicsState = GraphicsState(),
                hourlyGraphicsState = GraphicsState(),
                goalDoneState = GoalsDoneState(),
                pieChartState = emptyList(),
                yearMonth = YearMonth.now(),
                yearGraphicsSelected = LocalDate.now().year,
                yearHourlyGraphicsSelected = 1,
                startDayOfWeek = DayOfWeek.MONDAY,
                calendarUIState = CalendarUIState<HabitWithDay>(emptyList()),
                onCLickCard = {},
                onMonthChange = {},
                onYearSelected = {},
                onHourYearSelected = {}
            )
        }

        composeTestRule.onNodeWithTag("statistics_vertical_screen_content").assertIsDisplayed()
    }

    @Test
    fun given_some_habits_when_displayed_then_show_the_correct_headers(){
        val habit1 = Habit(name = "habit1", id = 1)
        val habit2 = Habit(name = "habit2", id = 2)
        val habit3 = Habit(name = "habit3", id = 3)
        val habit3WithDay = HabitWithDailyHabit(habit = habit3)
        val listHabit = listOf(habit1, habit2, habit3)

        composeTestRule.setContent {
            VerticalStatisticsScreen(
                statisticsState = StatisticsState.Success(state = StatisticsSuccessState.Habits(
                    habits = listHabit,
                    habitSelected = habit3WithDay
                )),
                boxUIState = emptyList(),
                graphicsState = GraphicsState(),
                hourlyGraphicsState = GraphicsState(),
                goalDoneState = GoalsDoneState(),
                pieChartState = emptyList(),
                yearMonth = YearMonth.now(),
                yearGraphicsSelected = LocalDate.now().year,
                yearHourlyGraphicsSelected = 1,
                startDayOfWeek = DayOfWeek.MONDAY,
                calendarUIState = CalendarUIState<HabitWithDay>(emptyList()),
                onCLickCard = {},
                onMonthChange = {},
                onYearSelected = {},
                onHourYearSelected = {}
            )
        }

        composeTestRule.onNodeWithTag("statistics_header_card_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("statistics_header_card_2").assertIsDisplayed()
        composeTestRule.onNodeWithTag("statistics_header_card_3").assertIsDisplayed()
    }

    @Test
    fun given_a_header_card_when_clicked_then_return_the_correct_id(){
        val habit = Habit(name = "habit1", id = 1)
        var clicked = 0L

        composeTestRule.setContent {
            HeaderCard(
                habit = habit,
                selected = true,
                onClickCard = { clicked = it }
            )
        }

        composeTestRule.onNodeWithTag("statistics_header_card_1").performClick()

        assert(clicked == 1L)
    }

    @Test
    fun verifyOnlySelectedHabitIsSelected() {
        val habit1 = Habit(id = 1L, name = "Agua")
        val habit2 = Habit(id = 2L, name = "Gimnasio")
        val habit3 = Habit(id = 3L, name = "Leer")

        val selectedHabit = HabitWithDailyHabit(
            habit = habit2,
            dailyHabits = mutableListOf()
        )

        composeTestRule.setContent {
            ContentVerticalStatisticsScreen(
                habits = listOf(habit1, habit2, habit3),
                habitSelected = selectedHabit,
                boxUIState = emptyList(),
                graphicsState = GraphicsState(),
                hourlyGraphicsState = GraphicsState(),
                goalDoneState = GoalsDoneState(),
                pieChartState = emptyList(),
                yearMonth = YearMonth.now(),
                yearGraphicsSelected = 2026,
                yearHourlyGraphicsSelected = 2026,
                startDayOfWeek = DayOfWeek.MONDAY,
                calendarUIState = CalendarUIState(emptyList()),
                onClickCard = {},
                onMonthChange = {}
            )
        }

        composeTestRule.onNodeWithTag("statistics_header_card_2")
            .assertIsSelected()

        composeTestRule
            .onNodeWithTag("statistics_header_card_1")
            .assertIsNotSelected()

        composeTestRule
            .onNodeWithTag("statistics_header_card_3")
            .assertIsNotSelected()
    }

    @Test
    fun ordered_days_returns_days_aligned_with_start_day_of_week() {
        val startDay = DayOfWeek.SUNDAY

        val days = DayOfWeek.entries
        val startIndex = days.indexOf(startDay)
        val orderedDays = (0 until 7).map { i -> days[(startIndex + i) % 7] }

        assertEquals(DayOfWeek.SUNDAY, orderedDays.first())
        assertEquals(DayOfWeek.SATURDAY, orderedDays.last())
        assertEquals(7, orderedDays.size)
    }

    @Test
    fun when_boxUIState_has_full_weeks_getWeeks_returns_list_chunked_into_groups_of_7() {
        // --- GIVEN ---
        val monday = LocalDate.of(2026, 7, 6)
        val fakeState = List(14) { index ->
            BoxUIState(day = monday.plusDays(index.toLong()), dayState = DayBoxState.Done)
        }

        composeTestRule.setContent {
            // --- WHEN ---
            val result = getWeeks(
                boxUIState = fakeState,
                startDayOfWeek = DayOfWeek.MONDAY
            )

            // --- THEN ---
            assertEquals(2, result.size)
            assertEquals(7, result[0].size)
            assertEquals(1, result[1].size)
        }
    }

    @Test
    fun when_boxUIState_requires_alignment_drops_overflow_days_correctly() {
        // --- GIVEN ---
        val wednesday = LocalDate.of(2026, 7, 8)
        val fakeState = List(10) { index ->
            BoxUIState(day = wednesday.plusDays(index.toLong()), dayState = DayBoxState.Done)
        }

        composeTestRule.setContent {
            // --- WHEN ---
            val result = getWeeks(
                boxUIState = fakeState,
                startDayOfWeek = DayOfWeek.MONDAY
            )

            // --- THEN ---
            assertTrue(result.all { it.size <= 7 })

        }
    }

    @Test
    fun monthGraphics_whenModelIsNull_doesNotRenderChartContent() {
        composeTestRule.setContent {
            MonthGraphics(
                graphicsState = GraphicsState(model = null),
                yearGraphicsSelected = 2026
            )
        }

        composeTestRule
            .onNodeWithContentDescription("arrow back year selected")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("2026")
            .assertDoesNotExist()
    }

    @Test
    fun bottomAxisValueFormatter_returnsCorrectMonthName_andHandlesOutOfBounds() {
        val monthLabels = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

        fun format(x: Float): String = monthLabels.getOrElse(x.toInt()) { "" }

        assertEquals("Ene", format(0f))
        assertEquals("Dic", format(11f))

        assertEquals("", format(-1f))
        assertEquals("", format(12f))
        assertEquals("", format(99f))
    }
}