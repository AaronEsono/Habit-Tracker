package aeb.proyecto.datastore.timer

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.repository.DatastoreRepository
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class DatastoreTimerTest {

    @get:Rule
    val mainDispatchersRule = MainDispatcherRuleUnit()

    private var datastoreManager: DataStoreManager = mockk()
    private var datastoreRepository: DatastoreRepository = mockk()

    @Test
    fun `given a value when called idHabitLinkedSelected function on datastore then return the same value`() = runTest {
        //Given
        val idHabitSelected = 1L
        coEvery { datastoreManager.idHabitLinkedSelected } returns flowOf(idHabitSelected)

        //When
        datastoreManager.idHabitLinkedSelected.test {
            //Then
            assertEquals(idHabitSelected, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called idHabitLinkedSelected function on repository then return the same value`() = runTest {
        //Given
        val idHabitSelected = 1L
        coEvery { datastoreRepository.idHabitLinkedTimer } returns flowOf(idHabitSelected)

        //When
        datastoreRepository.idHabitLinkedTimer.test {
            //Then
            assertEquals(idHabitSelected, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called dateHabitLinkedSelected function on datastore then return the same value`() = runTest {
        //Given
        val dateHabit = LocalDate.now().toString()
        coEvery { datastoreManager.dateHabitLinkedSelected } returns flowOf(dateHabit)

        //When
        datastoreManager.dateHabitLinkedSelected.test {
            //Then
            assertEquals(dateHabit, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called dateHabitLinkedSelected function on repository then return the same value`() = runTest {
        //Given
        val dateHabit = LocalDate.now().toString()
        coEvery { datastoreRepository.dateHabitLinkedTimer } returns flowOf(dateHabit)

        //When
        datastoreRepository.dateHabitLinkedTimer.test {
            //Then
            assertEquals(dateHabit, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called typeTimerSelected function on datastore then return the same value`() = runTest {
        //Given
        val typeSelected = 1
        coEvery { datastoreManager.typeTimerSelected } returns flowOf(typeSelected)

        //When
        datastoreManager.typeTimerSelected.test {
            //Then
            assertEquals(typeSelected, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called typeTimerSelected function on repository then return the same value`() = runTest {
        //Given
        val typeSelected = 1
        coEvery { datastoreRepository.typeTimerSelected } returns flowOf(typeSelected)

        //When
        datastoreRepository.typeTimerSelected.test {
            //Then
            assertEquals(typeSelected, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called hourWheelTimer function on datastore then return the same value`() = runTest {
        //Given
        val hourWheel = 14
        coEvery { datastoreManager.hourWheelTimer } returns flowOf(hourWheel)

        //When
        datastoreManager.hourWheelTimer.test {
            //Then
            assertEquals(hourWheel, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called minuteWheelTimer function on datastore then return the same value`() = runTest {
        //Given
        val minuteWheel = 34
        coEvery { datastoreManager.minuteWheelTimer } returns flowOf(minuteWheel)

        //When
        datastoreManager.minuteWheelTimer.test {
            //Then
            assertEquals(minuteWheel, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called secondWheelTimer function on datastore then return the same value`() = runTest {
        //Given
        val secondWheel = 54
        coEvery { datastoreManager.secondWheelTimer } returns flowOf(secondWheel)

        //When
        datastoreManager.secondWheelTimer.test {
            //Then
            assertEquals(secondWheel, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called wheelHourSelected function on repository then return the same value`() = runTest {
        //Given
        val hourWheel = "14:23:43"
        coEvery { datastoreRepository.wheelHourSelected } returns flowOf(hourWheel)

        //When
        datastoreRepository.wheelHourSelected.test {
            //Then
            assertEquals(hourWheel, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called restIntervalHourTimer function on datastore then return the same value`() = runTest {
        //Given
        val hourWheel = 14
        coEvery { datastoreManager.restIntervalHourTimer } returns flowOf(hourWheel)

        //When
        datastoreManager.restIntervalHourTimer.test {
            //Then
            assertEquals(hourWheel, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called restIntervalMinuteTimer function on datastore then return the same value`() = runTest {
        //Given
        val minuteWheel = 34
        coEvery { datastoreManager.restIntervalMinuteTimer } returns flowOf(minuteWheel)

        //When
        datastoreManager.restIntervalMinuteTimer.test {
            //Then
            assertEquals(minuteWheel, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called restIntervalSecondTimer function on datastore then return the same value`() = runTest {
        //Given
        val secondWheel = 54
        coEvery { datastoreManager.restIntervalSecondTimer } returns flowOf(secondWheel)

        //When
        datastoreManager.restIntervalSecondTimer.test {
            //Then
            assertEquals(secondWheel, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called restHourSelected function on repository then return the same value`() = runTest {
        //Given
        val hourWheel = "14:00:00"
        coEvery { datastoreRepository.restHourSelected } returns flowOf(hourWheel)

        //When
        datastoreRepository.restHourSelected.test {
            //Then
            assertEquals(hourWheel, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called numberSetsTimerSelected function on datastore then return the same value`() = runTest {
        //Given
        val numberOfSets = 3
        coEvery { datastoreManager.numberSetsTimerSelected } returns flowOf(numberOfSets)

        //When
        datastoreManager.numberSetsTimerSelected.test {
            //Then
            assertEquals(numberOfSets, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called numberSetsTimerSelected function on repository then return the same value`() = runTest {
        //Given
        val numberOfSets = 3
        coEvery { datastoreRepository.numberSetsTimerSelected } returns flowOf(numberOfSets)

        //When
        datastoreRepository.numberSetsTimerSelected.test {
            //Then
            assertEquals(numberOfSets, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called getIdHabitLinkedSelected function on datastore then return the same value`() = runTest {
        //Given
        val idHabitSelected = 1L
        coEvery { datastoreManager.getIdHabitLinkedSelected() } returns idHabitSelected

        //When
        val result = datastoreManager.getIdHabitLinkedSelected()

        //Then
        assertEquals(idHabitSelected, result)
        coVerify { datastoreManager.getIdHabitLinkedSelected() }
    }

    @Test
    fun `given a value when called getDateHabitLinkedSelected function on datastore then return the same value`() = runTest {
        //Given
        val dateHabitSelected = LocalDate.now().toString()
        coEvery { datastoreManager.getDateHabitLinkedSelected() } returns dateHabitSelected

        //When
        val result = datastoreManager.getDateHabitLinkedSelected()

        //Then
        assertEquals(dateHabitSelected, result)
        coVerify { datastoreManager.getDateHabitLinkedSelected() }
    }

    @Test
    fun `given a value when called getIdHabitLinkedSelected function on repository then return the same value`() = runTest {
        //Given
        val idHabitSelected = 1L
        coEvery { datastoreRepository.getIdHabitLinkedTimer() } returns idHabitSelected

        //When
        val result = datastoreRepository.getIdHabitLinkedTimer()

        //Then
        assertEquals(idHabitSelected, result)
        coVerify { datastoreRepository.getIdHabitLinkedTimer() }
    }

    @Test
    fun `given a value when called getDateHabitLinkedSelected function on repository then return the same value`() = runTest {
        //Given
        val dateHabitSelected = LocalDate.now().toString()
        coEvery { datastoreManager.getDateHabitLinkedSelected() } returns dateHabitSelected

        //When
        val result = datastoreManager.getDateHabitLinkedSelected()

        //Then
        assertEquals(dateHabitSelected, result)
        coVerify { datastoreManager.getDateHabitLinkedSelected() }
    }

    @Test
    fun `given a value when called setRestIntervalHourTimer function on datastore then calls the function`() = runTest {
        //Given
        val hour = 12
        coEvery { datastoreManager.setRestIntervalHourTimer(hour) } returns Unit

        datastoreManager.setRestIntervalHourTimer(hour)

        //Then
        coVerify { datastoreManager.setRestIntervalHourTimer(hour) }
    }

    @Test
    fun `given a value when called setRestIntervalHourTimer function on repository then calls the function`() = runTest {
        //Given
        val hour = 12
        coEvery { datastoreRepository.setRestIntervalHourTimer(hour) } returns Unit

        //When
        datastoreRepository.setRestIntervalHourTimer(hour)

        //Then
        coVerify { datastoreRepository.setRestIntervalHourTimer(hour) }
    }

    @Test
    fun `given a value when called setRestIntervalMinuteTimer function on datastore then calls the function`() = runTest {
        //Given
        val minutes = 12
        coEvery { datastoreManager.setRestIntervalMinuteTimer(minutes) } returns Unit

        //When
        datastoreManager.setRestIntervalMinuteTimer(minutes)

        //Then
        coVerify { datastoreManager.setRestIntervalMinuteTimer(minutes) }
    }

    @Test
    fun `given a value when called setRestIntervalMinuteTimer function on repository then calls the function`() = runTest {
        //Given
        val minutes = 12
        coEvery { datastoreRepository.setRestIntervalMinuteTimer(minutes) } returns Unit

        //When
        datastoreRepository.setRestIntervalMinuteTimer(minutes)

        //Then
        coVerify { datastoreRepository.setRestIntervalMinuteTimer(minutes) }
    }

    @Test
    fun `given a value when called setRestIntervalSecondTimer function on datastore then calls the function`() = runTest {
        //Given
        val seconds = 12
        coEvery { datastoreManager.setRestIntervalSecondTimer(seconds) } returns Unit

        //When
        datastoreManager.setRestIntervalSecondTimer(seconds)

        //Then
        coVerify { datastoreManager.setRestIntervalSecondTimer(seconds) }
    }

    @Test
    fun `given a value when called setRestIntervalSecondTimer function on repository then calls the function`() = runTest {
        //Given
        val seconds = 12
        coEvery { datastoreRepository.setRestIntervalSecondTimer(seconds) } returns Unit

        //When
        datastoreRepository.setRestIntervalSecondTimer(seconds)

        //Then
        coVerify { datastoreRepository.setRestIntervalSecondTimer(seconds) }
    }

    @Test
    fun `given a value when called setHourWheelTimer function on datastore then calls the function`() = runTest {
        //Given
        val hour = 12
        coEvery { datastoreManager.setHourWheelTimer(hour) } returns Unit

        datastoreManager.setHourWheelTimer(hour)

        //Then
        coVerify { datastoreManager.setHourWheelTimer(hour) }
    }

    @Test
    fun `given a value when called setHourWheelTimer function on repository then calls the function`() = runTest {
        //Given
        val hour = 12
        coEvery { datastoreRepository.setHourWheelTimer(hour) } returns Unit

        //When
        datastoreRepository.setHourWheelTimer(hour)

        //Then
        coVerify { datastoreRepository.setHourWheelTimer(hour) }
    }

    @Test
    fun `given a value when called setMinuteWheelTimer function on datastore then calls the function`() = runTest {
        //Given
        val minutes = 12
        coEvery { datastoreManager.setMinuteWheelTimer(minutes) } returns Unit

        //When
        datastoreManager.setMinuteWheelTimer(minutes)

        //Then
        coVerify { datastoreManager.setMinuteWheelTimer(minutes) }
    }

    @Test
    fun `given a value when called setMinuteWheelTimer function on repository then calls the function`() = runTest {
        //Given
        val minutes = 12
        coEvery { datastoreRepository.setMinuteWheelTimer(minutes) } returns Unit

        //When
        datastoreRepository.setMinuteWheelTimer(minutes)

        //Then
        coVerify { datastoreRepository.setMinuteWheelTimer(minutes) }
    }

    @Test
    fun `given a value when called setSecondWheelTimer function on datastore then calls the function`() = runTest {
        //Given
        val seconds = 12
        coEvery { datastoreManager.setSecondWheelTimer(seconds) } returns Unit

        //When
        datastoreManager.setSecondWheelTimer(seconds)

        //Then
        coVerify { datastoreManager.setSecondWheelTimer(seconds) }
    }

    @Test
    fun `given a value when called setSecondWheelTimer function on repository then calls the function`() = runTest {
        //Given
        val seconds = 12
        coEvery { datastoreRepository.setSecondWheelTimer(seconds) } returns Unit

        //When
        datastoreRepository.setSecondWheelTimer(seconds)

        //Then
        coVerify { datastoreRepository.setSecondWheelTimer(seconds) }
    }

    @Test
    fun `given a value when called setTypeTimerSelected function on datastore then calls the function`() = runTest {
        //Given
        val typeTime = 1
        coEvery { datastoreManager.setTypeTimerSelected(typeTime) } returns Unit

        //When
        datastoreManager.setTypeTimerSelected(typeTime)

        //Then
        coVerify { datastoreManager.setTypeTimerSelected(typeTime) }
    }

    @Test
    fun `given a value when called setTypeTimerSelected function on repository then calls the function`() = runTest {
        //Given
        val typeTime = 1
        coEvery { datastoreRepository.setTypeTimerSelected(typeTime) } returns Unit

        //When
        datastoreRepository.setTypeTimerSelected(typeTime)

        //Then
        coVerify { datastoreRepository.setTypeTimerSelected(typeTime) }
    }

    @Test
    fun `given a value when called setNumberSetsTimerSelected function on datastore then calls the function`() = runTest {
        //Given
        val numberSets = 1
        coEvery { datastoreManager.setNumberSetsTimerSelected(numberSets) } returns Unit

        //When
        datastoreManager.setNumberSetsTimerSelected(numberSets)

        //Then
        coVerify { datastoreManager.setNumberSetsTimerSelected(numberSets) }
    }

    @Test
    fun `given a value when called setNumberSetsTimerSelected function on repository then calls the function`() = runTest {
        //Given
        val numberSets = 1
        coEvery { datastoreRepository.setNumberSetsTimer(numberSets) } returns Unit

        //When
        datastoreRepository.setNumberSetsTimer(numberSets)

        //Then
        coVerify { datastoreRepository.setNumberSetsTimer(numberSets) }
    }

    @Test
    fun `given a value when called setIdHabitLinkedSelected function on datastore then calls the function`() = runTest {
        //Given
        val idHabit = 1L
        coEvery { datastoreManager.setIdHabitLinkedSelected(idHabit) } returns Unit

        //When
        datastoreManager.setIdHabitLinkedSelected(idHabit)

        //Then
        coVerify { datastoreManager.setIdHabitLinkedSelected(idHabit) }
    }

    @Test
    fun `given a value when called setIdHabitLinkedSelected function on repository then calls the function`() = runTest {
        //Given
        val idHabit = 1L
        coEvery { datastoreRepository.setIdHabitLinkedTimer(idHabit) } returns Unit

        //When
        datastoreRepository.setIdHabitLinkedTimer(idHabit)

        //Then
        coVerify { datastoreRepository.setIdHabitLinkedTimer(idHabit) }
    }

    @Test
    fun `given a value when called setDateHabitLinkedSelected function on datastore then calls the function`() = runTest {
        //Given
        val dateSelected = LocalDate.now().toString()
        coEvery { datastoreManager.setDateHabitLinkedSelected(dateSelected) } returns Unit

        //When
        datastoreManager.setDateHabitLinkedSelected(dateSelected)

        //Then
        coVerify { datastoreManager.setDateHabitLinkedSelected(dateSelected) }
    }

    @Test
    fun `given a value when called setDateHabitLinkedSelected function on repository then calls the function`() = runTest {
        //Given
        val dateSelected = LocalDate.now().toString()
        coEvery { datastoreRepository.setDateHabitLinkedTimer(dateSelected) } returns Unit

        //When
        datastoreRepository.setDateHabitLinkedTimer(dateSelected)

        //Then
        coVerify { datastoreRepository.setDateHabitLinkedTimer(dateSelected) }
    }

}