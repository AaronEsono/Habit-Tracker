package aeb.proyecto.domain.timer

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.domain.MainDispatchersRule
import aeb.proyecto.domain.usecase.timer.TimeEntriesUseCase
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.TimeEntry
import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import aeb.proyecto.room.repository.TimerEntryRepo
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TimeEntriesUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatchersRule()

    private lateinit var mockRepository: TimerEntryRepo
    private lateinit var mockDatastore: DatastoreInterface
    private lateinit var useCase: TimeEntriesUseCase

    @Before
    fun setUp() {
        mockRepository = mockk(relaxed = true)
        mockDatastore = mockk(relaxed = true)
        useCase = TimeEntriesUseCase(mockRepository, mockDatastore)
    }

    @Test
    fun `given type timer 0 with habit, when setDataFromTimeEntry is called, then set type timer and habit id only`() = runTest {
        // --- GIVEN ---
        val habit = Habit(id = 15L)
        val timeEntry = TimeEntry(typeTimer = 0, time = null, restTime = null, intervals = null)
        val timeEntryWithHabit = TimeEntryWithHabit(timeEntry = timeEntry, habit = habit)

        var lambdaCalled = false

        // --- WHEN ---
        useCase.setDataFromTimeEntry(timeEntryWithHabit) {
            lambdaCalled = true
        }

        // --- THEN ---
        coVerify(exactly = 1) { mockDatastore.setTypeTimerSelected(0) }
        coVerify(exactly = 1) { mockDatastore.setIdHabitLinkedTimer(15L) }

        coVerify(exactly = 0) { mockDatastore.setHourWheelTimer(any()) }
        assertEquals(false, lambdaCalled)
    }

    @Test
    fun `given type timer 1 with time, when setDataFromTimeEntry is called, then set type timer, habit id, and time`() = runTest {
        // --- GIVEN ---
        val habit = Habit(id = 22L)
        val timeEntry = TimeEntry(typeTimer = 1, time = 3725L, restTime = null, intervals = null)
        val timeEntryWithHabit = TimeEntryWithHabit(timeEntry = timeEntry, habit = habit)

        var capturedTriple: Triple<Int, Int, Int>? = null

        // --- WHEN ---
        useCase.setDataFromTimeEntry(timeEntryWithHabit) { triple ->
            capturedTriple = triple
        }

        // --- THEN ---
        coVerify(exactly = 1) { mockDatastore.setTypeTimerSelected(1) }
        coVerify(exactly = 1) { mockDatastore.setIdHabitLinkedTimer(22L) }

        coVerify(exactly = 1) { mockDatastore.setHourWheelTimer(1) }
        coVerify(exactly = 1) { mockDatastore.setMinuteWheelTimer(2) }
        coVerify(exactly = 1) { mockDatastore.setSecondWheelTimer(5) }

        assertTrue(capturedTriple != null)
        assertEquals(Triple(1, 2, 5), capturedTriple)
    }


    @Test
    fun `given type timer 2 with intervals and rest, when setDataFromTimeEntry is called, then set type timer, habit id, intervals, and rest`() = runTest {
        // --- GIVEN ---
        val habit = Habit(id = 8L)
        val timeEntry = TimeEntry(typeTimer = 2, time = 180L, restTime = 90L, intervals = 5)
        val timeEntryWithHabit = TimeEntryWithHabit(timeEntry = timeEntry, habit = habit)

        var lambdaCalled = false

        // --- WHEN ---
        useCase.setDataFromTimeEntry(timeEntryWithHabit) {
            lambdaCalled = true
        }

        // --- THEN ---
        coVerify(exactly = 1) { mockDatastore.setTypeTimerSelected(2) }
        coVerify(exactly = 1) { mockDatastore.setIdHabitLinkedTimer(8L) }

        coVerify(exactly = 1) { mockDatastore.setHourWheelTimer(0) }
        coVerify(exactly = 1) { mockDatastore.setMinuteWheelTimer(3) }
        coVerify(exactly = 1) { mockDatastore.setSecondWheelTimer(0) }

        coVerify(exactly = 1) { mockDatastore.setNumberSetsTimer(5) }
        coVerify(exactly = 1) { mockDatastore.setRestIntervalHourTimer(0) }
        coVerify(exactly = 1) { mockDatastore.setRestIntervalMinuteTimer(1) }
        coVerify(exactly = 1) { mockDatastore.setRestIntervalSecondTimer(30) }

        assertEquals(false, lambdaCalled)
    }


    @Test
    fun `given no habit in time entry, when setDataFromTimeEntry is called, then fallback to -1 habit id`() = runTest {
        // --- GIVEN ---
        val timeEntry = TimeEntry(typeTimer = 0, time = null, restTime = null, intervals = null)
        val timeEntryWithHabit = TimeEntryWithHabit(timeEntry = timeEntry, habit = null)

        // --- WHEN ---
        useCase.setDataFromTimeEntry(timeEntryWithHabit) {}

        // --- THEN ---
        coVerify(exactly = 1) { mockDatastore.setTypeTimerSelected(0) }
        coVerify(exactly = 1) { mockDatastore.setIdHabitLinkedTimer(-1L) }
    }
}