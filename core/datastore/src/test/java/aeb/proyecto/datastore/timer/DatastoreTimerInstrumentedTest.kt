package aeb.proyecto.datastore.timer

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.repository.DatastoreRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.time.LocalDate

class DatastoreTimerInstrumentedTest {

    @get:Rule
    val mainDispatchersInstrumentedRule = MainDispatcherRuleUnit()

    private lateinit var testFile: File
    private lateinit var datastore: DataStore<Preferences>
    private lateinit var datastoreManager: DataStoreManager
    private lateinit var datastoreRepository: DatastoreRepository

    @Before
    fun setUp(){
        testFile = File.createTempFile("test_prefs", ".preferences_pb")

        datastore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(mainDispatchersInstrumentedRule.testDispatcher + Job()),
            produceFile = { testFile }
        )

        datastoreManager = DataStoreManager(datastore)
        datastoreRepository = DatastoreRepository(datastoreManager)
    }

    @After
    fun tearDown(){
        if (::testFile.isInitialized) {
            testFile.delete()
        }
    }

    @Test
    fun `given none when called idHabitLinkedTimer on repository then return null`() = runTest {
        //Given
        val expected = null

        //When
        datastoreRepository.idHabitLinkedTimer.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a value when called idHabitLinkedTimer on repository then return the value`() = runTest {
        //Given
        val idHabitSelected = 2L

        //When
        datastoreRepository.setIdHabitLinkedTimer(idHabitSelected)

        //Then
        datastoreRepository.idHabitLinkedTimer.test {

            assertEquals(idHabitSelected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given two values when called idHabitLinkedTimer on repository then return the first one then the second one`() = runTest {
        //Given
        val firstIdHabitSelected = 2L
        val secondIdHabitSelected = 5L

        //When
        datastoreRepository.setIdHabitLinkedTimer(firstIdHabitSelected)

        //Then
        datastoreRepository.idHabitLinkedTimer.test {

            assertEquals(firstIdHabitSelected, awaitItem())

            datastoreRepository.setIdHabitLinkedTimer(secondIdHabitSelected)
            assertEquals(secondIdHabitSelected, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called dateHabitLinkedSelected on repository then return null`() = runTest {
        //Given
        val expected = null

        //When
        datastoreRepository.dateHabitLinkedTimer.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a value when called dateHabitLinkedSelected on repository then return the value`() = runTest {
        //Given
        val dateHabitLinked = LocalDate.now().toString()

        //When
        datastoreRepository.setDateHabitLinkedTimer(dateHabitLinked)

        //Then
        datastoreRepository.dateHabitLinkedTimer.test {

            assertEquals(dateHabitLinked, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given two values when called dateHabitLinkedSelected on repository then return the first one then the second one`() = runTest {
        //Given
        val firstDateHabitLinked = LocalDate.now().toString()
        val secondDateHabitLinked = LocalDate.now().plusDays(1).toString()

        //When
        datastoreRepository.setDateHabitLinkedTimer(firstDateHabitLinked)

        //Then
        datastoreRepository.dateHabitLinkedTimer.test {

            assertEquals(firstDateHabitLinked, awaitItem())

            datastoreRepository.setDateHabitLinkedTimer(secondDateHabitLinked)
            assertEquals(secondDateHabitLinked, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called typeTimerSelected on repository then return null`() = runTest {
        //Given
        val expected = null

        //When
        datastoreRepository.typeTimerSelected.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a value when called typeTimerSelected on repository then return the value`() = runTest {
        //Given
        val typeTimerSelected = 2

        //When
        datastoreRepository.setTypeTimerSelected(typeTimerSelected)

        //Then
        datastoreRepository.typeTimerSelected.test {

            assertEquals(typeTimerSelected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given two values when called typeTimerSelected on repository then return the first one then the second one`() = runTest {
        //Given
        val firstTypeTimerSelected = 1
        val secondTypeTimerSelected = 2

        //When
        datastoreRepository.setTypeTimerSelected(firstTypeTimerSelected)

        //Then
        datastoreRepository.typeTimerSelected.test {

            assertEquals(firstTypeTimerSelected, awaitItem())

            datastoreRepository.setTypeTimerSelected(secondTypeTimerSelected)
            assertEquals(secondTypeTimerSelected, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called hourWheelTimer on datastore then return null`() = runTest {
        //Given
        val expected = null

        //When
        datastoreManager.hourWheelTimer.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a value when called hourWheelTimer on datastore then return the value`() = runTest {
        //Given
        val hourWheel = 2

        //When
        datastoreRepository.setHourWheelTimer(hourWheel)

        //Then
        datastoreManager.hourWheelTimer.test {

            assertEquals(hourWheel, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given two values when called hourWheelTimer on datastore then return the first one then the second one`() = runTest {
        //Given
        val firstHourWheel = 1
        val secondHourWheel = 2

        //When
        datastoreRepository.setHourWheelTimer(firstHourWheel)

        //Then
        datastoreManager.hourWheelTimer.test {

            assertEquals(firstHourWheel, awaitItem())

            datastoreRepository.setHourWheelTimer(secondHourWheel)
            assertEquals(secondHourWheel, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called minuteWheelTimer on datastore then return null`() = runTest {
        //Given
        val expected = null

        //When
        datastoreManager.minuteWheelTimer.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a value when called minuteWheelTimer on datastore then return the value`() = runTest {
        //Given
        val minuteWheel = 2

        //When
        datastoreRepository.setMinuteWheelTimer(minuteWheel)

        //Then
        datastoreManager.minuteWheelTimer.test {

            assertEquals(minuteWheel, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given two values when called minuteWheelTimer on datastore then return the first one then the second one`() = runTest {
        //Given
        val firstMinuteWheel = 1
        val secondMinuteWheel = 2

        //When
        datastoreRepository.setMinuteWheelTimer(firstMinuteWheel)

        //Then
        datastoreManager.minuteWheelTimer.test {

            assertEquals(firstMinuteWheel, awaitItem())

            datastoreRepository.setMinuteWheelTimer(secondMinuteWheel)
            assertEquals(secondMinuteWheel, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called secondWheelTimer on datastore then return null`() = runTest {
        //Given
        val expected = null

        //When
        datastoreManager.secondWheelTimer.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a value when called secondWheelTimer on datastore then return the value`() = runTest {
        //Given
        val secondWheel = 2

        //When
        datastoreRepository.setSecondWheelTimer(secondWheel)

        //Then
        datastoreManager.secondWheelTimer.test {

            assertEquals(secondWheel, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given two values when called secondWheelTimer on datastore then return the first one then the second one`() = runTest {
        //Given
        val firstSecondWheel = 1
        val secondSecondWheel = 2

        //When
        datastoreRepository.setSecondWheelTimer(firstSecondWheel)

        //Then
        datastoreManager.secondWheelTimer.test {

            assertEquals(firstSecondWheel, awaitItem())

            datastoreRepository.setSecondWheelTimer(secondSecondWheel)
            assertEquals(secondSecondWheel, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called wheelHourSelected on repository then return the expected`() = runTest {
        //Given
        val expected = "null:null:null"

        //When
        datastoreRepository.wheelHourSelected.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given an hour,minute and second when called wheelHourSelected on repository then return the expected`() = runTest {
        //Given
        val hour = 12
        val minute = 32
        val second = 45
        val expected = "12:32:45"

        //When
        datastoreRepository.setHourWheelTimer(hour)
        datastoreRepository.setMinuteWheelTimer(minute)
        datastoreRepository.setSecondWheelTimer(second)

        //Then
        datastoreRepository.wheelHourSelected.test {

            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given an hour,minute and second when called wheelHourSelected and change on repository then return the expected`() = runTest {
        //Given
        val hour = 12
        val firstMinutes = 32
        val second = 45

        val secondHour = 10
        val secondMinutes = 56
        val secondSecond = 20

        val firstExpected = "12:32:45"
        val secondExpected = "10:56:20"

        //When
        datastoreRepository.setHourWheelTimer(hour)
        datastoreRepository.setMinuteWheelTimer(firstMinutes)
        datastoreRepository.setSecondWheelTimer(second)

        //Then
        datastoreRepository.wheelHourSelected.test {

            assertEquals(firstExpected, awaitItem())

            datastoreRepository.setHourWheelTimer(secondHour)
            datastoreRepository.setMinuteWheelTimer(secondMinutes)
            datastoreRepository.setSecondWheelTimer(secondSecond)

            skipItems(2)

            assertEquals(secondExpected, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called restIntervalHourTimer on datastore then return null`() = runTest {
        //Given
        val expected = null

        //When
        datastoreManager.restIntervalHourTimer.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a value when called restIntervalHourTimer on datastore then return the value`() = runTest {
        //Given
        val restHourWheel = 2

        //When
        datastoreRepository.setRestIntervalHourTimer(restHourWheel)

        //Then
        datastoreManager.restIntervalHourTimer.test {

            assertEquals(restHourWheel, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given two values when called restIntervalHourTimer on datastore then return the first one then the second one`() = runTest {
        //Given
        val firstRestHourWheel = 1
        val secondRestHourWheel = 2

        //When
        datastoreRepository.setRestIntervalHourTimer(firstRestHourWheel)

        //Then
        datastoreManager.restIntervalHourTimer.test {

            assertEquals(firstRestHourWheel, awaitItem())

            datastoreRepository.setRestIntervalHourTimer(secondRestHourWheel)
            assertEquals(secondRestHourWheel, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called restIntervalMinuteTimer on datastore then return null`() = runTest {
        //Given
        val expected = null

        //When
        datastoreManager.restIntervalMinuteTimer.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a value when called restIntervalMinuteTimer on datastore then return the value`() = runTest {
        //Given
        val restMinuteWheel = 2

        //When
        datastoreRepository.setRestIntervalMinuteTimer(restMinuteWheel)

        //Then
        datastoreManager.restIntervalMinuteTimer.test {

            assertEquals(restMinuteWheel, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given two values when called restIntervalMinuteTimer on datastore then return the first one then the second one`() = runTest {
        //Given
        val firstRestMinuteWheel = 1
        val secondRestMinuteWheel = 2

        //When
        datastoreRepository.setRestIntervalMinuteTimer(firstRestMinuteWheel)

        //Then
        datastoreManager.restIntervalMinuteTimer.test {

            assertEquals(firstRestMinuteWheel, awaitItem())

            datastoreRepository.setRestIntervalMinuteTimer(secondRestMinuteWheel)
            assertEquals(secondRestMinuteWheel, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called restIntervalSecondTimer on datastore then return null`() = runTest {
        //Given
        val expected = null

        //When
        datastoreManager.restIntervalSecondTimer.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a value when called restIntervalSecondTimer on datastore then return the value`() = runTest {
        //Given
        val restSecondWheel = 2

        //When
        datastoreRepository.setRestIntervalSecondTimer(restSecondWheel)

        //Then
        datastoreManager.restIntervalSecondTimer.test {

            assertEquals(restSecondWheel, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given two values when called restIntervalSecondTimer on datastore then return the first one then the second one`() = runTest {
        //Given
        val firstRestSecondWheel = 1
        val secondRestSecondWheel = 2

        //When
        datastoreRepository.setRestIntervalSecondTimer(firstRestSecondWheel)

        //Then
        datastoreManager.restIntervalSecondTimer.test {

            assertEquals(firstRestSecondWheel, awaitItem())

            datastoreRepository.setRestIntervalSecondTimer(secondRestSecondWheel)
            assertEquals(secondRestSecondWheel, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    //-------------------------------------------------------------------------------------------

    @Test
    fun `given none when called restHourSelected on repository then return the expected`() = runTest {
        //Given
        val expected = "null:null:null"

        //When
        datastoreRepository.restHourSelected.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given an hour,minute and second when called restHourSelected on repository then return the expected`() = runTest {
        //Given
        val restHour = 12
        val restMinute = 32
        val restSecond = 45
        val expected = "12:32:45"

        //When
        datastoreRepository.setRestIntervalHourTimer(restHour)
        datastoreRepository.setRestIntervalMinuteTimer(restMinute)
        datastoreRepository.setRestIntervalSecondTimer(restSecond)

        //Then
        datastoreRepository.restHourSelected.test {

            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given an hour,minute and second when called restHourSelected and change on repository then return the expected`() = runTest {
        //Given
        val restFirstHour = 12
        val restFirstMinutes = 32
        val restFirstSecond = 45

        val restSecondHour = 10
        val restSecondMinutes = 56
        val restSecondSeconds = 20

        val firstExpected = "12:32:45"
        val secondExpected = "10:56:20"

        //When
        datastoreRepository.setRestIntervalHourTimer(restFirstHour)
        datastoreRepository.setRestIntervalMinuteTimer(restFirstMinutes)
        datastoreRepository.setRestIntervalSecondTimer(restFirstSecond)

        //Then
        datastoreRepository.restHourSelected.test {

            assertEquals(firstExpected, awaitItem())

            datastoreRepository.setRestIntervalHourTimer(restSecondHour)
            datastoreRepository.setRestIntervalMinuteTimer(restSecondMinutes)
            datastoreRepository.setRestIntervalSecondTimer(restSecondSeconds)

            skipItems(2)

            assertEquals(secondExpected, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called numberSetsTimerSelected on repository then return null`() = runTest {
        //Given
        val expected = null

        //When
        datastoreRepository.numberSetsTimerSelected.test {

            //Then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a value when called numberSetsTimerSelected on repository then return the value`() = runTest {
        //Given
        val numberSets = 2

        //When
        datastoreRepository.setNumberSetsTimer(numberSets)

        //Then
        datastoreRepository.numberSetsTimerSelected.test {

            assertEquals(numberSets, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given two values when called numberSetsTimerSelected on repository then return the first one then the second one`() = runTest {
        //Given
        val firstNumberSets = 2
        val secondNumberSets = 5

        //When
        datastoreRepository.setNumberSetsTimer(firstNumberSets)

        //Then
        datastoreRepository.numberSetsTimerSelected.test {

            assertEquals(firstNumberSets, awaitItem())

            datastoreRepository.setNumberSetsTimer(secondNumberSets)
            assertEquals(secondNumberSets, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given none when called getIdHabitLinkedTimer on repository then return null`() = runTest {
        //Given
        val expected = null

        //When
        val result = datastoreRepository.getIdHabitLinkedTimer()

        //Then
        assertEquals(expected, result)
    }

    @Test
    fun `given a value when called getIdHabitLinkedTimer on repository then return the value`() = runTest {
        //Given
        val idHabitSelected = 2L

        //When
        datastoreRepository.setIdHabitLinkedTimer(idHabitSelected)
        val result = datastoreRepository.getIdHabitLinkedTimer()

        //Then
        assertEquals(idHabitSelected, result)
    }

    @Test
    fun `given none when called getDateHabitLinkedTimer on repository then return null`() = runTest {
        //Given
        val expected = null

        //When
        val result = datastoreRepository.getDateHabitLinkedTimer()

        //Then
        assertEquals(expected, result)
    }

    @Test
    fun `given a value when called getDateHabitLinkedTimer on repository then return the value`() = runTest {
        //Given
        val dateTimerSelected = LocalDate.now().toString()

        //When
        datastoreRepository.setDateHabitLinkedTimer(dateTimerSelected)
        val result = datastoreRepository.getDateHabitLinkedTimer()

        //Then
        assertEquals(dateTimerSelected, result)
    }

}