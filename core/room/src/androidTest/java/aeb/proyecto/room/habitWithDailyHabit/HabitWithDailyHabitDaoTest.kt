package aeb.proyecto.room.habitWithDailyHabit

import aeb.proyecto.room.builder.dailyHabitBuilder
import aeb.proyecto.room.builder.habitBuilder
import aeb.proyecto.room.dao.EntireHabitDao
import aeb.proyecto.room.dao.HabitWithDailyHabitDao
import aeb.proyecto.room.database.DatabaseHabitTracker
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.model.classes.TypeHabit
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HabitWithDailyHabitDaoTest {

    private lateinit var database: DatabaseHabitTracker
    private lateinit var habitWithDailyHabitDao: HabitWithDailyHabitDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            DatabaseHabitTracker::class.java
        ).allowMainThreadQueries().build()

        habitWithDailyHabitDao = database.habitWithDailyHabitDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenOneHabitWhenCalledInsertHabitThenReturnTheHabit() = runTest{
        //Given
        val list = habitBuilder(1)

        //When
        habitWithDailyHabitDao.insertHabit(list[0])

        //Then
        val result = habitWithDailyHabitDao.getHabit(1)
        assertEquals(list[0],result)
    }

    @Test
    fun givenSomeHabitWhenCalledInsertHabitThenReturnTheExactAmountOfHabits() = runTest{
        //Given
        val list = habitBuilder(5)

        //When
        list.forEach {
            habitWithDailyHabitDao.insertHabit(it)
        }

        //Then
        val result = habitWithDailyHabitDao.getAllHabits().test {
            assertEquals(list.size,awaitItem().size)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun givenOneHabitDayWhenCalledInsertDailyHabitThenReturnTheHabitDay() = runTest{
        //Given
        val list = dailyHabitBuilder(1)
        val habit = Habit(id = 1)

        //When
        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertDailyHabit(list[0])

        //Then
        val result = habitWithDailyHabitDao.getDailyHabits(1)
        assertEquals(list,result)
    }

    @Test
    fun givenSomeHabitDayWhenCalledInsertDailyHabitThenReturnTheHabitDay() = runTest{
        //Given
        val list = dailyHabitBuilder(5)
        val habit = Habit(id = 1)

        //When
        habitWithDailyHabitDao.insertHabit(habit)
        list.forEach {
            habitWithDailyHabitDao.insertDailyHabit(it)
        }

        //Then
        val result = habitWithDailyHabitDao.getDailyHabits(1)
        assertEquals(list.size,result.size)
    }

    @Test
    fun givenAHabitDayWhenCalledInsertDailyHabitWithNoHabitThenReturnNull() = runTest{
        //Given
        val dailyHabit = dailyHabitBuilder(1)

        // --- WHEN & THEN ---
        assertThrows(SQLiteConstraintException::class.java) {
            habitWithDailyHabitDao.insertDailyHabit(dailyHabit[0])
        }
    }

    @Test
    fun givenAHabitWhenCalledUpdateHabitThenReturnTheHabit() = runTest{
        //Given
        val habit = Habit(id = 1,name = "prueba")
        val updatedHabit = Habit(id = 1,name = "prueba2")

        //When
        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.updateHabit(updatedHabit)

        //Then
        val result = habitWithDailyHabitDao.getHabit(1)
        assertEquals(updatedHabit,result)
    }

    @Test
    fun givenADailyHabitWhenCalledUpdateDailyHabitThenReturnTheDailyHabit() = runTest{
        //Given
        val habit = Habit(id = 1,name = "prueba")
        val dailyHabit = HabitDay(id = 1,idHabit = 1, goalDone = 1.toBigDecimal())
        val updatedHabitDay = HabitDay(id = 1,idHabit = 1, goalDone = 10.toBigDecimal())

        //When
        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertDailyHabit(dailyHabit)
        habitWithDailyHabitDao.updateDailyHabit(updatedHabitDay)

        //Then
        val result = habitWithDailyHabitDao.getDailyHabits(1)
        assertEquals(updatedHabitDay,result[0])
    }

    @Test
    fun givenHabitsFlowWhenNewHabitIsInsertedThenFlowEmitsUpdatedListDynamically() = runTest {
        // --- GIVEN ---
        val habit1 = Habit(id = 1L, name = "Entrenar bíceps")
        val habit2 = Habit(id = 2L, name = "Hacer pizza casera")

        // Insertamos el primero para que el Flow no empiece completamente vacío
        habitWithDailyHabitDao.insertHabit(habit1)

        // --- WHEN & THEN ---
        habitWithDailyHabitDao.getAllHabits().test {

            val firstEmission = awaitItem()
            assertEquals(1, firstEmission.size)
            assertEquals(habit1, firstEmission.first())

            habitWithDailyHabitDao.insertHabit(habit2)

            val secondEmission = awaitItem()
            assertEquals(2, secondEmission.size)
            assertTrue(secondEmission.contains(habit1))
            assertTrue(secondEmission.contains(habit2))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenHabitsFlowWhenHabitIsDeletedThenFlowEmitsNothing() = runTest {
        //Given
        val list = habitBuilder(1)

        //When
        habitWithDailyHabitDao.insertHabit(list[0])
        habitWithDailyHabitDao.deleteHabit(1)

        //Then
        val result = habitWithDailyHabitDao.getHabit(1)
        assertEquals(null,result)
    }

    @Test
    fun givenAnHabitAndDayHabitWhenCalledGetHabitsThenReturnTheData() = runTest {
        //Given
        val habit = Habit(id = 1,name = "prueba")
        val dailyHabit = HabitDay(id = 1,idHabit = 1, goalDone = 1.toBigDecimal())

        //When
        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertDailyHabit(dailyHabit)

        //Then
        habitWithDailyHabitDao.getHabits().test {
            val emission = awaitItem()
            assertEquals(1,emission.size)
            assertEquals(habit,emission[0].habit)
            assertEquals(dailyHabit,emission[0].dailyHabits[0])
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun givenHabitsAndHabitDayFlowWhenNewHabitIsInsertedThenFlowEmitsUpdatedListDynamically() = runTest {
        // --- GIVEN ---
        val habitId1 = 1L
        val habitId2 = 2L

        val habit1 = Habit(id = habitId1, name = "Entrenar antebrazo")
        val dailyHabit1 = HabitDay(id = 10L, idHabit = habitId1, date = LocalDate.now())

        habitWithDailyHabitDao.insertHabit(habit1)
        habitWithDailyHabitDao.insertDailyHabit(dailyHabit1)

        // --- WHEN & THEN ---
        habitWithDailyHabitDao.getHabits().test {

            // 1. Primera emisión: Validamos que viene el primer hábito con su lista de días
            val firstEmission = awaitItem()
            assertEquals(1, firstEmission.size)
            assertEquals(habit1, firstEmission.first().habit)
            assertEquals(1, firstEmission.first().dailyHabits.size)
            assertEquals(dailyHabit1, firstEmission.first().dailyHabits.first())

            // 2. AÑADIR UN HÁBITO MÁS: Insertamos un segundo hábito (sin días aún)
            val habit2 = Habit(id = habitId2, name = "Leer a Dale Carnegie")
            habitWithDailyHabitDao.insertHabit(habit2)

            val secondEmission = awaitItem()
            assertEquals(2, secondEmission.size)
            val recoveredHabit2 = secondEmission.find { it.habit.id == habitId2 }
            assertNotNull(recoveredHabit2)
            assertTrue(recoveredHabit2!!.dailyHabits.isEmpty())

            val dailyHabit2 = HabitDay(id = 11L, idHabit = habitId2, date = LocalDate.now())
            habitWithDailyHabitDao.insertDailyHabit(dailyHabit2)

            val thirdEmission = awaitItem()
            val updatedHabit2 = thirdEmission.find { it.habit.id == habitId2 }

            assertNotNull(updatedHabit2)
            assertEquals(1, updatedHabit2!!.dailyHabits.size)
            assertEquals(dailyHabit2, updatedHabit2.dailyHabits.first())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenAnHabitAndDayHabitWhenCalledGetHabitWithHabitDayThenReturnTheData() = runTest {
        //Given
        val habit = Habit(id = 1,name = "prueba")
        val dailyHabit = HabitDay(id = 1,idHabit = 1, goalDone = 1.toBigDecimal())

        //When
        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertDailyHabit(dailyHabit)

        //Then
        habitWithDailyHabitDao.getHabitWithDailyHabits(1).test {
            val emission = awaitItem()
            assertEquals(habit,emission?.habit)
            assertEquals(dailyHabit,emission?.dailyHabits[0])
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun givenSomeHabitWhenCalledTypeHabitThenReturnTheTypeHabit() = runTest{
        //Given
        val habit1 = Habit(id = 1,name = "prueba", typeHabit = TypeHabit.Daily)
        val habit2 = Habit(id = 2,name = "prueba", typeHabit = TypeHabit.Monthly(numberTimes = 1, monthlyGoal = true))

        //When
        habitWithDailyHabitDao.insertHabit(habit1)
        habitWithDailyHabitDao.insertHabit(habit2)

        //Then
        habitWithDailyHabitDao.getExistingTypesHabit().test {
            val emission = awaitItem()
            assertEquals(2,emission.size)
            assertTrue(emission.contains(TypeHabit.Daily))
            assertTrue(emission.contains(TypeHabit.Monthly(numberTimes = 1, monthlyGoal = true)))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun givenHabitDayWhenCalledGetDailyHabitsByDateRangeThenReturnsTheCorrectedHabitDay() = runTest {
            //Given
            val habit = Habit(id = 1, name = "prueba")
            val dailyHabit1 = HabitDay(id = 1, idHabit = 1, date = LocalDate.of(2023, 1, 1))
            val dailyHabit2 = HabitDay(id = 2, idHabit = 1, date = LocalDate.of(2024, 1, 1))
            val dailyHabit3 = HabitDay(id = 3, idHabit = 1, date = LocalDate.of(2025, 1, 1))

            //When
            habitWithDailyHabitDao.insertHabit(habit)
            habitWithDailyHabitDao.insertDailyHabit(dailyHabit1)
            habitWithDailyHabitDao.insertDailyHabit(dailyHabit2)
            habitWithDailyHabitDao.insertDailyHabit(dailyHabit3)

            //Then
            habitWithDailyHabitDao.getDailyHabitsByDateRange(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 2, 1)
            ).test {
                val emission = awaitItem()
                assertEquals(2, emission.size)
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun givenHabitDayWhenCalledGetDailyHabitsByDateRangebyIdThenReturnsTheCorrectedHabitDay() = runTest {
        //Given
        val habit = Habit(id = 1, name = "prueba")
        val habit2 = Habit(id = 2, name = "prueba2")
        val dailyHabit1 = HabitDay(id = 1, idHabit = 1, date = LocalDate.of(2023, 1, 1))
        val dailyHabit2 = HabitDay(id = 2, idHabit = 2, date = LocalDate.of(2024, 1, 1))
        val dailyHabit3 = HabitDay(id = 3, idHabit = 1, date = LocalDate.of(2025, 1, 1))

        //When
        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertHabit(habit2)
        habitWithDailyHabitDao.insertDailyHabit(dailyHabit1)
        habitWithDailyHabitDao.insertDailyHabit(dailyHabit2)
        habitWithDailyHabitDao.insertDailyHabit(dailyHabit3)

        //Then
        habitWithDailyHabitDao.getDailyHabitsByDateRangeById(
            1,
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2024, 2, 1),

            ).test {
            val emission = awaitItem()
            assertEquals(1, emission.size)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun givenAnHabitDayWhenCalledGetHabitDayReturnsTheHabitDay() = runTest {
        //Given
        val habit = Habit(id = 1, name = "prueba")
        val dailyHabit = HabitDay(id = 1, idHabit = 1, date = LocalDate.of(2023, 1, 1))

        //When
        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertDailyHabit(dailyHabit)


        //Then
        val result = habitWithDailyHabitDao.getHabitDay( LocalDate.of(2023, 1, 1),1)
        assertEquals(dailyHabit,result)
    }

    @Test
    fun givenAnHabitDayWhenCalledGetHabitDayButWrongDateReturnsNull() = runTest {
        //Given
        val habit = Habit(id = 1, name = "prueba")
        val dailyHabit = HabitDay(id = 1, idHabit = 1, date = LocalDate.of(2023, 1, 1))

        //When
        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertDailyHabit(dailyHabit)


        //Then
        val result = habitWithDailyHabitDao.getHabitDay( LocalDate.of(2025, 1, 1),1)
        assertEquals(null,result)
    }

    @Test
    fun givenAnExistingHabitId_whenGetHabitFlowIsCalled_thenFlowEmitsTheHabitAndUpdatesDynamically() = runTest {
        // --- GIVEN ---
        val targetId = 1L
        val initialHabit = Habit(id = targetId, name = "Estudiar desarrollo Android")
        val updatedHabit = Habit(id = targetId, name = "Estudiar Jetpack Compose") // El cambio que aplicaremos

        habitWithDailyHabitDao.insertHabit(initialHabit)

        // --- WHEN & THEN ---
        habitWithDailyHabitDao.getHabitFlow(targetId).test {

            val firstEmission = awaitItem()
            assertNotNull(firstEmission)
            assertEquals(initialHabit.name, firstEmission?.name)

            habitWithDailyHabitDao.updateHabit(updatedHabit)

            val secondEmission = awaitItem()
            assertNotNull(secondEmission)
            assertEquals("Estudiar Jetpack Compose", secondEmission?.name)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenANonExistingHabitId_whenGetHabitFlowIsCalled_thenFlowEmitsNullSafely() = runTest {
        // --- GIVEN ---
        val wrongId = 999L

        // --- WHEN & THEN ---
        habitWithDailyHabitDao.getHabitFlow(wrongId).test {

            val emission = awaitItem()
            assertNull(emission)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenAnExistingHabitDay_whenDeleteHabitDayIsCalled_thenTheRecordIsRemovedFromDatabase() = runTest {
        // --- GIVEN ---
        val habit = Habit(id = 1, name = "prueba")
        val habitId = 1L
        val targetDate = LocalDate.of(2026, 7, 13)
        val habitDay = HabitDay(idHabit = habitId, date = targetDate)

        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertDailyHabit(habitDay)

        // --- WHEN ---
        habitWithDailyHabitDao.deleteHabitDay(habitId, targetDate)

        // --- THEN ---
        val result = habitWithDailyHabitDao.getDayByDate(habitId, targetDate)
        assertNull(result)
    }

    @Test
    fun givenANonExistingHabitDay_whenDeleteHabitDayIsCalled_thenDatabaseStateRemainsUnchanged() = runTest {
        // --- GIVEN ---
        val habit = Habit(id = 1, name = "prueba")
        val habitId = 1L
        val targetDate = LocalDate.of(2026, 7, 13)
        val differentDate = LocalDate.of(2026, 7, 14)

        val baselineDay = HabitDay(idHabit = habitId, date = differentDate)

        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertDailyHabit(baselineDay)

        // --- WHEN ---
        habitWithDailyHabitDao.deleteHabitDay(habitId, targetDate)

        // --- THEN ---
        val result = habitWithDailyHabitDao.getDayByDate(habitId, differentDate)
        assertNotNull(result)
    }

    @Test
    fun givenAnExistingHabitId_whenGetHabitOrNullIsCalled_thenReturnTheCorrectHabit() = runTest {
        // --- GIVEN ---
        val targetId = 42L
        val expectedHabit = Habit(id = targetId, name = "Ir al gimnasio")
        habitWithDailyHabitDao.insertHabit(expectedHabit)

        // --- WHEN ---
        val result = habitWithDailyHabitDao.getHabitOrNull(targetId)

        // --- THEN ---
        assertNotNull(result)
        assertEquals(expectedHabit, result)
    }

    @Test
    fun givenANonExistingHabitId_whenGetHabitOrNullIsCalled_thenReturnNull() = runTest {
        // --- GIVEN ---
        val unknownId = 999L
        // BD vacía o sin este ID

        // --- WHEN ---
        val result = habitWithDailyHabitDao.getHabitOrNull(unknownId)

        // --- THEN ---
        assertNull(result)
    }

    @Test
    fun givenMatchingIdAndDate_whenGetDayByDateIsCalled_thenReturnTheCorrectHabitDay() = runTest {
        // --- GIVEN ---
        val habitId = 5L
        val habit = Habit(id = habitId, name = "prueba")
        val targetDate = LocalDate.of(2026, 7, 13)
        val expectedDay = HabitDay(idHabit = habitId, id = 1, date = targetDate)

        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertDailyHabit(expectedDay)

        // --- WHEN ---
        val result = habitWithDailyHabitDao.getDayByDate(habitId, targetDate)

        // --- THEN ---
        assertNotNull(result)
        assertEquals(expectedDay, result)
    }

    @Test
    fun givenCorrectIdButIncorrectDate_whenGetDayByDateIsCalled_thenReturnNull() = runTest {
        // --- GIVEN ---
        val habitId = 5L
        val habit = Habit(id = habitId, name = "prueba")
        val correctDate = LocalDate.of(2026, 7, 13)
        val wrongDate = LocalDate.of(2026, 12, 25)

        val savedDay = HabitDay(idHabit = habitId, date = correctDate)

        habitWithDailyHabitDao.insertHabit(habit)
        habitWithDailyHabitDao.insertDailyHabit(savedDay)

        // --- WHEN ---
        val result = habitWithDailyHabitDao.getDayByDate(habitId, wrongDate)

        // --- THEN ---
        assertNull(result)
    }
}