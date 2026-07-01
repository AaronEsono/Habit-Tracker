package aeb.proyecto.datastore.habit

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.repository.DatastoreRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class DatastoreHabitInstrumentedTest {

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
    fun `given nothing when called getTypeSelected function then returns null`() = runTest {
        //Given
        val expected = null

        //When
        val result = datastoreRepository.getTypeSelected()

        //Then
        assertEquals(expected, result)
    }

    @Test
    fun `given a value when called setTypeSelected function then saves the data and return the value`() = runTest {
        //Given
        val typeSelected = "Weekly"

        //When
        datastoreRepository.setTypeSelected(typeSelected)
        val result = datastoreRepository.getTypeSelected()

        //Then
        assertEquals(typeSelected, result)
    }

}