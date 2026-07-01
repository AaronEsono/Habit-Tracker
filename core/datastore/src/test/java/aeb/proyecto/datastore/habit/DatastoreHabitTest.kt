package aeb.proyecto.datastore.habit

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.repository.DatastoreRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DatastoreHabitTest {

    @get:Rule
    val mainDispatchersRule = MainDispatcherRuleUnit()

    private var datastoreManager: DataStoreManager = mockk()
    private var datastoreRepository: DatastoreRepository = mockk()

    @Test
    fun `given a random value when called setTypeSelectedDate function on repository then calls the function`() = runTest{
        // GIVEN
        val typeSelected = "Daily"
        coEvery { datastoreRepository.setTypeSelected(typeSelected) } returns Unit

        //When
        datastoreRepository.setTypeSelected(typeSelected)

        //THEN
        coVerify { datastoreRepository.setTypeSelected(typeSelected) }
    }

    @Test
    fun `given a random value when called setTypeSelectedDate function on datastore then calls the function`() = runTest{
        // GIVEN
        val typeSelected = "Daily"
        coEvery { datastoreManager.setTypeSelectedDate(typeSelected) } returns Unit

        //When
        datastoreManager.setTypeSelectedDate(typeSelected)

        //THEN
        coVerify { datastoreManager.setTypeSelectedDate(typeSelected) }
    }

    @Test
    fun `given a value when called getTypeSelectedDate function on repository then return the value`() = runTest{
        // GIVEN
        val typeSelected = "Daily"
        coEvery { datastoreRepository.getTypeSelected() } returns typeSelected

        //When
        val result = datastoreRepository.getTypeSelected()

        //THEN
        assertEquals(typeSelected, result)
    }

    @Test
    fun `given a value when called getTypeSelectedDate function on datastore then return the value`() = runTest{
        // GIVEN
        val typeSelected = "Daily"
        coEvery { datastoreManager.getTypeSelected() } returns typeSelected

        //When
        val result = datastoreManager.getTypeSelected()

        //THEN
        assertEquals(typeSelected, result)
    }

}