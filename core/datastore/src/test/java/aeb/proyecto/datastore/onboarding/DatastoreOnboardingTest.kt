package aeb.proyecto.datastore.onboarding

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.repository.DatastoreRepository
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DatastoreOnboardingTest {

    @get:Rule
    val mainDispatchersRule = MainDispatcherRuleUnit()

    private var datastoreManager: DataStoreManager = mockk()
    private var datastoreRepository: DatastoreRepository = mockk()

    @Test
    fun `given a value when called onboardScreen function on datastore then return the same value`() = runTest {
        //Given
        val onboardSelected = true
        coEvery { datastoreManager.showOnboardScreen } returns flowOf(onboardSelected)

        //When
        datastoreManager.showOnboardScreen.test {
            //Then
            assertEquals(onboardSelected, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a value when called setOnboardScreen function on repository then return the same value`() = runTest {
        //Given
        val onboardSelected = true
        coEvery { datastoreRepository.setShowOnboardScreen(onboardSelected) } returns Unit

        //When
        datastoreRepository.setShowOnboardScreen(onboardSelected)

        //Then
        coEvery { datastoreRepository.setShowOnboardScreen(onboardSelected) }
    }

}