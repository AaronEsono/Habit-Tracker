package aeb.proyecto.datastore.stopwatch

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

class DatastoreStopwatchInstrumentedTest {

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
    fun `given nothing when called getTimePassedTimer then return null`() = runTest {
        //Given
        val expected = null

        //When
        val result = datastoreRepository.getTimePassedTimer()

        //Then
        assertEquals(expected, result)
    }

    @Test
    fun `given a value when called getTimePassedTimer then returns the value`() = runTest {
        //Given
        val timePassed = 6000L

        //When
        datastoreRepository.setTimePassedTimer(timePassed)
        val result = datastoreRepository.getTimePassedTimer()

        //Then
        assertEquals(timePassed, result)
    }

    @Test
    fun `given nothing when called timerLinkedAndFinished then return false`() = runTest {
        //Given NOTHING

        //When
        datastoreRepository.timerLinkedAndFinished.test {

            //Then
            assertEquals(false, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `given a value when called timerLinkedAndFinished then return the value`() = runTest {
        //Given
        val linkedAndFinished = true

        //When
        datastoreRepository.setIsLinkedHabitAndFinished(linkedAndFinished)

        //Then
        datastoreRepository.timerLinkedAndFinished.test {

            assertEquals(linkedAndFinished, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `given two values when called timerLinkedAndFinished then return the first and then the second`() = runTest {
        //Given
        val firstLinkedAndFinished = true
        val secondLinkedAndFinished = false

        //When
        datastoreRepository.setIsLinkedHabitAndFinished(firstLinkedAndFinished)

        //Then
        datastoreRepository.timerLinkedAndFinished.test {

            assertEquals(firstLinkedAndFinished, awaitItem())

            datastoreRepository.setIsLinkedHabitAndFinished(secondLinkedAndFinished)
            assertEquals(secondLinkedAndFinished, awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }
}