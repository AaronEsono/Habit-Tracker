package aeb.proyecto.statistics

import aeb.proyecto.domain.usecase.statistics.GetHabitSelectedUseCase
import aeb.proyecto.domain.usecase.statistics.GetHabitsStatisticsUseCase
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.statistics.components.common.donutChart.PieChartState
import aeb.proyecto.statistics.model.DayBoxState
import aeb.proyecto.statistics.model.NUMBER_OF_DAYS
import aeb.proyecto.statistics.model.StatisticsState
import aeb.proyecto.statistics.model.StatisticsSuccessState
import android.util.Log
import app.cash.turbine.test
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import kotlin.collections.emptyList

class StatisticsViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule(testDispatcher)

    private var getHabitsStatisticsUseCase = mockk<GetHabitsStatisticsUseCase>(relaxed = true)
    private var getHabitSelectedUseCase = mockk<GetHabitSelectedUseCase>(relaxed = true)

    private lateinit var viewModel: StatisticsViewModel

    @Before
    fun setUp() {
        mockkStatic(Dispatchers::class)

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


    @Test
    fun `when habits list is empty, then emit Loading and then Success with Empty state`() = runTest {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.statisticsState.test {

            val successState = awaitItem() as StatisticsState.Success
            assertEquals(StatisticsSuccessState.Empty, successState.state)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when habits exist, then emit Success with Habits state selecting habit correctly`() = runTest {
        // --- GIVEN ---
        val fakeHabit1 = Habit(id = 1L, name = "Meditar")
        val fakeHabit2 = Habit(id = 2L, name = "Hacer Ejercicio")
        val fakeHabits = listOf(fakeHabit1, fakeHabit2)
        val fakeHabitWithDaily = HabitWithDailyHabit(habit = fakeHabit2, dailyHabits = mutableListOf())

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(fakeHabits)
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(2L)
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(2L) } returns flowOf(fakeHabitWithDaily)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.statisticsState.test {

            val successItem = awaitItem() as StatisticsState.Success
            val data = successItem.state as StatisticsSuccessState.Habits

            assertEquals(2, data.habits.size)
            assertEquals(fakeHabitWithDaily, data.habitSelected)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when selected habit is not in list, then fallback to first habit`() = runTest {
        // --- GIVEN ---
        val fakeHabit1 = Habit(id = 10L, name = "Leer")
        val fakeHabits = listOf(fakeHabit1)
        val fakeHabitWithDaily = HabitWithDailyHabit(habit = fakeHabit1, dailyHabits = mutableListOf())

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(fakeHabits)
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(99L) // Non-existent ID
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(10L) } returns flowOf(fakeHabitWithDaily)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.statisticsState.test {

            val successItem = awaitItem() as StatisticsState.Success
            val data = successItem.state as StatisticsSuccessState.Habits

            assertEquals(10L, data.habitSelected.habit.id)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when use case throws exception, then emit Error state`() = runTest {
        // --- GIVEN ---
        val errorMessage = "Error al conectar con la base de datos"
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flow { throw Exception(errorMessage) }
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.statisticsState.test {

            val errorItem = awaitItem() as StatisticsState.Error
            assertEquals(errorMessage, errorItem.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `verify initial default values for secondary states`() {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- THEN ---
        val currentYear = LocalDate.now().year
        assertEquals(currentYear, viewModel.yearGraphicsSelected.value)
        assertEquals(currentYear, viewModel.yearHourlyGraphicsSelected.value)
    }

    @Test
    fun `when getDaySelected emits null, then dayOfWeek defaults to MONDAY`() = runTest {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)

        every { getHabitSelectedUseCase.getDaySelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.dayOfWeek.test {
            assertEquals(DayOfWeek.MONDAY, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when getDaySelected emits a valid day string, then dayOfWeek parses it correctly`() = runTest {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)

        every { getHabitSelectedUseCase.getDaySelected() } returns flowOf("FRIDAY")

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.dayOfWeek.test {
            assertEquals(DayOfWeek.FRIDAY, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when getDaySelected emits new day over time, then dayOfWeek updates accordingly`() = runTest {
        // --- GIVEN ---
        val daySelectedFlow = MutableStateFlow("TUESDAY")

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)
        every { getHabitSelectedUseCase.getDaySelected() } returns daySelectedFlow

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.dayOfWeek.test {
            assertEquals(DayOfWeek.TUESDAY, awaitItem())

            daySelectedFlow.value = "SUNDAY"

            assertEquals(DayOfWeek.SUNDAY, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when statisticsState is Loading or Error, then calendarUIState emits empty list`() = runTest {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flow { throw Exception("Error") }
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)
        every { getHabitSelectedUseCase.getDaySelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.calendarUIState.test {
            val calendarState = awaitItem()
            assertTrue(calendarState.dates.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when statisticsState is Success Empty, then calendarUIState emits empty list`() = runTest {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)
        every { getHabitSelectedUseCase.getDaySelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.calendarUIState.test {
            val calendarState = awaitItem()
            assertTrue(calendarState.dates.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when statisticsState has habit data, then calendarUIState populates dates and maps daily habits correctly`() = runTest {
        // --- GIVEN ---
        val testDate = LocalDate.of(2026, 5, 15)
        val fakeHabit = Habit(id = 1L, name = "Estudiar Kotlin", goal = 2.toBigDecimal())
        val fakeDailyHabit = HabitDay(date = testDate, goalDone = 2.toBigDecimal())

        val fakeHabitSelected = HabitWithDailyHabit(
            habit = fakeHabit,
            dailyHabits = mutableListOf(fakeDailyHabit)
        )

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(listOf(fakeHabit))
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(1L)
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(1L) } returns flowOf(fakeHabitSelected)
        every { getHabitSelectedUseCase.getDaySelected() } returns flowOf("MONDAY")

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.calendarUIState.test {
            val initialState = awaitItem()
            if (initialState.dates.isEmpty()) {
                val state = awaitItem()

                assertTrue(state.dates.isNotEmpty())
                val targetDay = state.dates.find { it.data?.day?.date == testDate }

                assertEquals(fakeHabit, targetDay?.data?.habit)
                assertEquals(fakeDailyHabit.goalDone, targetDay?.data?.day?.goalDone)
            } else {
                assertTrue(initialState.dates.isNotEmpty())
            }

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when statisticsState is Loading, then boxUIState emits empty list`() = runTest {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.boxUIState.test {
            val state = awaitItem()
            assertTrue(state.isEmpty())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when statisticsState has habits, then boxUIState correctly calculates Done, Uncompleted and NotDone states`() = runTest {
        // --- GIVEN ---
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val twoDaysAgo = today.minusDays(2)
        val goal = 2.toBigDecimal()

        val fakeHabit = Habit(id = 1L, name = "Gimnasio", goal = goal)

        val fakeDailyHabits = mutableListOf(
            HabitDay(date = today, goalDone = 2.toBigDecimal()), // Done
            HabitDay(date = yesterday, goalDone = 1.toBigDecimal()) // Uncompleted
        )

        val fakeHabitSelected = HabitWithDailyHabit(
            habit = fakeHabit,
            dailyHabits = fakeDailyHabits
        )

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(listOf(fakeHabit))
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(1L)
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(1L) } returns flowOf(fakeHabitSelected)
        every { getHabitSelectedUseCase.getDaySelected() } returns flowOf("MONDAY")

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.boxUIState.test {
            var state = awaitItem()

            if (state.isEmpty()) {
                state = awaitItem()
            }

            assertEquals(NUMBER_OF_DAYS, state.size)

            val todayBox = state.find { it.day == today }
            val yesterdayBox = state.find { it.day == yesterday }
            val twoDaysAgoBox = state.find { it.day == twoDaysAgo }

            assertEquals(DayBoxState.Done, todayBox?.dayState)
            assertEquals(DayBoxState.Uncompleted, yesterdayBox?.dayState)
            assertEquals(DayBoxState.NotDone, twoDaysAgoBox?.dayState)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when statisticsState is Loading, then graphicsState emits default GraphicsState`() = runTest {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.graphicsState.test {
            val state = awaitItem()
            assertNull(state.model)
            assertEquals(0, state.color)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when habit is Daily, then graphicsState calculates completed days per month correctly`() = runTest {
        // --- GIVEN ---
        val selectedYear = 2026
        val expectedColor = 0xFF123456.toInt()
        val goal = 2.toBigDecimal()

        val fakeHabit = Habit(
            id = 1L,
            name = "Leer",
            goal = goal,
            color = expectedColor,
            typeHabit = TypeHabit.Daily
        )

        // Simulamos días para Mayo (mes 5) de 2026:
        // 1. Día 10 de mayo: Completado (goalDone == 2 >= goal) -> CUENTA
        // 2. Día 11 de mayo: No completado (goalDone == 1 < goal) -> NO CUENTA
        // 3. Día 15 de mayo: Pasado de meta (goalDone == 3 >= goal) -> CUENTA
        // 4. Día 1 de mayo de 2025 (año distinto): -> NO CUENTA
        val fakeDailyHabits = mutableListOf(
            HabitDay(date = LocalDate.of(2026, 5, 10), goalDone = 2.toBigDecimal()),
            HabitDay(date = LocalDate.of(2026, 5, 11), goalDone = 1.toBigDecimal()),
            HabitDay(date = LocalDate.of(2026, 5, 15), goalDone = 3.toBigDecimal()),
            HabitDay(date = LocalDate.of(2025, 5, 1), goalDone = 2.toBigDecimal())
        )

        val fakeHabitSelected = HabitWithDailyHabit(
            habit = fakeHabit,
            dailyHabits = fakeDailyHabits
        )

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(listOf(fakeHabit))
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(1L)
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(1L) } returns flowOf(fakeHabitSelected)
        every { getHabitSelectedUseCase.getDaySelected() } returns flowOf("MONDAY")

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.graphicsState.test {
            var state = awaitItem()

            if (state.model == null) {
                state = awaitItem()
            }

            assertEquals(expectedColor, state.color)
            assertNotNull(state.model)

            val lineLayerModel = state.model?.models?.firstOrNull() as? LineCartesianLayerModel
            assertNotNull(lineLayerModel)

            val seriesValues = lineLayerModel?.series?.firstOrNull()
            assertNotNull(seriesValues)

            assertEquals(2.0, seriesValues?.getOrNull(4)?.y ?: 0.0, 0.01)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when habit is not Daily or Recurring, then goalDone greater than zero counts as completed`() = runTest {
        // --- GIVEN ---
        val fakeHabit = Habit(
            id = 1L,
            name = "Meditar",
            goal = 10.toBigDecimal(),
            typeHabit = TypeHabit.Weekly(numberDays = 1, weeklyGoal = false)
        )

        val fakeDailyHabits = mutableListOf(
            HabitDay(date = LocalDate.of(2026, 1, 15), goalDone = 1.toBigDecimal())
        )

        val fakeHabitSelected = HabitWithDailyHabit(
            habit = fakeHabit,
            dailyHabits = fakeDailyHabits
        )

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(listOf(fakeHabit))
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(1L)
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(1L) } returns flowOf(fakeHabitSelected)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.graphicsState.test {
            var state = awaitItem()
            if (state.model == null) {
                state = awaitItem()
            }

            assertNotNull(state.model)

            val lineLayerModel = state.model?.models?.firstOrNull() as? LineCartesianLayerModel
            val januaryValue = lineLayerModel?.series?.firstOrNull()?.getOrNull(0)?.y ?: 0.0

            assertEquals(1.0, januaryValue, 0.01)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when statisticsState is Loading, then hourlyGraphicsState emits default GraphicsState`() = runTest {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.hourlyGraphicsState.test {
            val state = awaitItem()
            assertNull(state.model)
            assertEquals(0, state.color)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when habits are present, then hourlyGraphicsState aggregates completed habits by hour of completion`() = runTest {
        // --- GIVEN ---
        val selectedYear = 2026
        val expectedColor = 0xFF654321.toInt()
        val goal = 1.toBigDecimal()

        val fakeHabit = Habit(
            id = 1L,
            name = "Estudiar Kotlin",
            goal = goal,
            color = expectedColor,
            typeHabit = TypeHabit.Daily
        )

        // Simulamos hábitos completados a distintas horas en 2026:
        // 1. 08:30 -> Hora 8 (CUENTA)
        // 2. 08:45 -> Hora 8 (CUENTA - Total hora 8 = 2)
        // 3. 21:15 -> Hora 21 (CUENTA - Total hora 21 = 1)
        // 4. 14:00 pero en 2025 -> Año distinto (NO CUENTA)
        val fakeDailyHabits = mutableListOf(
            HabitDay(
                date = LocalDate.of(2026, 3, 10),
                hourFinishDate = LocalTime.of(8, 30),
                goalDone = 1.toBigDecimal()
            ),
            HabitDay(
                date = LocalDate.of(2026, 3, 11),
                hourFinishDate = LocalTime.of(8, 45),
                goalDone = 1.toBigDecimal()
            ),
            HabitDay(
                date = LocalDate.of(2026, 4, 15),
                hourFinishDate = LocalTime.of(21, 15),
                goalDone = 1.toBigDecimal()
            ),
            HabitDay(
                date = LocalDate.of(2025, 3, 10),
                hourFinishDate = LocalTime.of(14, 0),
                goalDone = 1.toBigDecimal()
            )
        )

        val fakeHabitSelected = HabitWithDailyHabit(
            habit = fakeHabit,
            dailyHabits = fakeDailyHabits
        )

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(listOf(fakeHabit))
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(1L)
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(1L) } returns flowOf(fakeHabitSelected)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.hourlyGraphicsState.test {
            var state = awaitItem()

            if (state.model == null) {
                state = awaitItem()
            }

            assertEquals(expectedColor, state.color)
            assertNotNull(state.model)

            val lineLayerModel = state.model?.models?.firstOrNull() as? LineCartesianLayerModel
            val seriesValues = lineLayerModel?.series?.firstOrNull()
            assertNotNull(seriesValues)

            assertEquals(2.0, seriesValues?.getOrNull(8)?.y ?: 0.0, 0.01)
            assertEquals(1.0, seriesValues?.getOrNull(21)?.y ?: 0.0, 0.01)
            assertEquals(0.0, seriesValues?.getOrNull(14)?.y ?: 0.0, 0.01)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when statisticsState is Loading or null, then goalsDoneState emits default GoalsDoneState`() = runTest {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.goalsDoneState.test {
            val state = awaitItem()
            assertEquals(0, state.numberOfDaysCompleted)
            assertEquals(0, state.numberOfBestStreak)
            assertEquals(0, state.numberOfCurrentStreak)
            assertEquals(0, state.consistencyPercentage)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when habit has daily consecutive completions, then calculates total, streaks and consistency correctly`() = runTest {
        // --- GIVEN ---
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val twoDaysAgo = today.minusDays(2)
        val goal = 1.toBigDecimal()

        val fakeHabit = Habit(
            id = 1L,
            name = "Beber agua",
            goal = goal,
            typeHabit = TypeHabit.Daily
        )

        val fakeDailyHabits = mutableListOf(
            HabitDay(date = twoDaysAgo, goalDone = goal),
            HabitDay(date = yesterday, goalDone = goal),
            HabitDay(date = today, goalDone = goal)
        )

        val fakeHabitSelected = HabitWithDailyHabit(
            habit = fakeHabit,
            dailyHabits = fakeDailyHabits
        )

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(listOf(fakeHabit))
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(1L)
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(1L) } returns flowOf(fakeHabitSelected)
        every { getHabitSelectedUseCase.getDaySelected() } returns flowOf("MONDAY")

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.goalsDoneState.test {
            var state = awaitItem()

            if (state.numberOfDaysCompleted == 0) {
                state = awaitItem()
            }

            assertEquals(3, state.numberOfDaysCompleted)

            assertEquals(3, state.numberOfCurrentStreak)
            assertEquals(twoDaysAgo, state.currentStreakDates.first)
            assertEquals(today, state.currentStreakDates.second)

            assertEquals(3, state.numberOfBestStreak)
            assertEquals(twoDaysAgo, state.bestStreakDates.first)
            assertEquals(today, state.bestStreakDates.second)

            assertEquals(100, state.consistencyPercentage)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when current streak is broken, then numberOfCurrentStreak is zero but bestStreak keeps record`() = runTest {
        // --- GIVEN ---
        val today = LocalDate.now()

        // Hace 10, 9 y 8 días hubo racha de 3 días. Luego no se ha hecho más.
        val tenDaysAgo = today.minusDays(10)
        val nineDaysAgo = today.minusDays(9)
        val eightDaysAgo = today.minusDays(8)
        val goal = 1.toBigDecimal()

        val fakeHabit = Habit(
            id = 1L,
            name = "Hacer ejercicio",
            goal = goal,
            typeHabit = TypeHabit.Daily
        )

        val fakeDailyHabits = mutableListOf(
            HabitDay(date = tenDaysAgo, goalDone = goal),
            HabitDay(date = nineDaysAgo, goalDone = goal),
            HabitDay(date = eightDaysAgo, goalDone = goal)
        )

        val fakeHabitSelected = HabitWithDailyHabit(
            habit = fakeHabit,
            dailyHabits = fakeDailyHabits
        )

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(listOf(fakeHabit))
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(1L)
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(1L) } returns flowOf(fakeHabitSelected)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.goalsDoneState.test {
            var state = awaitItem()
            if (state.numberOfDaysCompleted == 0) {
                state = awaitItem()
            }

            assertEquals(3, state.numberOfDaysCompleted)

            // La racha actual está rota/muerta
            assertEquals(0, state.numberOfCurrentStreak)

            // La mejor racha histórica sigue siendo de 3 días (hace 10 -> hace 8)
            assertEquals(3, state.numberOfBestStreak)
            assertEquals(tenDaysAgo, state.bestStreakDates.first)
            assertEquals(eightDaysAgo, state.bestStreakDates.second)

            // Consistencia: 3 días completados entre 11 días transcurridos ≈ 27%
            assertTrue(state.consistencyPercentage < 100)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when statisticsState is Loading or Empty, then pieChartState emits empty list`() = runTest {
        // --- GIVEN ---
        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(emptyList())
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(null)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.pieChartState.test {
            val state = awaitItem()
            assertTrue(state.isEmpty())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when habit has daily data, then pieChartState calculates percentages and filters zero percent slices`() = runTest {
        // --- GIVEN ---
        val expectedColor = 0xFF4CAF50.toInt()
        val goal = 1.toBigDecimal()

        val fakeHabit = Habit(
            id = 1L,
            name = "Leer 20 mins",
            goal = goal,
            color = expectedColor,
            typeHabit = TypeHabit.Daily
        )

        // Simulamos hábitos con días en distintos estados
        val fakeDailyHabits = mutableListOf(
            HabitDay(date = LocalDate.now().minusDays(2), goalDone = goal), // Completed
            HabitDay(date = LocalDate.now().minusDays(1), goalDone = BigDecimal.ZERO) // Not Done
        )

        val fakeHabitSelected = HabitWithDailyHabit(
            habit = fakeHabit,
            dailyHabits = fakeDailyHabits
        )

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(listOf(fakeHabit))
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(1L)
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(1L) } returns flowOf(fakeHabitSelected)
        every { getHabitSelectedUseCase.getDaySelected() } returns flowOf("MONDAY")

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.pieChartState.test {
            var state = awaitItem()

            // Consumimos la lista vacía inicial si aplica
            if (state.isEmpty()) {
                state = awaitItem()
            }

            // Verificamos que la lista no esté vacía
            assertTrue(state.isNotEmpty())

            // Todos los elementos de la lista deben conservar el color del hábito
            assertTrue(state.all { it.habitColor == expectedColor })

            // Verificamos que no haya ningún segmento con porcentaje 0
            assertTrue(state.all { it.percentage > 0 })

            // Comprobamos que esté ordenada según PieChartState
            val isSorted = state.zipWithNext().all { (a, b) -> a.state <= b.state }
            assertTrue(isSorted)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when all periods are completed, then pieChartState contains only COMPLETED segment`() = runTest {
        // --- GIVEN ---
        val goal = 1.toBigDecimal()
        val fakeHabit = Habit(id = 1L, name = "Agua", goal = goal)

        // Días 100% completados
        val fakeDailyHabits = mutableListOf(
            HabitDay(date = LocalDate.now(), goalDone = goal)
        )

        val fakeHabitSelected = HabitWithDailyHabit(
            habit = fakeHabit,
            dailyHabits = fakeDailyHabits
        )

        every { getHabitsStatisticsUseCase.getAllHabits() } returns flowOf(listOf(fakeHabit))
        every { getHabitSelectedUseCase.getHabitSelected() } returns flowOf(1L)
        every { getHabitsStatisticsUseCase.getHabitWithDailyHabit(1L) } returns flowOf(fakeHabitSelected)

        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN & THEN ---
        viewModel.pieChartState.test {
            var state = awaitItem()
            if (state.isEmpty()) {
                state = awaitItem()
            }

            assertEquals(1, state.size)
            assertEquals(PieChartState.COMPLETED, state.first().state)
            assertEquals(100, state.first().percentage)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onMonthButtonClicked updates yearMonth state correctly`() = runTest {
        // --- GIVEN ---
        val targetMonth = YearMonth.of(2026, 11)
        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)

        // --- WHEN ---
        viewModel.onMonthButtonClicked(targetMonth)

        // --- THEN ---
        assertEquals(targetMonth, viewModel.yearMonth.value)
    }

    @Test
    fun `onYearSelected increments and decrements yearGraphicsSelected correctly`() = runTest {
        // --- GIVEN ---
        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)
        val initialYear = viewModel.yearGraphicsSelected.value

        // --- WHEN (Siguiente año) ---
        viewModel.onYearSelected(isNext = true)

        // --- THEN ---
        assertEquals(initialYear + 1, viewModel.yearGraphicsSelected.value)

        // --- WHEN (Año anterior x2) ---
        viewModel.onYearSelected(isNext = false)
        viewModel.onYearSelected(isNext = false)

        // --- THEN ---
        assertEquals(initialYear - 1, viewModel.yearGraphicsSelected.value)
    }

    @Test
    fun `onHourYearSelected increments and decrements yearHourlyGraphicsSelected correctly`() = runTest {
        // --- GIVEN ---
        viewModel = StatisticsViewModel(getHabitsStatisticsUseCase, getHabitSelectedUseCase)
        val initialYear = viewModel.yearHourlyGraphicsSelected.value

        // --- WHEN (Siguiente año) ---
        viewModel.onHourYearSelected(isNext = true)

        // --- THEN ---
        assertEquals(initialYear + 1, viewModel.yearHourlyGraphicsSelected.value)

        // --- WHEN (Año anterior) ---
        viewModel.onHourYearSelected(isNext = false)

        // --- THEN ---
        assertEquals(initialYear, viewModel.yearHourlyGraphicsSelected.value)
    }

}