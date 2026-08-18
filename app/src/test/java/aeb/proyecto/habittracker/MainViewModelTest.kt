package aeb.proyecto.habittracker

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.domain.usecase.main.ManageDatastoreUseCase
import aeb.proyecto.domain.usecase.main.ManageDialogTimerUseCase
import aeb.proyecto.domain.usecase.main.ManageHabitsUseCase
import aeb.proyecto.domain.usecase.main.ManageOnboardingScreenUseCase
import aeb.proyecto.domain.usecase.main.ShowDialogState
import aeb.proyecto.language.model.EnumLanguage
import aeb.proyecto.language.provider.RegionFirstDayProvider
import aeb.proyecto.room.entities.relations.HabitWithDay
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class MainViewModelTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val manageDatastoreUseCase: ManageDatastoreUseCase = mockk(relaxed = true)
    private val firstDayProvider: RegionFirstDayProvider = mockk(relaxed = true)
    private val manageDialogTimerUseCase: ManageDialogTimerUseCase = mockk(relaxed = true)
    private val manageHabitsUseCase: ManageHabitsUseCase = mockk(relaxed = true)

    private val manageOnboardingScreenUseCase: ManageOnboardingScreenUseCase = mockk(relaxed = true)

    private val showDialogTimerFlow = MutableStateFlow<ShowDialogState>(ShowDialogState.NoShowDialog)
    private val themeModeFlow = MutableStateFlow(0)

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatchersRule.testDispatcher)

        every { manageDialogTimerUseCase.showDialogTimer } returns showDialogTimerFlow
        every { manageDatastoreUseCase.themeMode } returns themeModeFlow

        viewModel = MainViewModel(
            manageDatastoreUseCase = manageDatastoreUseCase,
            firstDayProvider = firstDayProvider,
            manageDialogTimerUseCase = manageDialogTimerUseCase,
            manageHabitsUseCase = manageHabitsUseCase,
            manageOnboardingScreenUseCase = manageOnboardingScreenUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

// =========================================================================
    // setData() TESTS
    // =========================================================================

    @Test
    fun `setData when settings need update should update dayStartWeek and language`() = runTest {
        // GIVEN
        val mockSettings = AppSettings(
            dayStartWeek = "",
            language = ""
        )
        coEvery { manageDatastoreUseCase.getAppSettings() } returns mockSettings
        every { firstDayProvider.getFirstDayOfWeekByLocale() } returns DayOfWeek.SUNDAY

        // WHEN
        viewModel.setData()
        testScheduler.advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) {
            manageDatastoreUseCase.saveSettingsApp(
                match {
                    it.dayStartWeek == DayOfWeek.SUNDAY.name && it.language.isNotEmpty()
                }
            )
        }
    }

    @Test
    fun `setData when settings do not need update should not save settings`() = runTest {
        // GIVEN
        val mockSettings = AppSettings(
            dayStartWeek = DayOfWeek.TUESDAY.name,
            language = "es"
        )
        coEvery { manageDatastoreUseCase.getAppSettings() } returns mockSettings

        // WHEN
        viewModel.setData()
        mainDispatchersRule.testDispatcher.scheduler.advanceUntilIdle()

        // THEN
        coVerify(exactly = 0) { manageDatastoreUseCase.saveSettingsApp(any()) }
    }

    @Test
    fun `setData called twice should execute logic only once due to dataSet guard`() = runTest {
        // GIVEN
        val mockSettings = AppSettings(dayStartWeek = "", language = "")
        coEvery { manageDatastoreUseCase.getAppSettings() } returns mockSettings
        every { firstDayProvider.getFirstDayOfWeekByLocale() } returns DayOfWeek.MONDAY

        // WHEN
        viewModel.setData()
        viewModel.setData()
        mainDispatchersRule.testDispatcher.scheduler.advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) { manageDatastoreUseCase.getAppSettings() }
    }

    @Test
    fun `closeDialog should delegate call to manageDatastoreUseCase`() = runTest {
        // WHEN
        viewModel.closeDialog()
        mainDispatchersRule.testDispatcher.scheduler.advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) { manageDatastoreUseCase.closeDialog() }
    }

    @Test
    fun `clearToast should set showToast state to false`() = runTest {
        // GIVEN: Forzamos showToast a true ejecutando updateHabit
        viewModel.updateHabit()
        mainDispatchersRule.testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.showToast.value)

        // WHEN
        viewModel.clearToast()

        // THEN
        assertFalse(viewModel.showToast.value)
    }

    @Test
    fun `updateHabit when showDialogState is NoShowDialog should only close dialog and show toast`() = runTest {
        // GIVEN
        showDialogTimerFlow.value = ShowDialogState.NoShowDialog

        // WHEN
        viewModel.updateHabit()
        mainDispatchersRule.testDispatcher.scheduler.advanceUntilIdle()

        // THEN
        coVerify(exactly = 0) { manageHabitsUseCase.updateHabit(any(), any(), any()) }
        coVerify(exactly = 1) { manageDatastoreUseCase.closeDialog() }
        assertTrue(viewModel.showToast.value)
    }

    @Test
    fun `setOnboardScreen should delegate onboarding state to use case`() = runTest {
        // GIVEN
        val onboardState = true

        // WHEN
        viewModel.setOnboardScreen(onboardState)
        mainDispatchersRule.testDispatcher.scheduler.advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) {
            manageOnboardingScreenUseCase.setShowOnboardingScreen(onboardState)
        }
    }

}