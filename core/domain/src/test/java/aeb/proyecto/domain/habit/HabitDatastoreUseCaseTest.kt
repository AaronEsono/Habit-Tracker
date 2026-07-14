package aeb.proyecto.domain.habit

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.domain.MainDispatchersRule
import aeb.proyecto.domain.usecase.habit.HabitDatastoreUseCase
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class HabitDatastoreUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatchersRule()

    private lateinit var mockDatastore: DatastoreInterface
    private lateinit var useCase: HabitDatastoreUseCase

    @Before
    fun setUp() {
        mockDatastore = mockk(relaxed = true)
        useCase = HabitDatastoreUseCase(mockDatastore)
    }

    @Test
    fun `given exact seconds, when bigDecimalToTriple is called then returns correct hours, minutes, and seconds`() {
        // --- GIVEN ---
        val totalSeconds = BigDecimal("3665")

        // --- WHEN ---
        val result = useCase.bigDecimalToTriple(totalSeconds)

        // --- THEN ---
        assertEquals(1L, result.first)
        assertEquals(1L, result.second)
        assertEquals(5L, result.third)
    }

    @Test
    fun `given decimal seconds with fractional drift, when bigDecimalToTriple is called then truncates downward and returns correct triple`() {
        // --- GIVEN ---
        val driftingSeconds = BigDecimal("75.999")

        // --- WHEN ---
        val result = useCase.bigDecimalToTriple(driftingSeconds)

        // --- THEN ---
        assertEquals(0L, result.first)
        assertEquals(1L, result.second)
        assertEquals(15L, result.third)
    }

    @Test
    fun `given zero seconds, when bigDecimalToTriple is called then returns all zeros`() {
        // --- GIVEN ---
        val zeroSeconds = BigDecimal.ZERO

        // --- WHEN ---
        val result = useCase.bigDecimalToTriple(zeroSeconds)

        // --- THEN ---
        assertEquals(Triple(0L, 0L, 0L), result)
    }

    @Test
    fun `given habit parameters and time left, when setTimerFromHabit is called then deconstructs time and saves all values in Datastore`() = runTest {
        // --- GIVEN ---
        val habitId = 15L
        val date = LocalDate.of(2026, 7, 14)
        val timeLeft = BigDecimal("5430")

        // --- WHEN ---
        useCase.setTimerFromHabit(habitId, date, timeLeft)

        // --- THEN ---
        coVerify(exactly = 1) { mockDatastore.setIdHabitLinkedTimer(habitId) }
        coVerify(exactly = 1) { mockDatastore.setDateHabitLinkedTimer("2026-07-14") }

        coVerify(exactly = 1) { mockDatastore.setHourWheelTimer(1) }
        coVerify(exactly = 1) { mockDatastore.setMinuteWheelTimer(30) }
        coVerify(exactly = 1) { mockDatastore.setSecondWheelTimer(30) }

        coVerify(exactly = 1) { mockDatastore.setTypeTimerSelected(1) }
    }
}