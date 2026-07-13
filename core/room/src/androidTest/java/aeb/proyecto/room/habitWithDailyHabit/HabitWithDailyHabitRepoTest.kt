package aeb.proyecto.room.habitWithDailyHabit

import aeb.proyecto.room.dao.HabitWithDailyHabitDao
import aeb.proyecto.room.database.DatabaseHabitTracker
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.model.classes.DAILY_TAG
import aeb.proyecto.room.model.classes.MONTHLY_TAG
import aeb.proyecto.room.model.classes.RECURRING_TAG
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.WEEKLY_TAG
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class HabitWithDailyHabitRepoTest {

    private lateinit var database: DatabaseHabitTracker
    private lateinit var habitWithDailyHabitDao: HabitWithDailyHabitDao
    private lateinit var habitWithDailyHabitRepo: HabitWithDailyHabitRepo

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            DatabaseHabitTracker::class.java
        ).allowMainThreadQueries().build()

        habitWithDailyHabitDao = database.habitWithDailyHabitDao()
        habitWithDailyHabitRepo = HabitWithDailyHabitRepo(habitWithDailyHabitDao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenMultipleDistinctHabitTypesInDatabase_whenGetExistingTypesHabitIsCalled_thenFlowEmitsCorrectTagsWithoutDuplicates() = runTest {
        // --- GIVEN ---
        val habit1 = Habit(id = 1L, name = "Gimnasio", typeHabit = TypeHabit.Daily)
        val habit2 = Habit(id = 2L, name = "Estudiar", typeHabit = TypeHabit.Weekly(numberDays = 3, weeklyGoal = true))
        val habit3 = Habit(id = 3L, name = "Meditar", typeHabit = TypeHabit.Monthly(numberTimes = 4, monthlyGoal = false))

        habitWithDailyHabitDao.insertHabit(habit1)
        habitWithDailyHabitDao.insertHabit(habit2)
        habitWithDailyHabitDao.insertHabit(habit3)

        // --- WHEN & THEN ---
        habitWithDailyHabitRepo.getExistingTypesHabit().test {
            val emittedTags = awaitItem()

            assertEquals(3, emittedTags.size)
            assertTrue(emittedTags.contains(DAILY_TAG))
            assertTrue(emittedTags.contains(WEEKLY_TAG))
            assertTrue(emittedTags.contains(MONTHLY_TAG))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenMultipleHabitsOfTheSameTypeClass_whenGetExistingTypesHabitIsCalled_thenDistinctByFiltersOutClassDuplicates() = runTest {
        // --- GIVEN ---
        val habitWeekly1 = Habit(id = 1L, name = "Bíceps", typeHabit = TypeHabit.Weekly(numberDays = 3, weeklyGoal = true))
        val habitWeekly2 = Habit(id = 2L, name = "Antebrazo", typeHabit = TypeHabit.Weekly(numberDays = 5, weeklyGoal = false))

        val habitRecurring1 = Habit(id = 3L, name = "Pizza Casera", typeHabit = TypeHabit.Recurring(date = LocalDate.now(), interval = 7))
        val habitRecurring2 = Habit(id = 4L, name = "Revisión mensual", typeHabit = TypeHabit.Recurring(date = LocalDate.now().plusDays(1), interval = 30))

        habitWithDailyHabitDao.insertHabit(habitWeekly1)
        habitWithDailyHabitDao.insertHabit(habitWeekly2)
        habitWithDailyHabitDao.insertHabit(habitRecurring1)
        habitWithDailyHabitDao.insertHabit(habitRecurring2)

        // --- WHEN & THEN ---
        habitWithDailyHabitRepo.getExistingTypesHabit().test {
            val emittedTags = awaitItem()

            assertEquals(2, emittedTags.size)
            assertTrue(emittedTags.contains(WEEKLY_TAG))
            assertTrue(emittedTags.contains(RECURRING_TAG))

            assertEquals(emittedTags.distinct().size, emittedTags.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenMixedHabitTypes_whenGetHabitWithDailyHabitsByDateAndTypeIsCalled_thenFilterCorrectlyByTheSpecifiedTag() = runTest {
        // --- GIVEN ---
        val start = LocalDate.of(2026, 7, 1)
        val end = LocalDate.of(2026, 7, 7)

        val targetHabit = Habit(id = 1L, name = "Entrenar antebrazo", typeHabit = TypeHabit.Daily)
        val otherHabit = Habit(id = 2L, name = "Hacer pizza", typeHabit = TypeHabit.Recurring(date = start, interval = 7))

        habitWithDailyHabitDao.insertHabit(targetHabit)
        habitWithDailyHabitDao.insertHabit(otherHabit)

        val targetDay = HabitDay(idHabit = 1L,id = 1, date = LocalDate.of(2026, 7, 3))
        habitWithDailyHabitDao.insertDailyHabit(targetDay)

        // --- WHEN & THEN ---
        habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateAndType(start, end, DAILY_TAG).test {
            val resultList = awaitItem()

            assertEquals(1, resultList.size)
            assertEquals(targetHabit, resultList.first().habit)

            assertEquals(1, resultList.first().dailyHabits.size)
            assertEquals(targetDay, resultList.first().dailyHabits.first())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenMultipleHabitsWithSameTag_whenGetHabitWithDailyHabitsByDateAndTypeIsCalled_thenDistributeDailyHabitsToTheirCorrectParents() = runTest {
        // --- GIVEN ---
        val start = LocalDate.of(2026, 7, 1)
        val end = LocalDate.of(2026, 7, 7)

        val habitA = Habit(id = 10L, name = "Leer a Dale Carnegie", typeHabit = TypeHabit.Daily)
        val habitB = Habit(id = 20L, name = "Estudiar Jetpack Compose", typeHabit = TypeHabit.Daily)

        habitWithDailyHabitDao.insertHabit(habitA)
        habitWithDailyHabitDao.insertHabit(habitB)

        val dayForA = HabitDay(idHabit = 10L, id = 1, date = LocalDate.of(2026, 7, 2))
        val dayForB = HabitDay(idHabit = 20L, id = 2, date = LocalDate.of(2026, 7, 5))

        habitWithDailyHabitDao.insertDailyHabit(dayForA)
        habitWithDailyHabitDao.insertDailyHabit(dayForB)

        // --- WHEN & THEN ---
        habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateAndType(start, end, DAILY_TAG).test {
            val resultList = awaitItem()

            assertEquals(2, resultList.size)

            val relationA = resultList.find { it.habit.id == 10L }
            val relationB = resultList.find { it.habit.id == 20L }

            assertNotNull(relationA)
            assertNotNull(relationB)

            assertEquals(1, relationA!!.dailyHabits.size)
            assertEquals(dayForA, relationA.dailyHabits.first())

            assertEquals(1, relationB!!.dailyHabits.size)
            assertEquals(dayForB, relationB.dailyHabits.first())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenExistingHabitIdAndDaysInRange_whenGetHabitWithDailyHabitsByDateToDateIsCalled_thenFlowEmitsCombinedModelSuccessfully() = runTest {
        // --- GIVEN ---
        val habitId = 7L
        val start = LocalDate.of(2026, 7, 1)
        val end = LocalDate.of(2026, 7, 7)

        val targetHabit = Habit(id = habitId, name = "Ajustar rutina del gimnasio", typeHabit = TypeHabit.Daily)
        val dayInsideRange = HabitDay(idHabit = habitId, id = 1, date = LocalDate.of(2026, 7, 3))
        val dayOutsideRange = HabitDay(idHabit = habitId, id = 2, date = LocalDate.of(2026, 7, 15))

        habitWithDailyHabitDao.insertHabit(targetHabit)
        habitWithDailyHabitDao.insertDailyHabit(dayInsideRange)
        habitWithDailyHabitDao.insertDailyHabit(dayOutsideRange)

        // --- WHEN & THEN ---
        habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateToDate(habitId, start, end).test {
            val result = awaitItem()

            assertNotNull(result)
            assertEquals(targetHabit, result?.habit)

            assertEquals(1, result?.dailyHabits?.size)
            assertEquals(dayInsideRange, result?.dailyHabits?.first())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenNonExistingHabitId_whenGetHabitWithDailyHabitsByDateToDateIsCalled_thenFlowEmitsNullSafely() = runTest {
        // --- GIVEN ---
        val wrongHabitId = 999L
        val start = LocalDate.of(2026, 7, 1)
        val end = LocalDate.of(2026, 7, 7)

        // --- WHEN & THEN ---
        habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateToDate(wrongHabitId, start, end).test {
            val result = awaitItem()

            assertNull(result)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenExistingHabitAndExistingDay_whenGetHabitWithDayIsCalled_thenReturnCombinedModelWithSavedDay() = runTest {
        // --- GIVEN ---
        val habitId = 1L
        val targetDate = LocalDate.of(2026, 7, 13)

        val fakeHabit = Habit(id = habitId, name = "Estudiar patrones de diseño")
        val savedDay = HabitDay(idHabit = habitId,id = 1, date = targetDate, goalDone = BigDecimal("1.5"))

        habitWithDailyHabitDao.insertHabit(fakeHabit)
        habitWithDailyHabitDao.insertDailyHabit(savedDay)

        // --- WHEN ---
        val result = habitWithDailyHabitRepo.getHabitWithDay(habitId, targetDate)

        // --- THEN ---
        assertEquals(fakeHabit, result.habit)
        assertEquals(savedDay, result.day)
        assertEquals(BigDecimal("1.5"), result.day.goalDone)
    }

    @Test
    fun givenExistingHabitButNoDayInDatabase_whenGetHabitWithDayIsCalled_thenReturnCombinedModelWithSynthesizedZeroDay() = runTest {
        // --- GIVEN ---
        val habitId = 2L
        val targetDate = LocalDate.of(2026, 7, 13)
        val fakeHabit = Habit(id = habitId, name = "Entrenar bíceps")

        habitWithDailyHabitDao.insertHabit(fakeHabit)

        // --- WHEN ---
        val result = habitWithDailyHabitRepo.getHabitWithDay(habitId, targetDate)

        // --- THEN ---
        assertEquals(fakeHabit, result.habit)
        assertEquals(habitId, result.day.idHabit)
        assertEquals(targetDate, result.day.date)
        assertEquals(BigDecimal(0), result.day.goalDone)
    }

    @Test
    fun givenExistingHabitId_whenGetHabitWithDayOrNullIsCalled_thenReturnThePopulatedCombinedModel() = runTest {
        // --- GIVEN ---
        val habitId = 3L
        val targetDate = LocalDate.of(2026, 7, 13)
        val fakeHabit = Habit(id = habitId, name = "Leer a Dale Carnegie")

        habitWithDailyHabitDao.insertHabit(fakeHabit)

        // --- WHEN ---
        val result = habitWithDailyHabitRepo.getHabitWithDayOrNull(habitId, targetDate)

        // --- THEN ---
        assertNotNull(result)
        assertEquals(fakeHabit, result?.habit)
        assertEquals(BigDecimal(0), result?.day?.goalDone)
    }

    @Test
    fun givenNonExistingHabitId_whenGetHabitWithDayOrNullIsCalled_thenReturnNullSafely() = runTest {
        // --- GIVEN ---
        val wrongHabitId = 999L
        val targetDate = LocalDate.of(2026, 7, 13)

        // --- WHEN ---
        val result = habitWithDailyHabitRepo.getHabitWithDayOrNull(wrongHabitId, targetDate)

        // --- THEN ---
        assertNull(result)
    }

    @Test
    fun givenMixedHabitUnitsInDatabase_whenGetHabitWithTimeUnitIsCalled_thenFlowEmitsOnlyHabitsWithTimeUnit() = runTest {
        // --- GIVEN ---
        // Creamos hábitos que pertenecen a listTime (Tiempo)
        val timeHabit1 = Habit(id = 1L, name = "Meditar", unit = UnitHabit.MINUTES)
        val timeHabit2 = Habit(id = 2L, name = "Correr en cinta", unit = UnitHabit.HOURS)

        // Creamos hábitos que pertenecen a listQuantity o listFrequency (No son de tiempo)
        val quantityHabit = Habit(id = 3L, name = "Leer a Dale Carnegie", unit = UnitHabit.PAGES)
        val frequencyHabit = Habit(id = 4L, name = "Ir al gimnasio", unit = UnitHabit.SESSIONS)

        // Insertamos todos en la base de datos en memoria
        habitWithDailyHabitDao.insertHabit(timeHabit1)
        habitWithDailyHabitDao.insertHabit(timeHabit2)
        habitWithDailyHabitDao.insertHabit(quantityHabit)
        habitWithDailyHabitDao.insertHabit(frequencyHabit)

        // --- WHEN & THEN ---
        habitWithDailyHabitRepo.getHabitWithTimeUnit().test {
            val resultList = awaitItem()

            assertEquals(2, resultList.size)
            assertTrue(resultList.contains(timeHabit1))
            assertTrue(resultList.contains(timeHabit2))

            assertFalse(resultList.contains(quantityHabit))
            assertFalse(resultList.contains(frequencyHabit))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenOnlyQuantityAndFrequencyHabits_whenGetHabitWithTimeUnitIsCalled_thenFlowEmitsAnEmptyList() = runTest {
        // --- GIVEN ---
        val habit1 = Habit(id = 10L, name = "Caminar", unit = UnitHabit.STEPS)
        val habit2 = Habit(id = 11L, name = "Hacer flexiones", unit = UnitHabit.REPETITIONS)
        val habit3 = Habit(id = 12L, name = "Completar tareas", unit = UnitHabit.TASKS)

        habitWithDailyHabitDao.insertHabit(habit1)
        habitWithDailyHabitDao.insertHabit(habit2)
        habitWithDailyHabitDao.insertHabit(habit3)

        // --- WHEN & THEN ---
        habitWithDailyHabitRepo.getHabitWithTimeUnit().test {
            val resultList = awaitItem()

            assertNotNull(resultList)
            assertTrue(resultList.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }
}