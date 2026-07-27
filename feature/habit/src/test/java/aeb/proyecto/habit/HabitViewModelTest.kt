package aeb.proyecto.habit

import aeb.proyecto.domain.usecase.habit.GetDailyHabitUseCase
import aeb.proyecto.domain.usecase.habit.GetHabitUseCase
import aeb.proyecto.domain.usecase.habit.GetTypesOfHabitUseCase
import aeb.proyecto.domain.usecase.habit.HabitDatastoreUseCase
import aeb.proyecto.domain.usecase.save.SaveAuthenticationUseCase
import aeb.proyecto.domain.usecase.save.SaveFirestoreUseCase
import aeb.proyecto.domain.usecase.save.SaveHabitsRepositoryUseCase
import aeb.proyecto.domain.usecase.save.SaveNotificationUseCase
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.habit.model.pager.PagerElement
import aeb.proyecto.habit.model.pager.PagerSelected
import aeb.proyecto.habit.utils.initializeSelectedTypeIfNeeded
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.DAILY_TAG
import aeb.proyecto.room.model.classes.MONTHLY_TAG
import aeb.proyecto.room.model.classes.RECURRING_TAG
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.UnitType
import aeb.proyecto.room.model.classes.WEEKLY_TAG
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertSame
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.theories.suppliers.TestedOn
import java.time.DayOfWeek
import java.time.LocalDate

class HabitViewModelTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val mockGetTypesOfHabitUseCase = mockk<GetTypesOfHabitUseCase>(relaxed = true)
    private val mockGetDailyHabitUseCase = mockk<GetDailyHabitUseCase>(relaxed = true)
    private val mockHabitDatastoreUseCase = mockk<HabitDatastoreUseCase>(relaxed = true)
    private val mockGetHabitUseCase = mockk<GetHabitUseCase>(relaxed = true)

    private lateinit var viewModel: HabitViewModel

    @Before
    fun setUp(){
        every { mockGetTypesOfHabitUseCase() } returns emptyFlow()
        every { mockHabitDatastoreUseCase.startDayOfWeek } returns flowOf(DayOfWeek.MONDAY)
    }

    fun initViewModel(){
        viewModel = HabitViewModel(
            getTypesOfHabitUseCase = mockGetTypesOfHabitUseCase,
            getDailyHabitUseCase = mockGetDailyHabitUseCase,
            habitDatastoreUseCase = mockHabitDatastoreUseCase,
            getHabitUseCase = mockGetHabitUseCase
        )
    }


    @Test
    fun `given availablePagerTypesUiState when initially then emits Loading`() = runTest {
        // --- GIVEN ---
        every { mockGetTypesOfHabitUseCase() } returns emptyFlow()

        // --- WHEN ---
        initViewModel()

        // --- THEN ---
        assertEquals(PagerTypesUiState.Loading, viewModel.availablePagerTypesUiState.value)
    }

    @Test
    fun `given availablePagerTypesUiState when emits Error when use case throws exception`() = runTest {
        // --- GIVEN ---
        every { mockGetTypesOfHabitUseCase() } returns flow {
            throw RuntimeException("Database error")
        }

        // --- WHEN / THEN ---
        initViewModel()

        viewModel.availablePagerTypesUiState.test {
            assertEquals(PagerTypesUiState.Loading, awaitItem())

            val errorItem = awaitItem()
            assertEquals(PagerTypesUiState.Error, errorItem)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when pager tag is unknown or uninitialized, emits Empty state`() = runTest {
        // --- GIVEN / WHEN ---
        initViewModel()

        // --- THEN ---
        viewModel.selectedTimeRangeUiState.test {
            assertEquals(TimeRangeUiState.Empty, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when TimeRangeUiState is Empty, emits FilteredHabitsUiState Empty`() = runTest {
        // --- GIVEN ---
        initViewModel()

        // --- WHEN / THEN ---
        viewModel.habitsForSelectedTimeUiState.test {
            assertEquals(FilteredHabitsUiState.Loading, awaitItem())

            val item = awaitItem()
            assertEquals(FilteredHabitsUiState.Empty, item)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `when UseCase throws Exception, catches error and emits FilteredHabitsUiState Error`() = runTest {
        // --- GIVEN ---
        val date = LocalDate.now()
        every {
            mockGetDailyHabitUseCase.getDailyHabitsByType(date, date, DAILY_TAG)
        } returns flow {
            throw RuntimeException("Database error")
        }

        initViewModel()

        // --- WHEN / THEN ---
        viewModel.habitsForSelectedTimeUiState.test {
            assertEquals(FilteredHabitsUiState.Loading, awaitItem())

            viewModel.onPagerTypeSelected(PagerElement.DAILY)

            val errorState = awaitItem()
            assertEquals(FilteredHabitsUiState.Empty, errorState)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onPagerTypeSelected when element does not exist in available types, does nothing`() = runTest {
        // --- GIVEN ---
        val availableTypes = listOf(DAILY_TAG)
        every { mockGetTypesOfHabitUseCase() } returns flowOf(availableTypes)

        initViewModel()

        val initialState = viewModel.currentPagerType.value

        // --- WHEN ---
        viewModel.onPagerTypeSelected(PagerElement.MONTHLY)

        // --- THEN ---
        assertEquals(initialState, viewModel.currentPagerType.value)
        coVerify(exactly = 0) { mockHabitDatastoreUseCase.setSelectedHabitType(any()) }
    }

    @Test
    fun `onBottomSheetSelectDateSelected updates bottomSheetUIState enabling SelectDate`() = runTest {
        // --- GIVEN ---
        initViewModel()

        // --- WHEN ---
        viewModel.onBottomSheetSelectDateSelected()

        // --- THEN ---
        val expectedType = TypeBottomSheet.SelectDate(enabled = true)
        assertEquals(expectedType, viewModel.bottomSheetUIState.value.enabledSelectDateState)
    }

    @Test
    fun `onDismissBottomSheet with EditHabit disables enabledEditHabitState`() = runTest {
        // --- GIVEN ---
        initViewModel()

        // --- WHEN ---
        viewModel.onDismissBottomSheet(TypeBottomSheet.EditHabit(enabled = true))

        // --- THEN ---
        val expectedState = TypeBottomSheet.EditHabit(enabled = false)
        assertEquals(expectedState, viewModel.bottomSheetUIState.value.enabledEditHabitState)
    }

    @Test
    fun `onDismissBottomSheet with DeleteHabit disables enabledDeleteHabitState`() = runTest {
        // --- GIVEN ---
        initViewModel()

        // --- WHEN ---
        viewModel.onDismissBottomSheet(TypeBottomSheet.DeleteHabit(enabled = true))

        // --- THEN ---
        val expectedState = TypeBottomSheet.DeleteHabit(enabled = false)
        assertEquals(expectedState, viewModel.bottomSheetUIState.value.enabledDeleteHabitState)
    }

    @Test
    fun `onClickCard updates bottomSheetUIState enabling EditHabit with correct habit id`() = runTest {
        // --- GIVEN ---
        initViewModel()
        val targetHabitId = 42L

        // --- WHEN ---
        viewModel.onClickCard(targetHabitId)

        // --- THEN ---
        val expectedState = TypeBottomSheet.EditHabit(enabled = true, idHabit = targetHabitId)
        assertEquals(expectedState, viewModel.bottomSheetUIState.value.enabledEditHabitState)
    }

    @Test
    fun `onClickDelete updates bottomSheetUIState enabling DeleteHabit with correct id and color`() = runTest {
        // --- GIVEN ---
        initViewModel()
        val targetHabitId = 15L
        val targetColor = 0xFF4285F4.toInt()

        // --- WHEN ---
        viewModel.onClickDelete(id = targetHabitId, color = targetColor)

        // --- THEN ---
        val expectedState = TypeBottomSheet.DeleteHabit(
            enabled = true,
            id = targetHabitId,
            color = targetColor
        )
        assertEquals(expectedState, viewModel.bottomSheetUIState.value.enabledDeleteHabitState)
    }

    private val dailyElement = PagerElement.DAILY
    private val weeklyElement = PagerElement.WEEKLY
    private val monthlyElement = PagerElement.MONTHLY

    private val sortedTypes = listOf(dailyElement, weeklyElement, monthlyElement)

    @Test
    fun `when memory has a valid selection, respects memory and updates datastore`() = runTest {
        // --- GIVEN ---
        val initialSelectedState = CurrentPagerSelection.Selected(
            PagerSelected(index = 1, pagerElement = weeklyElement)
        )
        val selectedTypeFlow = MutableStateFlow<CurrentPagerSelection>(initialSelectedState)

        var updatedSelection: CurrentPagerSelection? = null

        // --- WHEN ---
        val result = initializeSelectedTypeIfNeeded(
            sortedTypes = sortedTypes,
            selectedType = selectedTypeFlow,
            habitDatastoreUseCase = mockHabitDatastoreUseCase,
            updateSelected = { updatedSelection = it }
        )

        // --- THEN ---
        assertTrue(result)

        val expected = CurrentPagerSelection.Selected(PagerSelected(index = 1, pagerElement = weeklyElement))
        assertEquals(expected, updatedSelection)

        coVerify(exactly = 1) { mockHabitDatastoreUseCase.setSelectedHabitType(weeklyElement.tag) }
        coVerify(exactly = 0) { mockHabitDatastoreUseCase.getTypeSelected() }
    }

    @Test
    fun `when memory is uninitialized, falls back to saved tag in DataStore`() = runTest {
        // --- GIVEN ---
        val selectedTypeFlow = MutableStateFlow<CurrentPagerSelection>(CurrentPagerSelection.Uninitialized)
        coEvery { mockHabitDatastoreUseCase.getTypeSelected() } returns monthlyElement.tag

        var updatedSelection: CurrentPagerSelection? = null

        // --- WHEN ---
        val result = initializeSelectedTypeIfNeeded(
            sortedTypes = sortedTypes,
            selectedType = selectedTypeFlow,
            habitDatastoreUseCase = mockHabitDatastoreUseCase,
            updateSelected = { updatedSelection = it }
        )

        // --- THEN ---
        assertTrue(result)

        val expected = CurrentPagerSelection.Selected(PagerSelected(index = 2, pagerElement = monthlyElement))
        assertEquals(expected, updatedSelection)

        coVerify(exactly = 1) { mockHabitDatastoreUseCase.getTypeSelected() }
        coVerify(exactly = 1) { mockHabitDatastoreUseCase.setSelectedHabitType(monthlyElement.tag) }
    }

    @Test
    fun `when memory and DataStore are empty, falls back to first element in sortedTypes`() = runTest {
        // --- GIVEN ---
        val selectedTypeFlow = MutableStateFlow<CurrentPagerSelection>(CurrentPagerSelection.Uninitialized)
        coEvery { mockHabitDatastoreUseCase.getTypeSelected() } returns null

        var updatedSelection: CurrentPagerSelection? = null

        // --- WHEN ---
        val result = initializeSelectedTypeIfNeeded(
            sortedTypes = sortedTypes,
            selectedType = selectedTypeFlow,
            habitDatastoreUseCase = mockHabitDatastoreUseCase,
            updateSelected = { updatedSelection = it }
        )

        // --- THEN ---
        assertTrue(result)

        val expected = CurrentPagerSelection.Selected(PagerSelected(index = 0, pagerElement = dailyElement))
        assertEquals(expected, updatedSelection)

        coVerify(exactly = 1) { mockHabitDatastoreUseCase.setSelectedHabitType(dailyElement.tag) }
    }

    @Test
    fun `when DataStore throws exception, catches error safely and returns false`() = runTest {
        // --- GIVEN ---
        val selectedTypeFlow = MutableStateFlow<CurrentPagerSelection>(CurrentPagerSelection.Uninitialized)
        coEvery { mockHabitDatastoreUseCase.getTypeSelected() } throws RuntimeException("Disk IO Failure")

        var updateCalled = false

        // --- WHEN ---
        val result = initializeSelectedTypeIfNeeded(
            sortedTypes = sortedTypes,
            selectedType = selectedTypeFlow,
            habitDatastoreUseCase = mockHabitDatastoreUseCase,
            updateSelected = { updateCalled = true }
        )

        // --- THEN ---
        assertFalse(result)
        assertFalse(updateCalled)
    }

}