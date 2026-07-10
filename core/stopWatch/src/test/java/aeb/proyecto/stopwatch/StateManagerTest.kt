package aeb.proyecto.stopwatch

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class StateManagerTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    val manager = StopWatchStateManager()

    @Test
    fun `given manager when update elapsed time with negative value then forces absolute floor limit of zero`() {
        // --- WHEN ---
        manager.updateElapsedTime(-5000L)

        // --- THEN ---
        assertEquals(0L, manager.elapsedTime.value)
    }

    @Test
    fun `given manager when update elapsed time is called then timer string emits formatted Hms`() = runTest {
        manager.timerString.test {
            assertEquals("00:00:00", awaitItem())

            // --- WHEN ---
            manager.updateElapsedTime(65000L)

            // --- THEN ---
            assertEquals("00:01:05", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given manager when set state is called then current state changes correctly`() {
        // --- GIVEN ---
        val manager = StopWatchStateManager()

        // --- WHEN ---
        manager.setState(StopwatchState.InProgress)

        // --- THEN ---
        assertEquals(StopwatchState.InProgress, manager.currentState.value)
    }

    @Test
    fun `given manager when set notification title is called then notification title emits new string`() = runTest {
        // --- GIVEN ---
        val manager = StopWatchStateManager()

        manager.notificationTitle.test {
            assertEquals("Stopwatch", awaitItem()) // Estado inicial

            // --- WHEN ---
            manager.setNotificationTitle("Intervalo 1: ¡Dale caña!")

            // --- THEN ---
            assertEquals("Intervalo 1: ¡Dale caña!", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given manager when set timer type is called then type timer emits new modality`() = runTest {
        // --- GIVEN ---
        val manager = StopWatchStateManager()
        val targetType = TypeTimer.TIMER(time = 300000L)

        manager.typeTimer.test {
            assertEquals(TypeTimer.STOPWATCH, awaitItem())

            // --- WHEN ---
            manager.setTimerType(targetType)

            // --- THEN ---
            assertEquals(targetType, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given manager when set running timer is called then running timer emits boolean flag`() = runTest {
        // --- GIVEN ---
        val manager = StopWatchStateManager()

        manager.runningTimer.test {
            assertFalse(awaitItem())

            // --- WHEN ---
            manager.setRunningTimer(true)

            // --- THEN ---
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given manager when set habit linked is called then habit linked emits database relation`() = runTest {
        // --- GIVEN ---
        val manager = StopWatchStateManager()
        val fakeHabitWithDay = HabitWithDay(
            habit = Habit(id = 1L, name = "Ir al gimnasio"),
            day = HabitDay(date = LocalDate.now())
        )

        manager.habitLinked.test {
            assertNull(awaitItem())

            // --- WHEN ---
            manager.setHabitLinked(fakeHabitWithDay)

            // --- THEN ---
            assertEquals(fakeHabitWithDay, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given manager when initialized then primitive time trackers start at zero`() {
        // --- GIVEN & WHEN ---
        val manager = StopWatchStateManager()

        // --- THEN ---
        assertEquals(0L, manager.startTime)
        assertEquals(0L, manager.timeElapsedBeforePause)
    }

    @Test
    fun `given manager when elapsed time reflects multiple hours then timer string formats correctly`() = runTest {
        // --- GIVEN ---
        val manager = StopWatchStateManager()
        // 3 horas, 25 minutos y 45 segundos en milisegundos
        // (3 * 3600 + 25 * 60 + 45) * 1000 = 12345000ms
        val structuralTargetTime = 12345000L

        manager.timerString.test {
            assertEquals("00:00:00", awaitItem())

            // --- WHEN ---
            manager.updateElapsedTime(structuralTargetTime)

            // --- THEN ---
            assertEquals("03:25:45", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

}