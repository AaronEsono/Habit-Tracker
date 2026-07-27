package aeb.proyecto.habit

import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.screen.RestartDay
import aeb.proyecto.habit.components.common.bottomSheet.deleteHabit.DeleteHabitBottomSheet
import aeb.proyecto.habit.components.common.button.AddHabitButton
import aeb.proyecto.habit.components.common.button.BarActionIcon
import aeb.proyecto.habit.components.common.habitCards.dailyCard.DailyCard
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.separateGoal.SeparateMonthlyCard
import aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.uniqueGoal.UniqueMonthlyCard
import aeb.proyecto.habit.components.common.habitCards.recurringCard.RecurringCard
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.types.separateGoal.SeparateWeeklyCard
import aeb.proyecto.habit.components.common.habitCards.weeklyCard.types.uniqueGoal.UniqueWeeklyCard
import aeb.proyecto.habit.components.common.pager.PageSelected
import aeb.proyecto.habit.components.common.timeRange.TimeRangeHabit
import aeb.proyecto.habit.components.common.timeRange.components.DailyTimeRange
import aeb.proyecto.habit.components.common.timeRange.components.MonthlyTimeRange
import aeb.proyecto.habit.components.common.timeRange.components.WeeklyTimeRange
import aeb.proyecto.habit.components.common.timeRange.components.selectedDate
import aeb.proyecto.habit.components.vertical.VerticalHabitScreen
import aeb.proyecto.habit.components.vertical.components.bottomSheet.configureHabit.VerticalConfigureHabitBottomSheet
import aeb.proyecto.habit.model.BottomSheetUIState
import aeb.proyecto.habit.model.pager.PagerElement
import aeb.proyecto.habit.model.pager.PagerSelected
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.room.model.classes.UnitHabit
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.coroutineScope
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@SmallTest
class HabitScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun given_state_when_no_habit_then_show_no_habit_screen(){

        composeTestRule.setContent {
            VerticalHabitScreen(
                pagerTypesUIState = PagerTypesUiState.Success(emptyList()),
                filteredHabitsUiState = FilteredHabitsUiState.Empty,
                currentPagerSelected = CurrentPagerSelection.Uninitialized,
                selectedTimeRangeUiState = TimeRangeUiState.Empty,
                bottomSheetUIState = BottomSheetUIState(),
                startDayOfWeek = DayOfWeek.MONDAY,
                dateSelected = LocalDate.now(),
                navigateToAddHabit = {},
                onClickTab = {},
                onClickTimeRange = { _, _ -> },
                onBottomSheetSelectDateSelected = {},
                onDismissBottomSheet = {},
                onRestart = { _, _ -> },
                onClickConfigureHabit = { _, _, _ -> },
                onClickTimer = {},
                onClickCard = {},
                onLongClick = { _, _ -> },
                onClick = { _, _ -> },
                onClickEdit = {},
                onClickDelete = { _, _ -> },
                onAcceptDeleteHabit = {}
            )
        }

