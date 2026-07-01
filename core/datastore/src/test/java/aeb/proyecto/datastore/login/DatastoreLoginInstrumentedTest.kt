package aeb.proyecto.datastore.login

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.model.UserSession
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

class DatastoreLoginInstrumentedTest {

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
    fun `given nothing when called getUserSession then returns the default data`() = runTest {
        //Given
        val defaultData = UserSession()

        //When
        val result = datastoreRepository.getUserSession()

        //Then
        assertEquals(defaultData, result)
    }


    @Test
    fun `given a UserSession when called saveUserSession then returns and saves the correct data`() = runTest {
        //Given
        val userSession = UserSession(email = "email1", password = "password1")

        //When
        datastoreRepository.saveUserSession(userSession)

        //Then
        val result = datastoreRepository.getUserSession()
        assertEquals(userSession, result)
    }

    @Test
    fun `given a UserSession when called saveUserSession then clears the data when called clearSession`() = runTest {
        //Given
        val userSession = UserSession(email = "email1", password = "password1")

        //When
        datastoreRepository.saveUserSession(userSession)

        //Then
       datastoreRepository.clearSession()

        val result = datastoreRepository.getUserSession()
        assertEquals(UserSession(), result)
    }

    @Test
    fun `given two UserSession when called userSession twice then given the first one is returned and then the second one is returned`() = runTest {
        //Given
        val firstUserSession = UserSession(email = "email1", password = "password1")
        val secondUserSession = UserSession(email = "email2", password = "password2")

        //When
        datastoreRepository.saveUserSession(firstUserSession)

        //Then
        datastoreManager.userSession.test {
            val firstResult = awaitItem()
            assertEquals(firstUserSession, firstResult)

            datastoreRepository.saveUserSession(secondUserSession)
            val secondResult = awaitItem()
            assertEquals(secondUserSession, secondResult)
        }
    }

}