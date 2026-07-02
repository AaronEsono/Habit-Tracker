package aeb.proyecto.datastore.stopwatch

import aeb.proyecto.datastore.DataStoreManager
import aeb.proyecto.datastore.MainDispatcherRuleUnit
import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.datastore.repository.DatastoreRepository
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DatastoreStopwatchTest {

    @get:Rule
    val mainDispatchersRule = MainDispatcherRuleUnit()

    private var datastoreManager: DataStoreManager = mockk()
    private var datastoreRepository: DatastoreRepository = mockk()


    @Test
    fun `given a expected timePassed when called getTimePassedTimer function on repository then return the same value`() = runTest{
        // GIVEN
        val timePassed = 6000L
        coEvery { datastoreRepository.getTimePassedTimer() } returns timePassed

        //When
        val result = datastoreRepository.getTimePassedTimer()

        //THEN
        assertEquals(timePassed, result)
        coVerify { datastoreRepository.getTimePassedTimer() }
    }

    @Test
    fun `given a expected timePassed when called getTimePassedTimer function on datastore then return the same value`() = runTest{
        // GIVEN
        val timePassed = 6000L
        coEvery { datastoreManager.getTimePassedTimer() } returns timePassed

        //When
        val result = datastoreManager.getTimePassedTimer()

        //THEN
        assertEquals(timePassed, result)
        coVerify { datastoreManager.getTimePassedTimer() }
    }

    @Test
    fun `given a value when called setTimePassedTimer function on repository then return the same value`() = runTest{
        // GIVEN
        val timePassed = 400L
        coEvery { datastoreRepository.setTimePassedTimer(timePassed) } returns Unit

        //When
        datastoreRepository.setTimePassedTimer(timePassed)

        //THEN
        coVerify { datastoreRepository.setTimePassedTimer(timePassed) }
    }

    @Test
    fun `given a value when called setTimePassedTimer function on datastore then return the same value`() = runTest{
        // GIVEN
        val timePassed = 400L
        coEvery { datastoreManager.setTimePassedTimer(timePassed) } returns Unit

        //When
        datastoreManager.setTimePassedTimer(timePassed)

        //THEN
        coVerify { datastoreManager.setTimePassedTimer(timePassed) }
    }

    @Test
    fun `given a value when called setIsLinkedHabitAndFinished function on repository then return the same value`() = runTest{
        // GIVEN
        val linkedAndFinished = true
        coEvery { datastoreRepository.setIsLinkedHabitAndFinished(linkedAndFinished) } returns Unit

        //When
        datastoreRepository.setIsLinkedHabitAndFinished(linkedAndFinished)

        //THEN
        coVerify { datastoreRepository.setIsLinkedHabitAndFinished(linkedAndFinished) }
    }

    @Test
    fun `given a value when called setIsLinkedHabitAndFinished function on datastore then return the same value`() = runTest{
        // GIVEN
        val linkedAndFinished = true
        coEvery { datastoreManager.setIsLinkedHabitAndFinished(linkedAndFinished) } returns Unit

        //When
        datastoreManager.setIsLinkedHabitAndFinished(linkedAndFinished)

        //THEN
        coVerify { datastoreManager.setIsLinkedHabitAndFinished(linkedAndFinished) }
    }

    @Test
    fun `given a expected timePassed when called timerLinkedAndFinished function on repository then return the same value`() = runTest{
        // GIVEN
        val linkedAndFinished = true
        coEvery { datastoreRepository.timerLinkedAndFinished } returns flowOf(linkedAndFinished)

        //When
        datastoreRepository.timerLinkedAndFinished.test {

            //Then
            assertEquals(linkedAndFinished, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a expected timePassed when called timerLinkedAndFinished function on datastore then return the same value`() = runTest{
        // GIVEN
        val linkedAndFinished = true
        coEvery { datastoreManager.timerLinkedAndFinished } returns flowOf(linkedAndFinished)

        //When
        datastoreManager.timerLinkedAndFinished.test {

            //Then
            assertEquals(linkedAndFinished, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}