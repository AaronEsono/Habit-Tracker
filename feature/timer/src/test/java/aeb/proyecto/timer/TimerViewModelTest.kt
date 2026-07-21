package aeb.proyecto.timer

import aeb.proyecto.domain.usecase.settings.DataSettingsUseCase
import aeb.proyecto.domain.usecase.timer.GetTimerDataUseCase
import aeb.proyecto.domain.usecase.timer.TimeEntriesUseCase
import aeb.proyecto.domain.usecase.timer.TimerData
import aeb.proyecto.domain.usecase.timer.TimerDataStoreUseCase
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.TimeEntry
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import aeb.proyecto.stopwatch.helper.StopWatchHelper
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import aeb.proyecto.timer.model.HabitLinkedState
import aeb.proyecto.timer.model.HourSelectedState
import aeb.proyecto.timer.model.TimeEntryState
import aeb.proyecto.timer.model.TimerServiceUIState
import android.util.Log
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class TimerViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule(testDispatcher)

    private val mockServiceHelper = mockk<StopWatchHelper>(relaxed = true)
    private val mockGetTimerDataUseCase = mockk<GetTimerDataUseCase>()
    private val mockTimerDataStoreUseCase = mockk<TimerDataStoreUseCase>(relaxed = true)
    private val mockStopWatchStateManager = mockk<StopWatchStateManager>(relaxed = true)
    private val mockTimeEntriesUseCase = mockk<TimeEntriesUseCase>(relaxed = true)

    private val timerDataFlow = MutableSharedFlow<TimerData>()

    private lateinit var viewModel: TimerViewModel


    @Before
    fun setUp() {
        mockkStatic(Dispatchers::class)

        every { mockGetTimerDataUseCase.timerData } returns timerDataFlow

        Dispatchers.setMain(testDispatcher)
        every { Dispatchers.Default } returns testDispatcher
        every { Dispatchers.IO } returns testDispatcher

        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Dispatchers::class)
        unmockkStatic(Log::class)
        Dispatchers.resetMain()
    }

    fun initViewModel(){
        viewModel = TimerViewModel(
            mockServiceHelper,
            mockGetTimerDataUseCase,
            mockTimerDataStoreUseCase,
            mockStopWatchStateManager,
            mockTimeEntriesUseCase
        )
    }

    // Timer data ---------
    @Test
    fun `when data is collected, then starts with Loading and transitions to Success with mapped values`() = runTest{
        initViewModel()

        viewModel.timerData.test {
            assertEquals(TimerUiState.Loading, awaitItem())

            // --- GIVEN ---
            val fakeData = TimerData(
                typeTimer = 1,
                hourSelected = null,
                restHour = null,
                sets = 0,
                habitWithDay = null
            )

            // --- WHEN ---
            timerDataFlow.emit(fakeData)

            // --- THEN ---
            val successItem = awaitItem()
            assertTrue(successItem is TimerUiState.Success)

            val uiStateData = (successItem as TimerUiState.Success).timerDataUIState
            assertEquals(HourSelectedState.NoData, uiStateData.hourSelected)
            assertEquals(HourSelectedState.NoData, uiStateData.restHour)
            assertEquals(HabitLinkedState.NoData, uiStateData.habitLinked)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given timer type is StopWatch, when data is emitted, then action button must be enabled`() = runTest {
        initViewModel()

        viewModel.timerData.test {
            assertEquals(TimerUiState.Loading, awaitItem())

            val stopWatchData = TimerData(typeTimer = 1, hourSelected = Triple(0,10,0), restHour = null, sets = 0, habitWithDay = null)
            timerDataFlow.emit(stopWatchData)

            val successItem = awaitItem() as TimerUiState.Success
            assertTrue(successItem.timerDataUIState.buttonEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given timer type is CountDown and hours are zero, when data is emitted, then action button must be disabled`() = runTest {
        initViewModel()

        viewModel.timerData.test {
            assertEquals(TimerUiState.Loading, awaitItem())

            val countDownZeroData = TimerData(typeTimer = 2, hourSelected = Triple(0, 0, 0), restHour = null, sets = 0, habitWithDay = null)
            timerDataFlow.emit(countDownZeroData)

            val successItem = awaitItem() as TimerUiState.Success
            assertFalse(successItem.timerDataUIState.buttonEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // timerStopWatchUIState ------------
    private val elapsedTimeFlow = MutableStateFlow(0L)
    private val typeTimerFlow = MutableStateFlow(TypeTimer.STOPWATCH)
    private val currentStateFlow = MutableStateFlow(StopwatchState.Idle)
    private val timerStringFlow = MutableStateFlow("00:00:00")
    private val habitLinkedFlow = MutableStateFlow<HabitWithDay?>(null)

    fun setupStopWatchManagerMocks() {
        every { mockStopWatchStateManager.elapsedTime } returns elapsedTimeFlow
        every { mockStopWatchStateManager.typeTimer } returns typeTimerFlow
        every { mockStopWatchStateManager.currentState } returns currentStateFlow
        every { mockStopWatchStateManager.timerString } returns timerStringFlow
        every { mockStopWatchStateManager.habitLinked } returns habitLinkedFlow
    }

    @Test
    fun `given currentState is Idle, when flow collects, then emit NoTimer`() = runTest {
        setupStopWatchManagerMocks()
        initViewModel()

        viewModel.timerStopWatchUIState.test {
            assertEquals(TimerServiceUIState.NoTimer, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given currentState is InProgress, when values change, then emit TimerRunning with combined data`() = runTest {
        setupStopWatchManagerMocks()
        initViewModel()

        viewModel.timerStopWatchUIState.test {
            assertEquals(TimerServiceUIState.NoTimer, awaitItem())

            // --- GIVEN ---
            val expectedTime = 5000L
            val expectedType = TypeTimer.STOPWATCH
            val expectedState = StopwatchState.InProgress
            val expectedString = "00:00:05"

            // --- WHEN ---
            elapsedTimeFlow.value = expectedTime
            typeTimerFlow.value = expectedType
            timerStringFlow.value = expectedString
            currentStateFlow.value = expectedState

            // --- THEN ---
            val emittedItem = awaitItem()
            assertTrue(emittedItem is TimerServiceUIState.TimerRunning)

            val runningData = (emittedItem as TimerServiceUIState.TimerRunning)
            assertEquals(expectedTime, runningData.elapsedTime)
            assertEquals(expectedType, runningData.typeTimer)
            assertEquals(expectedState, runningData.currentState)
            assertEquals(expectedString, runningData.hourString)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given active timer, when currentState goes back to Idle, then transition back to NoTimer`() = runTest {
        setupStopWatchManagerMocks()
        initViewModel()

        viewModel.timerStopWatchUIState.test {
            assertEquals(TimerServiceUIState.NoTimer, awaitItem())

            currentStateFlow.value = StopwatchState.InProgress
            assertTrue(awaitItem() is TimerServiceUIState.TimerRunning)

            // 2. --- WHEN ---
            currentStateFlow.value = StopwatchState.Idle

            // 3. --- THEN ---
            assertEquals(TimerServiceUIState.NoTimer, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    // historyEntries ------
    private val timeEntriesFlow = MutableSharedFlow<List<TimeEntryWithHabit>>()

    fun prepareTimeEntriesData() {
        every { mockTimeEntriesUseCase.getTimeEntries() } returns timeEntriesFlow
    }

    @Test
    fun `given empty database entries, when historyEntries collects, then emit EmptyList`() = runTest {
        // --- GIVEN ---
        prepareTimeEntriesData()
        initViewModel()

        viewModel.historyEntries.test {
            assertEquals(TimeEntryState.EmptyList, awaitItem())

            // --- WHEN ---
            timeEntriesFlow.emit(emptyList())

            // --- THEN ---
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given existing database entries, when historyEntries collects, then emit TimeEntries state`() = runTest {
        // --- GIVEN ---
        prepareTimeEntriesData()
        initViewModel()

        viewModel.historyEntries.test {
            assertEquals(TimeEntryState.EmptyList, awaitItem())

            // --- WHEN ---
            val fakeEntries = listOf(
                TimeEntryWithHabit(
                    timeEntry = TimeEntry(),
                    habit = Habit()
                )
            )
            timeEntriesFlow.emit(fakeEntries)

            // --- THEN ---
            val successItem = awaitItem()
            assertTrue(successItem is TimeEntryState.TimeEntries)

            assertEquals(fakeEntries, (successItem as TimeEntryState.TimeEntries).timeEntries)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given StopWatch mode, when startService is called, then trigger startForegroundServiceOnStopWatch`() = runTest {
        // --- GIVEN ---
        initViewModel()

        val fakeData = TimerData(
            typeTimer = 0,
            hourSelected = null,
            restHour = null,
            sets = 0,
            habitWithDay = null
        )

        timerDataFlow.emit(fakeData)

        viewModel.timerData.test {
            assertEquals(TimerUiState.Loading, awaitItem())

            timerDataFlow.emit(fakeData)

            val successItem = awaitItem()
            assertTrue(successItem is TimerUiState.Success)

            // --- WHEN ---
            viewModel.startService()

            // --- THEN ---
            verify(exactly = 1) {
                mockServiceHelper.startForegroundServiceOnStopWatch( Pair(-1L, ""))
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given Timer mode with specific time, when startService is called, then trigger startForegroundServiceOnTimer with converted milliseconds`() = runTest {
        // --- GIVEN ---
        initViewModel()

        val expectedMilliseconds = 3723000L
        val fakeData = TimerData(
            typeTimer = 1,
            hourSelected = Triple(1, 2, 3),
            restHour = null,
            sets = 0,
            habitWithDay = null
        )

        viewModel.timerData.test {
            assertEquals(TimerUiState.Loading, awaitItem())

            timerDataFlow.emit(fakeData)

            val successItem = awaitItem()
            assertTrue(successItem is TimerUiState.Success)

            // --- WHEN ---
            viewModel.startService()

            // --- THEN ---
            verify(exactly = 1) {
                mockServiceHelper.startForegroundServiceOnTimer(expectedMilliseconds, Pair(-1L, ""))
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given Interval mode with sets and rest, when startService is called, then trigger startForegroundServiceOnInterval`() = runTest {
        // --- GIVEN ---
        initViewModel()

        val expectedWorkMs = 300000L
        val expectedRestMs = 90000L
        val expectedSets = 4

        val fakeData = TimerData(
            typeTimer = 2,
            hourSelected = Triple(0, 5, 0),
            restHour = Triple(0, 1, 30),
            sets = expectedSets,
            habitWithDay = null
        )

        viewModel.timerData.test {
            assertEquals(TimerUiState.Loading, awaitItem())

            timerDataFlow.emit(fakeData)

            val successItem = awaitItem()
            assertTrue(successItem is TimerUiState.Success)

            // --- WHEN ---
            viewModel.startService()

            // --- THEN ---
            verify(exactly = 1) {
                mockServiceHelper.startForegroundServiceOnInterval(
                    expectedWorkMs,
                    expectedRestMs,
                    expectedSets,
                    Pair(-1L, "")
                )
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given active service, when action functions are called, then trigger corresponding serviceHelper methods`() {
        // Given
        initViewModel()

        // --- WHEN ---
        viewModel.finishService()
        viewModel.cancelService()
        viewModel.resumeService()
        viewModel.stopService()

        // --- THEN ---
        verify(exactly = 1) { mockServiceHelper.finishService() }
        verify(exactly = 1) { mockServiceHelper.cancelService() }
        verify(exactly = 1) { mockServiceHelper.resumeService() }
        verify(exactly = 1) { mockServiceHelper.stopService() }
    }

    @Test
    fun `given serviceHelper throws an exception, when actions are called, then catch error and log it without crashing`() {
        // --- GIVEN ---
        initViewModel()
        val expectedErrorMessage = "Service interaction failed"
        every { mockServiceHelper.finishService() } throws RuntimeException(expectedErrorMessage)
        every { mockServiceHelper.cancelService() } throws RuntimeException(expectedErrorMessage)

        // --- WHEN ---
        viewModel.finishService()
        viewModel.cancelService()

        // --- THEN ---
        verify(exactly = 1) { mockServiceHelper.finishService() }
        verify(exactly = 1) { mockServiceHelper.cancelService() }

        verify(exactly = 2) { Log.e("Error", expectedErrorMessage) }
    }

    @Test
    fun `given bottom sheet, when toggled via click and dismiss, then update state correctly`() {
        // Given
        initViewModel()

        // --- WHEN & THEN ---
        viewModel.onClickHabitButton()
        assertTrue(viewModel.bottomSheetState.value)

        viewModel.onDismissHabitBottomSheet()
        assertFalse(viewModel.bottomSheetState.value)
    }

}