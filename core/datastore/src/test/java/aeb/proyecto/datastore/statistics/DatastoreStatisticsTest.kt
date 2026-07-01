package aeb.proyecto.datastore.statistics

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.repository.DatastoreRepository
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DatastoreStatisticsTest {

    @get:Rule
    val mainDispatchersRule = MainDispatcherRuleUnit()

    private var datastoreManager: DataStoreManager = mockk()
    private var datastoreRepository: DatastoreRepository = mockk()

    @Test
    fun `given a random value when called setHabitSelected function on repository then calls the function`() =
        runTest {
            // GIVEN
            val id = 0L
            coEvery { datastoreRepository.setHabitSelected(id) } returns Unit

            //When
            datastoreRepository.setHabitSelected(id)

            //Then
            coVerify { datastoreRepository.setHabitSelected(id) }
        }

    @Test
    fun `given a random value when called setHabitSelected function on datastore then calls the function`() =
        runTest {
            // GIVEN
            val id = 0L
            coEvery { datastoreManager.setHabitSelected(id) } returns Unit

            //When
            datastoreManager.setHabitSelected(id)

            //Then
            coVerify { datastoreManager.setHabitSelected(id) }
        }

    @Test
    fun `given a random value when called habitSelected function on repository then calls the function and give the expected value`() =
        runTest {
            //Given
            val id = 2L
            coEvery { datastoreRepository.habitSelected } returns flowOf(id)

            //When
            datastoreRepository.habitSelected.test {
                //Then
                assert(awaitItem() == id)
                awaitComplete()
            }
        }

    @Test
    fun `given a random value when called habitSelected function on datastore then calls the function and give the expected value`() =
        runTest {
            //Given
            val id = 2L
            coEvery { datastoreManager.habitSelected } returns flowOf(id)

            //When
            datastoreManager.habitSelected.test {
                //Then
                assert(awaitItem() == id)
                awaitComplete()
            }
        }

}