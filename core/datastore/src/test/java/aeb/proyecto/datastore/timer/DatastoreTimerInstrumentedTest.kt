package aeb.proyecto.datastore.timer

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.repository.DatastoreRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.io.File

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

}