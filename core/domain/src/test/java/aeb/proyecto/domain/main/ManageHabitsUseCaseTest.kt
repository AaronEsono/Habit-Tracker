package aeb.proyecto.domain.main

import aeb.proyecto.domain.MainDispatchersRule
import aeb.proyecto.domain.usecase.main.ManageHabitsUseCase
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class ManageHabitsUseCaseTest {


    @get:Rule
    val mainDispatcherRule = MainDispatchersRule()

    private lateinit var mockRepository: HabitWithDailyHabitRepo
    private lateinit var useCase: ManageHabitsUseCase

    @Before
    fun setUp() {
        mockRepository = mockk(relaxed = true)
        useCase = ManageHabitsUseCase(mockRepository)
    }

    @Test
    fun `given an existing daily habit, when updateHabit is called, then accumulate progress and call update`() = runTest {
        // --- GIVEN ---
        val habitId = 10L
        val targetDate = LocalDate.of(2026, 7, 14)
        val progressToAdd = 5L

        // Simulamos que ya existe un registro de progreso para hoy con 10 unidades acumuladas
        val existingDailyHabit = HabitDay(
            id = 1L,
            idHabit = habitId,
            date = targetDate,
            goalDone = BigDecimal("10.0")
        )
        coEvery { mockRepository.getHabitDay(targetDate, habitId) } returns existingDailyHabit

        // --- WHEN ---
        useCase.updateHabit(id = habitId, date = targetDate, unit = progressToAdd)

        // --- THEN ---
        coVerify(exactly = 1) {
            mockRepository.updateDailyHabit(withArg { updatedHabit ->
                assert(updatedHabit.id == 1L)
                assert(updatedHabit.idHabit == habitId)
                assert(updatedHabit.goalDone == BigDecimal("15.0"))
            })
        }
        // Nos aseguramos de que NO ha intentado insertar uno nuevo
        coVerify(exactly = 0) { mockRepository.insertDailyHabit(any()) }
    }

    @Test
    fun `given no existing daily habit, when updateHabit is called, then create new baseline record and call insert`() = runTest {
        // --- GIVEN ---
        val habitId = 20L
        val targetDate = LocalDate.of(2026, 7, 14)
        val progressToAdd = 8L

        coEvery { mockRepository.getHabitDay(targetDate, habitId) } returns null

        // --- WHEN ---
        useCase.updateHabit(id = habitId, date = targetDate, unit = progressToAdd)

        // --- THEN ---
        coVerify(exactly = 1) {
            mockRepository.insertDailyHabit(withArg { newHabit ->
                assert(newHabit.idHabit == habitId)
                assert(newHabit.date == targetDate)
                assert(newHabit.goalDone == BigDecimal("8"))
            })
        }
        coVerify(exactly = 0) { mockRepository.updateDailyHabit(any()) }
    }

}