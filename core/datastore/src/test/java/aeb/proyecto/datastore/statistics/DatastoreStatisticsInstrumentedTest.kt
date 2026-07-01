package aeb.proyecto.datastore.statistics

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.model.AppSettings
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
import org.junit.experimental.theories.suppliers.TestedOn
import java.io.File

class DatastoreStatisticsInstrumentedTest {

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
    fun `given a value when called setHabitSelected then return the default value`() = runTest{
        //Given
        val habitSelected = 3L

        //When
        datastoreRepository.setHabitSelected(habitSelected)

        //Then
        datastoreRepository.habitSelected.test {
            assertEquals(habitSelected, awaitItem())
        }
    }

    @Test
    fun `given nothing when called habitSelected function then returns the dafeult value`() = runTest {
        //Given nothing

        //When
        datastoreRepository.habitSelected.test {
            //Then
            assertEquals(null, awaitItem())
        }

    }

    @Test
    fun `given two habitSelected when called habitSelected function then returns the value expected and then the other one`() = runTest{
        //Given
        val firstHabitSelected = 1L
        val secondHabitSelected = 2L
        datastoreRepository.setHabitSelected(firstHabitSelected)

        //When
        datastoreRepository.habitSelected.test {
            //Then
            assertEquals(firstHabitSelected, awaitItem())

            datastoreRepository.setHabitSelected(secondHabitSelected)
            assertEquals(secondHabitSelected, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

}