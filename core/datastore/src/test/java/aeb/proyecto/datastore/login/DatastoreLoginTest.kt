package aeb.proyecto.datastore.login

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.model.UserSession
import aeb.proyecto.datastore.repository.DatastoreRepository
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DatastoreLoginTest {

    @get:Rule
    val mainDispatchersRule = MainDispatcherRuleUnit()

    private var datastoreManager: DataStoreManager = mockk()
    private var datastoreRepository: DatastoreRepository = mockk()

    @Test
    fun `given a value when called getUserSession function on datastore then returns the value`() = runTest {
            //Given
            val expectedUserSession = UserSession(email = "email1")
            coEvery { datastoreManager.getUserSession() } returns expectedUserSession

            //When
            val result = datastoreManager.getUserSession()

            //Then
            assertEquals(expectedUserSession, result)
    }

    @Test
    fun `given a value when called getUserSession function on repository then returns the value`() = runTest {
        //Given
        val expectedUserSession = UserSession(email = "email1")
        coEvery { datastoreRepository.getUserSession() } returns expectedUserSession

        //When
        val result = datastoreRepository.getUserSession()

        //Then
        assertEquals(expectedUserSession, result)
    }

    @Test
    fun `given nothing when called clearSession function on datastore then calls the function`() = runTest {
        //Given
        coEvery { datastoreManager.clearSession() } returns Unit

        //When
        datastoreManager.clearSession()

        //Then
        coVerify { datastoreManager.clearSession() }
    }

    @Test
    fun `given nothing when called clearSession function on repository then calls the function`() = runTest {
        //Given
        coEvery { datastoreRepository.clearSession() } returns Unit

        //When
        datastoreRepository.clearSession()

        //Then
        coVerify { datastoreRepository.clearSession() }
    }

    @Test
    fun `given a value when called saveUserSession function on repository then calls the function`() = runTest {
        //Given
        val saveUserSession = UserSession(email = "email1")
        coEvery { datastoreRepository.saveUserSession(saveUserSession) } returns Unit

        //When
        val result = datastoreRepository.saveUserSession(saveUserSession)

        //Then
        coVerify { datastoreRepository.saveUserSession(saveUserSession) }
    }

    @Test
    fun `given a value when called saveUserSession function on datastore then calls the function`() = runTest {
        //Given
        val saveUserSession = UserSession(email = "email1")
        coEvery { datastoreManager.saveUserSession(saveUserSession) } returns Unit

        //When
        val result = datastoreManager.saveUserSession(saveUserSession)

        //Then
        coVerify { datastoreManager.saveUserSession(saveUserSession) }
    }

    @Test
    fun `given a value when called userSession function on datastore then calls the function and return the value`() = runTest {
        //Given
        val saveUserSession = UserSession(email = "email1")
        coEvery { datastoreManager.userSession } returns flowOf(saveUserSession)

        //When
        datastoreManager.userSession.test {

            //Then
            assertEquals(saveUserSession, awaitItem())
            awaitComplete()
        }
    }
}