        composeTestRule.onNodeWithTag("habit_no_habit_screen").assertIsDisplayed()
    }

    @Test
    fun given_state_when_loading_then_show_no_habit_screen(){

        composeTestRule.setContent {
            VerticalHabitScreen(
                pagerTypesUIState = PagerTypesUiState.Loading,
                filteredHabitsUiState = FilteredHabitsUiState.Empty,
                currentPagerSelected = CurrentPagerSelection.Uninitialized,
                selectedTimeRangeUiState = TimeRangeUiState.Empty,
                bottomSheetUIState = BottomSheetUIState(),
                startDayOfWeek = DayOfWeek.MONDAY,
                dateSelected = LocalDate.now(),
                navigateToAddHabit = {},
                onClickTab = {},
                onClickTimeRange = { _, _ -> },
                onBottomSheetSelectDateSelected = {},
                onDismissBottomSheet = {},
                onRestart = { _, _ -> },
                onClickConfigureHabit = { _, _, _ -> },
                onClickTimer = {},
                onClickCard = {},
                onLongClick = { _, _ -> },
                onClick = { _, _ -> },
                onClickEdit = {},
                onClickDelete = { _, _ -> },
                onAcceptDeleteHabit = {}
            )
        }

        composeTestRule.onNodeWithTag("habit_loading").assertIsDisplayed()
    }

    @Test
    fun given_state_when_habit_then_show_no_habit_screen(){

        composeTestRule.setContent {
            VerticalHabitScreen(
                pagerTypesUIState = PagerTypesUiState.Success(listOf(PagerElement.DAILY)),
                filteredHabitsUiState = FilteredHabitsUiState.Empty,
                currentPagerSelected = CurrentPagerSelection.Uninitialized,
                selectedTimeRangeUiState = TimeRangeUiState.Empty,
                bottomSheetUIState = BottomSheetUIState(),
                startDayOfWeek = DayOfWeek.MONDAY,
                dateSelected = LocalDate.now(),
                navigateToAddHabit = {},
                onClickTab = {},
                onClickTimeRange = { _, _ -> },
                onBottomSheetSelectDateSelected = {},
                onDismissBottomSheet = {},
                onRestart = { _, _ -> },
                onClickConfigureHabit = { _, _, _ -> },
                onClickTimer = {},
                onClickCard = {},
                onLongClick = { _, _ -> },
                onClick = { _, _ -> },
                onClickEdit = {},
                onClickDelete = { _, _ -> },
                onAcceptDeleteHabit = {}
            )
        }

        composeTestRule.onNodeWithTag("habit_content_screen").assertIsDisplayed()
    }

    @Test
    fun given_add_habit_Button_when_clicked_then_returns_the_correct_value(){
        var result = 0L

        composeTestRule.setContent {
            AddHabitButton(
                navigateToAddHabit = { result = it }
            )
        }

        composeTestRule.onNodeWithTag("add_habit_button").performClick()

        assertEquals(result, -1L)
    }

    @Test
    fun given_pager_element_whe_in_pageSelected_then_show_the_correct(){

        composeTestRule.setContent {
            PageSelected(
                pagerElements = listOf(PagerElement.DAILY),
                currentPagerSelected = CurrentPagerSelection.Selected(
                    PagerSelected(
                        0,
                        PagerElement.DAILY
                    )
                )
            )

        }

        composeTestRule.onNodeWithTag("habit_pager_${PagerElement.DAILY.tag}",useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun given_a_list_pager_element_whe_in_pageSelected_then_show_the_correct(){

        composeTestRule.setContent {
            PageSelected(
                pagerElements = listOf(PagerElement.DAILY,PagerElement.WEEKLY,PagerElement.MONTHLY),
                currentPagerSelected = CurrentPagerSelection.Selected(
                    PagerSelected(
                        0,
                        PagerElement.DAILY
                    )
                )
            )

        }

        composeTestRule.onNodeWithTag("habit_pager_${PagerElement.DAILY.tag}",useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("habit_pager_${PagerElement.WEEKLY.tag}",useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("habit_pager_${PagerElement.MONTHLY.tag}",useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun given_pager_elements_when_one_is_selected_then_verify_tab_is_selected() {
        // --- GIVEN ---
        val elements = listOf(PagerElement.DAILY, PagerElement.WEEKLY, PagerElement.MONTHLY)
        val selectedElement = PagerElement.WEEKLY

        composeTestRule.setContent {
            PageSelected(
                pagerElements = elements,
                currentPagerSelected = CurrentPagerSelection.Selected(
                    PagerSelected(
                        index = 1,
                        pagerElement = selectedElement
                    )
                )
            )
        }

        // --- THEN ---
        composeTestRule
            .onNodeWithTag("habit_pager_${PagerElement.WEEKLY.tag}", useUnmergedTree = true)
            .assertIsSelected()

        composeTestRule
            .onNodeWithTag("habit_pager_${PagerElement.DAILY.tag}", useUnmergedTree = true)
            .assertIsNotSelected()
    }

    @Test
    fun given_pageSelected_when_tab_clicked_then_invokes_onClickTab_with_correct_element() {
        // --- GIVEN ---
        val elements = listOf(PagerElement.DAILY, PagerElement.WEEKLY, PagerElement.MONTHLY)
        var clickedElement: PagerElement? = null

        composeTestRule.setContent {
            PageSelected(
                pagerElements = elements,
                currentPagerSelected = CurrentPagerSelection.Selected(
                    PagerSelected(index = 0, pagerElement = PagerElement.DAILY)
                ),
                onClickTab = { element ->
                    clickedElement = element
                }
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithTag("habit_pager_${PagerElement.MONTHLY.tag}", useUnmergedTree = true)
            .performClick()

        // --- THEN ---
        assertEquals(PagerElement.MONTHLY, clickedElement)
    }

    @Test
    fun given_time_range_when_daily_then_show_the_data() {
        // --- GIVEN ---
        val selectedDate = LocalDate.now()
        val list = List(10) { index ->
            selectedDate.plusDays((index + 1).toLong())
        }

        composeTestRule.setContent {
            TimeRangeHabit(
                selectedTimeRangeUiState = TimeRangeUiState.Daily(list),
                selectedDate = selectedDate,
                onClickTimeRange = {_,_ -> },
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithTag("habit_dailyTimeRange", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun given_time_range_when_weekly_then_show_the_data() {
        // --- GIVEN ---
        val starWeek = LocalDate.now()
        val endWeek = starWeek.plusDays(6)

        composeTestRule.setContent {
            TimeRangeHabit(
                selectedTimeRangeUiState = TimeRangeUiState.Weekly(starWeek, endWeek),
                selectedDate = starWeek,
                onClickTimeRange = {_,_ -> },
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithTag("habit_weeklyTimeRange", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun given_time_range_when_monthly_then_show_the_data() {
        // --- GIVEN ---
        val starMonth = LocalDate.now()
        val endMonth = starMonth.plusDays(6)

        composeTestRule.setContent {
            TimeRangeHabit(
                selectedTimeRangeUiState = TimeRangeUiState.Monthly(starMonth, endMonth),
                selectedDate = endMonth,
                onClickTimeRange = {_,_ -> },
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithTag("habit_monthlyTimeRange", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun selectedDate_returns_true_when_dates_match() {
        val date = LocalDate.of(2026, 7, 24)

        val result = selectedDate(day = date, selectedDate = date)

        assertTrue(result)
    }

    @Test
    fun selectedDate_returns_false_when_dates_differ() {
        val day = LocalDate.of(2026, 7, 24)
        val selected = LocalDate.of(2026, 7, 25)

        val result = selectedDate(day = day, selectedDate = selected)

        assertFalse(result)
    }

    @Test
    fun given_dailyTimeRange_when_rendered_then_displays_lazy_row_correctly() {
        val days = listOf(
            LocalDate.of(2026, 7, 20),
            LocalDate.of(2026, 7, 21),
            LocalDate.of(2026, 7, 22)
        )

        composeTestRule.setContent {
            DailyTimeRange(
                selectedDate = days[0],
                daysOnRange = days
            )
        }

        composeTestRule
            .onNodeWithTag("habit_dailyTimeRange")
            .assertIsDisplayed()
    }

    @Test
    fun given_days_in_range_when_dayCard_clicked_then_invokes_onClick_with_date() {
        val targetDate = LocalDate.of(2026, 7, 22)
        val days = listOf(
            LocalDate.of(2026, 7, 20),
            LocalDate.of(2026, 7, 21),
            targetDate
        )

        var clickedDate: LocalDate? = null
        var isSelectedParam: Boolean? = null

        composeTestRule.setContent {
            DailyTimeRange(
                selectedDate = days[0],
                daysOnRange = days,
                onClick = { date, isSelected ->
                    clickedDate = date
                    isSelectedParam = isSelected
                }
            )
        }

        composeTestRule
            .onNodeWithTag("habit_day_card_$targetDate", useUnmergedTree = true)
            .performClick()

        assertEquals(targetDate, clickedDate)
        assertNotNull(isSelectedParam)
    }

    @Test
    fun given_selectedDate_deep_in_range_when_rendered_then_scrolls_to_make_selected_date_visible() {
        val startDate = LocalDate.of(2026, 7, 1)
        val days = (0 until 30).map { startDate.plusDays(it.toLong()) }

        val selectedDate = days[15]

        composeTestRule.setContent {
            DailyTimeRange(
                selectedDate = selectedDate,
                numberOfElements = 8,
                daysOnRange = days
            )
        }

        composeTestRule
            .onNodeWithTag("habit_day_card_$selectedDate", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun given_weeklyTimeRange_when_rendered_then_displays_container_and_arrows() {
        val start = LocalDate.of(2026, 7, 13) // Lunes
        val end = LocalDate.of(2026, 7, 19)   // Domingo

        composeTestRule.setContent {
            WeeklyTimeRange(
                startOfWeek = start,
                endOfWeek = end,
                onClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithTag("habit_weeklyTimeRange")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("weekly forward button")
            .assertIsDisplayed()
    }

    @Test
    fun given_weeklyTimeRange_when_previous_arrow_clicked_then_subtracts_seven_days() {
        val start = LocalDate.of(2026, 7, 13)
        val end = LocalDate.of(2026, 7, 19)

        var clickedDate: LocalDate? = null
        var directionBoolean: Boolean? = null

        composeTestRule.setContent {
            WeeklyTimeRange(
                startOfWeek = start,
                endOfWeek = end,
                onClick = { date, isForward ->
                    clickedDate = date
                    directionBoolean = isForward
                }
            )
        }

        composeTestRule
            .onNodeWithTag("habit_weekly_prev_button", useUnmergedTree = true)
            .performClick()

        assertEquals(LocalDate.of(2026, 7, 6), clickedDate)
        assertEquals(false, directionBoolean)
    }

    @Test
    fun given_weeklyTimeRange_when_next_arrow_clicked_then_adds_seven_days() {
        val start = LocalDate.of(2026, 7, 13)
        val end = LocalDate.of(2026, 7, 19)

        var clickedDate: LocalDate? = null

        composeTestRule.setContent {
            WeeklyTimeRange(
                startOfWeek = start,
                endOfWeek = end,
                onClick = { date, _ ->
                    clickedDate = date
                }
            )
        }

        composeTestRule
            .onNodeWithTag("habit_weekly_next_button", useUnmergedTree = true)
            .performClick()

        assertEquals(LocalDate.of(2026, 7, 20), clickedDate)
    }

    @Test
    fun given_monthlyTimeRange_when_rendered_then_displays_container_and_navigation_buttons() {
        // --- GIVEN ---
        val start = LocalDate.of(2026, 7, 1)
        val end = LocalDate.of(2026, 7, 31)

        // --- WHEN ---
        composeTestRule.setContent {
            MonthlyTimeRange(
                startOfMonth = start,
                endOfMonth = end,
                onClick = { _, _ -> }
            )
        }

        // --- THEN ---
        composeTestRule
            .onNodeWithTag("habit_monthlyTimeRange")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("habit_monthly_prev_button", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("habit_monthly_next_button", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun given_monthlyTimeRange_when_prev_button_clicked_then_subtracts_one_month_from_start() {
        // --- GIVEN ---
        val start = LocalDate.of(2026, 7, 1)
        val end = LocalDate.of(2026, 7, 31)

        var clickedDate: LocalDate? = null
        var isForwardParam: Boolean? = null

        composeTestRule.setContent {
            MonthlyTimeRange(
                startOfMonth = start,
                endOfMonth = end,
                onClick = { date, isForward ->
                    clickedDate = date
                    isForwardParam = isForward
                }
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithTag("habit_monthly_prev_button", useUnmergedTree = true)
            .performClick()

        // --- THEN ---
        assertEquals(LocalDate.of(2026, 6, 1), clickedDate)
        assertEquals(false, isForwardParam)
    }

    @Test
    fun given_monthlyTimeRange_when_next_button_clicked_then_adds_one_month_to_end() {
        // --- GIVEN ---
        val start = LocalDate.of(2026, 7, 1)
        val end = LocalDate.of(2026, 7, 31)

        var clickedDate: LocalDate? = null
        var isForwardParam: Boolean? = null

        composeTestRule.setContent {
            MonthlyTimeRange(
                startOfMonth = start,
                endOfMonth = end,
                onClick = { date, isForward ->
                    clickedDate = date
                    isForwardParam = isForward
                }
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithTag("habit_monthly_next_button", useUnmergedTree = true)
            .performClick()

        // --- THEN ---
        assertEquals(LocalDate.of(2026, 8, 31), clickedDate)
        assertEquals(false, isForwardParam)
    }

    private val fakeHabit = Habit(
        id = 1L,
        name = "Beber agua",
        description = "2 litros al día",
        goal = BigDecimal("2000"),
        unit = UnitHabit.ATTEMPTS,
        color = 0xFF4285F4.toInt(),
        icon = Icons.Default.WaterDrop
    )

    private val selectedDate = LocalDate.of(2026, 7, 24)

    private fun createHabitWithDaily(goalDone: BigDecimal?): HabitWithDailyHabit {
        val dailyHabits = if (goalDone != null) {
            mutableListOf(
                HabitDay(
                    id = 10L,
                    idHabit = 1L,
                    date = selectedDate,
                    goalDone = goalDone
                )
            )
        } else mutableListOf()

        return HabitWithDailyHabit(
            habit = fakeHabit,
            dailyHabits = dailyHabits
        )
    }

    @Test
    fun given_no_progress_when_rendered_then_shows_add_icon() {
        val habitData = createHabitWithDaily(goalDone = BigDecimal.ZERO)

        composeTestRule.setContent {
            DailyCard(
                selectedDate = selectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithContentDescription("add habit")
            .assertIsDisplayed()
    }

    @Test
    fun given_completed_goal_when_rendered_then_shows_check_icon() {
        val habitData = createHabitWithDaily(goalDone = BigDecimal("2000"))

        composeTestRule.setContent {
            DailyCard(
                selectedDate = selectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        composeTestRule.mainClock.advanceTimeBy(600)

        composeTestRule
            .onNodeWithContentDescription("check habit")
            .assertIsDisplayed()
    }

    @Test
    fun given_zero_goal_when_rendered_then_catches_arithmetic_exception_and_shows_add_icon() {
        val zeroGoalHabit = fakeHabit.copy(goal = BigDecimal.ZERO)
        val habitData = HabitWithDailyHabit(
            habit = zeroGoalHabit,
            dailyHabits = mutableListOf(
                HabitDay(id = 10L, idHabit = 1L, date = selectedDate, goalDone = BigDecimal("100"))
            )
        )

        composeTestRule.setContent {
            DailyCard(
                selectedDate = selectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithContentDescription("add habit")
            .assertIsDisplayed()
    }

    @Test
    fun given_dailyCard_when_progress_area_clicked_then_triggers_onClick_with_correct_params() {
        val habitData = createHabitWithDaily(goalDone = BigDecimal("500"))
        var clickedId: Long? = null
        var clickedDate: LocalDate? = null

        composeTestRule.setContent {
            DailyCard(
                selectedDate = selectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { id, date ->
                    clickedId = id
                    clickedDate = date
                },
                onLongClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithTag("habit_progress_area")
            .performClick()

        assertEquals(1L, clickedId)
        assertEquals(selectedDate, clickedDate)
    }

    private val fakeHabit2 = Habit(
        id = 2L,
        name = "Leer libro",
        description = "Lectura semanal",
        goal = BigDecimal("70"),
        unit = UnitHabit.PAGES,
        color = 0xFF4285F4.toInt(),
        icon = Icons.Default.Book
    )

    private val startOfWeek = LocalDate.of(2026, 7, 20)
    private val endOfWeek = LocalDate.of(2026, 7, 26)
    private val selectedDateWeek = LocalDate.of(2026, 7, 22)

    private fun createHabitWithWeeklyData(completedPages: BigDecimal): HabitWithDailyHabit {
        val dailyHabits = mutableListOf(
            HabitDay(
                id = 100L,
                idHabit = 1L,
                date = startOfWeek,
                goalDone = completedPages
            )
        )
        return HabitWithDailyHabit(
            habit = fakeHabit2,
            dailyHabits = dailyHabits
        )
    }

    @Test
    fun given_no_weekly_progress_when_rendered_then_displays_add_icon() {
        // --- GIVEN ---
        val habitData = createHabitWithWeeklyData(completedPages = BigDecimal.ZERO)

        // --- WHEN ---
        composeTestRule.setContent {
            UniqueWeeklyCard(
                startOfWeek = startOfWeek,
                endOfWeek = endOfWeek,
                selectedDate = selectedDateWeek,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        // --- THEN ---
        composeTestRule
            .onNodeWithContentDescription("add habit")
            .assertIsDisplayed()
    }

    @Test
    fun given_weekly_goal_completed_when_rendered_then_displays_check_icon() {
        // --- GIVEN ---
        val habitData = createHabitWithWeeklyData(completedPages = BigDecimal("70"))

        // --- WHEN ---
        composeTestRule.setContent {
            UniqueWeeklyCard(
                startOfWeek = startOfWeek,
                endOfWeek = endOfWeek,
                selectedDate = selectedDateWeek,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        composeTestRule.mainClock.advanceTimeBy(600)

        // --- THEN ---
        composeTestRule
            .onNodeWithContentDescription("check habit")
            .assertIsDisplayed()
    }

    @Test
    fun given_uniqueWeeklyCard_when_action_button_clicked_then_invokes_onClick_with_selectedDate() {
        // --- GIVEN ---
        val habitData = createHabitWithWeeklyData(completedPages = BigDecimal.ZERO)
        var clickedId: Long? = null
        var clickedDate: LocalDate? = null

        composeTestRule.setContent {
            UniqueWeeklyCard(
                startOfWeek = startOfWeek,
                endOfWeek = endOfWeek,
                selectedDate = selectedDateWeek,
                habit = habitData,
                onClickCard = {},
                onClick = { id, date ->
                    clickedId = id
                    clickedDate = date
                },
                onLongClick = { _, _ -> }
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithContentDescription("add habit")
            .performClick()

        // --- THEN ---
        assertEquals(2L, clickedId)
        assertEquals(selectedDateWeek, clickedDate)
    }

    private val separateFakeHabit = Habit(
        id = 3L,
        name = "Hacer Ejercicio",
        description = "Entrenamiento diario",
        goal = BigDecimal("1"),
        unit = UnitHabit.TIMES,
        color = 0xFF4CAF50.toInt(),
        icon = Icons.Default.FitnessCenter,
        typeHabit = TypeHabit.Weekly(numberDays = 5, weeklyGoal = false)
    )

    private val separateStartOfWeek = LocalDate.of(2026, 7, 20)
    private val separateEndOfWeek = LocalDate.of(2026, 7, 26)
    private val separateSelectedDate = LocalDate.of(2026, 7, 22)


    private fun createSeparateHabitWithDailyRecords(
        selectedDateProgress: BigDecimal = BigDecimal.ZERO,
        completedDaysCount: Int = 0
    ): HabitWithDailyHabit {
        val dailyHabits = mutableListOf<HabitDay>()

        // 1. Registro del día actualmente enfocado/seleccionado
        dailyHabits.add(
            HabitDay(
                id = 200L,
                idHabit = separateFakeHabit.id,
                date = separateSelectedDate,
                goalDone = selectedDateProgress
            )
        )

        // 2. Registros simulados de días adicionales completados en la semana
        var added = 0
        var currentDay = separateStartOfWeek
        while (added < completedDaysCount && currentDay <= separateEndOfWeek) {
            if (currentDay != separateSelectedDate) {
                dailyHabits.add(
                    HabitDay(
                        id = 200L + added + 1,
                        idHabit = separateFakeHabit.id,
                        date = currentDay,
                        goalDone = BigDecimal("1")
                    )
                )
                added++
            }
            currentDay = currentDay.plusDays(1)
        }

        return HabitWithDailyHabit(
            habit = separateFakeHabit,
            dailyHabits = dailyHabits
        )
    }

    @Test
    fun given_no_progress_on_selected_date_when_rendered_then_displays_add_icon() {
        // --- GIVEN ---
        val habitData = createSeparateHabitWithDailyRecords(selectedDateProgress = BigDecimal.ZERO)

        // --- WHEN ---
        composeTestRule.setContent {
            SeparateWeeklyCard(
                startOfWeek = separateStartOfWeek,
                endOfWeek = separateEndOfWeek,
                selectedDate = separateSelectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        // --- THEN ---
        composeTestRule
            .onNodeWithContentDescription("add habit")
            .assertIsDisplayed()
    }


    @Test
    fun given_separateWeeklyCard_when_action_button_clicked_then_invokes_onClick_with_selectedDate() {
        // --- GIVEN ---
        val habitData = createSeparateHabitWithDailyRecords()
        var clickedId: Long? = null
        var clickedDate: LocalDate? = null

        composeTestRule.setContent {
            SeparateWeeklyCard(
                startOfWeek = separateStartOfWeek,
                endOfWeek = separateEndOfWeek,
                selectedDate = separateSelectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { id, date ->
                    clickedId = id
                    clickedDate = date
                },
                onLongClick = { _, _ -> }
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithContentDescription("add habit")
            .performClick()

        // --- THEN ---
        assertEquals(separateFakeHabit.id, clickedId)
        assertEquals(separateSelectedDate, clickedDate)
    }

    private val monthlySeparateFakeHabit = Habit(
        id = 4L,
        name = "Meditar",
        description = "Sesión mensual de meditación",
        goal = BigDecimal("1"),
        unit = UnitHabit.TIMES,
        color = 0xFF9C27B0.toInt(),
        icon = Icons.Default.SelfImprovement,
        typeHabit = TypeHabit.Monthly(numberTimes = 15, monthlyGoal = false)
    )

    private val monthlySeparateStartOfMonth = LocalDate.of(2026, 7, 1)
    private val monthlySeparateSelectedDate = LocalDate.of(2026, 7, 15)

    private fun createMonthlySeparateHabitWithRecords(
        selectedDateProgress: BigDecimal = BigDecimal.ZERO,
        completedDaysCount: Int = 0
    ): HabitWithDailyHabit {
        val dailyHabits = mutableListOf<HabitDay>()

        dailyHabits.add(
            HabitDay(
                id = 300L,
                idHabit = monthlySeparateFakeHabit.id,
                date = monthlySeparateSelectedDate,
                goalDone = selectedDateProgress
            )
        )

        var added = 0
        var currentDay = monthlySeparateStartOfMonth
        val endOfMonth = monthlySeparateStartOfMonth.plusMonths(1).minusDays(1)

        while (added < completedDaysCount && currentDay <= endOfMonth) {
            if (currentDay != monthlySeparateSelectedDate) {
                dailyHabits.add(
                    HabitDay(
                        id = 300L + added + 1,
                        idHabit = monthlySeparateFakeHabit.id,
                        date = currentDay,
                        goalDone = BigDecimal("1")
                    )
                )
                added++
            }
            currentDay = currentDay.plusDays(1)
        }

        return HabitWithDailyHabit(
            habit = monthlySeparateFakeHabit,
            dailyHabits = dailyHabits
        )
    }

    @Test
    fun given_no_progress_on_selected_date_when_month_rendered_then_displays_add_icon() {
        // --- GIVEN ---
        val habitData = createMonthlySeparateHabitWithRecords(selectedDateProgress = BigDecimal.ZERO)

        // --- WHEN ---
        composeTestRule.setContent {
            SeparateMonthlyCard(
                startOfMonth = monthlySeparateStartOfMonth,
                selectedDate = monthlySeparateSelectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        // --- THEN ---
        composeTestRule
            .onNodeWithContentDescription("add habit")
            .assertIsDisplayed()
    }

    @Test
    fun given_separateMonthlyCard_when_action_button_clicked_then_invokes_onClick_with_selectedDate() {
        // --- GIVEN ---
        val habitData = createMonthlySeparateHabitWithRecords()
        var clickedId: Long? = null
        var clickedDate: LocalDate? = null

        composeTestRule.setContent {
            SeparateMonthlyCard(
                startOfMonth = monthlySeparateStartOfMonth,
                selectedDate = monthlySeparateSelectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { id, date ->
                    clickedId = id
                    clickedDate = date
                },
                onLongClick = { _, _ -> }
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithContentDescription("add habit")
            .performClick()

        // --- THEN ---
        assertEquals(monthlySeparateFakeHabit.id, clickedId)
        assertEquals(monthlySeparateSelectedDate, clickedDate)
    }

    private val monthlyUniqueFakeHabit = Habit(
        id = 5L,
        name = "Ahorrar Dinero",
        description = "Meta mensual acumulada",
        goal = BigDecimal("500"),
        unit = UnitHabit.ATTEMPTS,
        color = 0xFF009688.toInt(),
        icon = Icons.Default.Savings
    )

    private val monthlyUniqueStartOfMonth = LocalDate.of(2026, 7, 1)
    private val monthlyUniqueSelectedDate = LocalDate.of(2026, 7, 15)


    private fun createMonthlyUniqueHabitWithRecords(
        accumulatedProgress: BigDecimal
    ): HabitWithDailyHabit {
        val dailyHabits = mutableListOf<HabitDay>()

        if (accumulatedProgress > BigDecimal.ZERO) {
            dailyHabits.add(
                HabitDay(
                    id = 400L,
                    idHabit = monthlyUniqueFakeHabit.id,
                    date = monthlyUniqueStartOfMonth,
                    goalDone = accumulatedProgress
                )
            )
        }

        return HabitWithDailyHabit(
            habit = monthlyUniqueFakeHabit,
            dailyHabits = dailyHabits
        )
    }

    @Test
    fun given_no_monthly_progress_when_rendered_then_displays_add_icon() {
        // --- GIVEN ---
        val habitData = createMonthlyUniqueHabitWithRecords(accumulatedProgress = BigDecimal.ZERO)

        // --- WHEN ---
        composeTestRule.setContent {
            UniqueMonthlyCard(
                startOfMonth = monthlyUniqueStartOfMonth,
                selectedDate = monthlyUniqueSelectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        // --- THEN ---
        composeTestRule
            .onNodeWithContentDescription("add habit")
            .assertIsDisplayed()
    }

    @Test
    fun given_full_monthly_goal_reached_when_rendered_then_displays_check_icon() {
        // --- GIVEN ---
        val habitData = createMonthlyUniqueHabitWithRecords(accumulatedProgress = BigDecimal("500"))

        // --- WHEN ---
        composeTestRule.setContent {
            UniqueMonthlyCard(
                startOfMonth = monthlyUniqueStartOfMonth,
                selectedDate = monthlyUniqueSelectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        composeTestRule.mainClock.advanceTimeBy(600)

        // --- THEN ---
        composeTestRule
            .onNodeWithContentDescription("check habit")
            .assertIsDisplayed()
    }

    @Test
    fun given_uniqueMonthlyCard_when_action_button_clicked_then_invokes_onClick_with_selectedDate() {
        // --- GIVEN ---
        val habitData = createMonthlyUniqueHabitWithRecords(accumulatedProgress = BigDecimal.ZERO)
        var clickedId: Long? = null
        var clickedDate: LocalDate? = null

        composeTestRule.setContent {
            UniqueMonthlyCard(
                startOfMonth = monthlyUniqueStartOfMonth,
                selectedDate = monthlyUniqueSelectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { id, date ->
                    clickedId = id
                    clickedDate = date
                },
                onLongClick = { _, _ -> }
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithContentDescription("add habit")
            .performClick()

        // --- THEN ---
        assertEquals(monthlyUniqueFakeHabit.id, clickedId)
        assertEquals(monthlyUniqueSelectedDate, clickedDate)
    }

    private val recurringFakeHabit = Habit(
        id = 6L,
        name = "Regar Plantas",
        description = "Hábito recurrente cada N días",
        goal = BigDecimal("1"),
        unit = UnitHabit.TIMES,
        color = 0xFF8BC34A.toInt(),
        icon = Icons.Default.LocalFlorist
    )

    private val recurringSelectedDate = LocalDate.of(2026, 7, 27)


    private fun createRecurringHabitWithRecord(
        progress: BigDecimal = BigDecimal.ZERO
    ): HabitWithDailyHabit {
        val dailyHabits = mutableListOf(
            HabitDay(
                id = 500L,
                idHabit = recurringFakeHabit.id,
                date = recurringSelectedDate,
                goalDone = progress
            )
        )

        return HabitWithDailyHabit(
            habit = recurringFakeHabit,
            dailyHabits = dailyHabits
        )
    }

    @Test
    fun given_no_progress_when_rendered_then_displays_add_icon() {
        // --- GIVEN ---
        val habitData = createRecurringHabitWithRecord(progress = BigDecimal.ZERO)

        // --- WHEN ---
        composeTestRule.setContent {
            RecurringCard(
                selectedDate = recurringSelectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        // --- THEN ---
        composeTestRule
            .onNodeWithContentDescription("add habit")
            .assertIsDisplayed()
    }

    @Test
    fun given_goal_completed_when_rendered_then_displays_check_icon() {
        // --- GIVEN ---
        val habitData = createRecurringHabitWithRecord(progress = BigDecimal("1"))

        // --- WHEN ---
        composeTestRule.setContent {
            RecurringCard(
                selectedDate = recurringSelectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        composeTestRule.mainClock.advanceTimeBy(600)

        // --- THEN ---
        composeTestRule
            .onNodeWithContentDescription("check habit")
            .assertIsDisplayed()
    }

    @Test
    fun given_recurringCard_when_card_body_clicked_then_invokes_onClickCard() {
        // --- GIVEN ---
        val habitData = createRecurringHabitWithRecord()
        var clickedHabitId: Long? = null

        composeTestRule.setContent {
            RecurringCard(
                selectedDate = recurringSelectedDate,
                habit = habitData,
                onClickCard = { id -> clickedHabitId = id },
                onClick = { _, _ -> },
                onLongClick = { _, _ -> }
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithText("Regar Plantas")
            .performClick()

        // --- THEN ---
        assertEquals(recurringFakeHabit.id, clickedHabitId)
    }

    @Test
    fun given_recurringCard_when_action_button_clicked_then_invokes_onClick_with_selectedDate() {
        // --- GIVEN ---
        val habitData = createRecurringHabitWithRecord()
        var clickedId: Long? = null
        var clickedDate: LocalDate? = null

        composeTestRule.setContent {
            RecurringCard(
                selectedDate = recurringSelectedDate,
                habit = habitData,
                onClickCard = {},
                onClick = { id, date ->
                    clickedId = id
                    clickedDate = date
                },
                onLongClick = { _, _ -> }
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithContentDescription("add habit")
            .performClick()

        // --- THEN ---
        assertEquals(recurringFakeHabit.id, clickedId)
        assertEquals(recurringSelectedDate, clickedDate)
    }

    @Test
    fun given_configure_bt_when_clicked_cross_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            VerticalConfigureHabitBottomSheet(
                habitWithDay = HabitWithDay(),
                onDismiss = { clicked = true },
                onRestart = {_,_ ->},
                onClickTimer = {},
                onClick = {_,_,_ ->},
            )
        }

        composeTestRule.onNodeWithTag("configure_habit_close_button").performClick()
        composeTestRule.waitForIdle()

        assertTrue(clicked)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun given_restart_day_when_clicked_cross_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            val coroutineScope = rememberCoroutineScope()
            val sheetState = rememberModalBottomSheetState()

            RestartDay(
                habitWithDay = HabitWithDay(),
                coroutineScope = coroutineScope,
                onDismiss = { clicked = true },
                sheetState = sheetState,
                onRestart = {_,_ ->}
            )
        }

        composeTestRule.onNodeWithTag("habit_cancel_button_restart_day").performClick()
        composeTestRule.waitForIdle()

        assertTrue(clicked)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun given_restart_day_when_clicked_accept_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            val coroutineScope = rememberCoroutineScope()
            val sheetState = rememberModalBottomSheetState()

            RestartDay(
                habitWithDay = HabitWithDay(),
                coroutineScope = coroutineScope,
                onDismiss = { clicked = true },
                sheetState = sheetState,
                onRestart = {_,_ ->  clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("habit_confirm_button_restart_day").performClick()
        composeTestRule.waitForIdle()

        assertTrue(clicked)
    }

    @Test
    fun given_delete_habit_bt_when_clicked_delete_then_perform_click(){
        var clicked = false

        composeTestRule.setContent {
            DeleteHabitBottomSheet(
                colorButton = 0,
                onDismiss = { clicked = true},
                onAcceptDelete = {}
            )
        }

        composeTestRule.onNodeWithTag("bt_delete_cancel_habit").performClick()
        composeTestRule.waitForIdle()

        assertTrue(clicked)
    }

    @Test
    fun given_delete_habit_bt_when_clicked_on_accept_delete_then_perform_click(){
        var clicked = false

        composeTestRule.setContent {
            DeleteHabitBottomSheet(
                colorButton = 0,
                onDismiss = {},
                onAcceptDelete = {  clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("bt_on_accept_delete_cancel_habit").performClick()
        composeTestRule.waitForIdle()

        assertTrue(clicked)
    }

}