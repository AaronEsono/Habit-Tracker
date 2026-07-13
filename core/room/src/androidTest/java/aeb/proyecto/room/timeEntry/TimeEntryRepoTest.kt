package aeb.proyecto.room.timeEntry

import aeb.proyecto.room.dao.HabitWithNotificationDao
import aeb.proyecto.room.dao.TimerEntryDao
import aeb.proyecto.room.database.DatabaseHabitTracker
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.TimeEntry
import aeb.proyecto.room.repository.TimerEntryRepo
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class TimeEntryRepoTest {

    private lateinit var database: DatabaseHabitTracker
    private lateinit var timeEntryDao: TimerEntryDao
    private lateinit var habitWithNotificationDao: HabitWithNotificationDao
    private lateinit var timeEntryRepo: TimerEntryRepo

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            DatabaseHabitTracker::class.java
        ).allowMainThreadQueries().build()

        timeEntryDao = database.timerEntryDao()
        habitWithNotificationDao = database.habitWithNotificationDao()
        timeEntryRepo = TimerEntryRepo(timeEntryDao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenAGlobalStopwatchWithoutHabit_whenFindTimeEntryIsCalled_thenDoNothingAndDoNotPersistAnything() = runTest {
        // --- GIVEN ---
        val globalStopwatch = TimeEntry(id = 0L, typeTimer = 0, idHabit = null)

        // --- WHEN ---
        timeEntryRepo.findTimeEntry(globalStopwatch)

        // --- THEN ---
        val result = timeEntryDao.findStopWatch(idHabit = null)
        assertNull(result)
    }

    @Test
    fun givenAnExistingTimer_whenFindTimeEntryIsCalled_thenUpdateItsLastTimeUsedTimestamp() = runTest {
        // --- GIVEN ---
        val habitId = 44L
        val oldTimestamp = LocalDateTime.now().minusDays(3)

        val existingTimer = TimeEntry(
            id = 1L,
            typeTimer = 1,
            time = 60L,
            idHabit = habitId,
            lastTimeUsed = oldTimestamp
        )

        habitWithNotificationDao.insertHabit(Habit(id = habitId))
        timeEntryDao.insertTimerEntry(existingTimer)

        val inputEntry = TimeEntry(typeTimer = 1, time = 60L, idHabit = habitId)

        // --- WHEN ---
        timeEntryRepo.findTimeEntry(inputEntry)

        // --- THEN ---
        val updatedResult = timeEntryDao.findTimer(time = 60L, idHabit = habitId)

        assertNotNull(updatedResult)
        assertEquals(1L, updatedResult?.id)
        assertTrue(updatedResult!!.lastTimeUsed.isAfter(oldTimestamp))
    }

    @Test
    fun givenANonExistingIntervalTimer_whenFindTimeEntryIsCalled_thenInsertItAsANewBaselineRecord() = runTest {
        // --- GIVEN ---
        val newIntervalInput = TimeEntry(
            typeTimer = 2,
            time = 30L,
            restTime = 15L,
            intervals = 10,
            idHabit = 12L
        )
        habitWithNotificationDao.insertHabit(Habit(id = 12L))

        val checkBefore = timeEntryDao.findInterval(time = 30L, rest = 15L, interval = 10, idHabit = 12L)
        assertNull(checkBefore)

        // --- WHEN ---
        timeEntryRepo.findTimeEntry(newIntervalInput)

        // --- THEN ---
        val checkAfter = timeEntryDao.findInterval(time = 30L, rest = 15L, interval = 10, idHabit = 12L)

        assertNotNull(checkAfter)
        assertEquals(2, checkAfter?.typeTimer)
        assertEquals(30L, checkAfter?.time)
        assertEquals(15L, checkAfter?.restTime)
        assertEquals(12L, checkAfter?.idHabit)
        assertTrue(checkAfter!!.id > 0L)
    }

    @Test
    fun givenFavouritesAndRecentEntriesInDatabase_whenGetHistoryEntriesIsCalled_thenReturnCombinedListInSingleEmission() = runTest {
        // --- GIVEN ---
        val baseTime = LocalDateTime.now()

        val favEntry = TimeEntry(
            id = 1L,
            typeTimer = 1,
            favourite = true,
            lastTimeUsed = baseTime
        )

        val recentEntry = TimeEntry(
            id = 2L,
            typeTimer = 0,
            favourite = false,
            lastTimeUsed = baseTime.plusMinutes(5)
        )

        timeEntryDao.insertTimerEntry(favEntry)
        timeEntryDao.insertTimerEntry(recentEntry)

        // --- WHEN & THEN ---
        timeEntryRepo.getHistoryEntries().test {
            val currentEmission = awaitItem()

            assertNotNull(currentEmission)
            assertEquals(2, currentEmission.size)

            // Verificamos que ambos registros estén presentes en el resultado final
            assertEquals(1L, currentEmission[0].timeEntry.id) // El favorito (primero en la suma)
            assertEquals(2L, currentEmission[1].timeEntry.id) // El reciente (segundo en la suma)

            cancelAndIgnoreRemainingEvents()
        }
    }
}