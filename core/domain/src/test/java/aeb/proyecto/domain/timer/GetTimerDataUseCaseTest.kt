package aeb.proyecto.domain.timer

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.domain.MainDispatchersRule
import aeb.proyecto.domain.usecase.timer.GetTimerDataUseCase
import aeb.proyecto.domain.usecase.timer.getHourFromString
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetTimerDataUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatchersRule()

    private lateinit var mockDatastore: DatastoreInterface
    private lateinit var mockRepository: HabitWithDailyHabitRepo
    private lateinit var useCase: GetTimerDataUseCase

    @Before
    fun setUp() {
        mockDatastore = mockk(relaxed = true)
        mockRepository = mockk(relaxed = true)
        useCase = GetTimerDataUseCase(mockDatastore, mockRepository)
    }

    @Test
    fun givenAValidTimeString_whenGetHourFromStringIsCalled_thenReturnCorrectTriple() {
        // --- GIVEN ---
        val timeString = "01:30:45"

        // --- WHEN ---
        val result = getHourFromString(timeString)

        // --- THEN ---
        assertNotNull(result)
        assertEquals(Triple(1, 30, 45), result)
    }

    @Test
    fun givenAnInvalidTimeString_whenGetHourFromStringIsCalled_thenReturnNullWithoutCrashing() {
        // --- GIVEN ---
        val brokenString = "esto_no_es_un_tiempo"

        // --- WHEN ---
        val result = getHourFromString(brokenString)

        // --- THEN ---
        assertNull(result)
    }
}