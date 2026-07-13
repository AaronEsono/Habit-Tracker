package aeb.proyecto.room.entireHabit

import aeb.proyecto.room.builder.dailyHabitBuilder
import aeb.proyecto.room.builder.habitBuilder
import aeb.proyecto.room.builder.notificationBuilder
import aeb.proyecto.room.dao.EntireHabitDao
import aeb.proyecto.room.database.DatabaseHabitTracker
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.HabitNotification
import aeb.proyecto.room.entities.relations.EntireHabit
import aeb.proyecto.room.model.classes.TypeNotification
import android.R
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class EntireHabitDaoTest {

    private lateinit var database: DatabaseHabitTracker
    private lateinit var entireHabitDao: EntireHabitDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            DatabaseHabitTracker::class.java
        ).allowMainThreadQueries().build()

        entireHabitDao = database.entireDaoHabit()
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
        entireHabitDao.insertHabit(list[0])

        //Then
        val result = entireHabitDao.getAll()
        assertEquals(list[0],result[0].habit)
    }

    @Test
    fun givenSomeHabitWhenCalledInsertHabitThenReturnTheExactAmountOfHabits() = runTest{
        //Given
        val list = habitBuilder(5)

        //When
        list.forEach {
            entireHabitDao.insertHabit(it)
        }

        //Then
        val result = entireHabitDao.getAll()
        assertEquals(list.size,result.map { it.habit }.size)
    }

    @Test
    fun givenOneHabitDayWhenCalledInsertDailyHabitThenReturnTheHabitDay() = runTest{
        //Given
        val list = dailyHabitBuilder(1)
        val habit = Habit(id = 1)

        //When
        entireHabitDao.insertHabit(habit)
        entireHabitDao.insertDailyHabits(list)

        //Then
        val result = entireHabitDao.getAll()
        assertEquals(list,result[0].dailyHabits)
    }

    @Test
    fun givenSomeHabitDayWhenCalledInsertDailyHabitThenReturnTheHabitDay() = runTest{
        //Given
        val list = dailyHabitBuilder(5)
        val habit = Habit(id = 1)

        //When
        entireHabitDao.insertHabit(habit)
        entireHabitDao.insertDailyHabits(list)

        //Then
        val result = entireHabitDao.getAll()
        assertEquals(list.size,result[0].dailyHabits.size)
    }

    @Test
    fun givenAHabitDayWhenCalledInsertDailyHabitWithNoHabitThenReturnNull() = runTest{
        //Given
        val list = dailyHabitBuilder(1)

        // --- WHEN & THEN ---
        assertThrows(SQLiteConstraintException::class.java) {
            entireHabitDao.insertDailyHabits(list)
        }
    }

    @Test
    fun givenOneNotificationWhenCalledInsertNotificationThenReturnTheNotification() = runTest{
        //Given
        val list = notificationBuilder(1)
        val habit = Habit(id = 1)

        //When
        entireHabitDao.insertHabit(habit)
        entireHabitDao.insertNotification(list)

        //Then
        val result = entireHabitDao.getAll()
        assertEquals(list,result[0].notifications)
    }

    @Test
    fun givenSomeNotificationWhenCalledInsertNotificationThenReturnTheNotification() = runTest{
        //Given
        val list = notificationBuilder(5)
        val habit = Habit(id = 1)

        //When
        entireHabitDao.insertHabit(habit)
        entireHabitDao.insertNotification(list)

        //Then
        val result = entireHabitDao.getAll()
        assertEquals(list.size,result[0].notifications.size)
    }

    @Test
    fun givenANotificationWhenCalledInsertNotificationWithNoHabitThenReturnNull() = runTest{
        //Given
        val list = notificationBuilder(1)

        // --- WHEN & THEN ---
        assertThrows(SQLiteConstraintException::class.java) {
            entireHabitDao.insertNotification(list)
        }
    }

    @Test
    fun givenOneNotificationWhenCalledInsertNotificationThenReturnTheNotificationWithColor() = runTest{
        //Given
        val notification = listOf<HabitNotification>(HabitNotification(id = 1, habitId = 1, type = TypeNotification.Recurring()))
        val habit = Habit(id = 1, color = 1, name = "prueba")

        //When
        entireHabitDao.insertHabit(habit)
        entireHabitDao.insertNotification(notification)

        //Then
        val result = entireHabitDao.getAllNotifications()[0]

        assertEquals("prueba",result.name)
        assertEquals(1,result.color)
        assertEquals(TypeNotification.Recurring(),result.typeNotification)
    }

    @Test
    fun givenAHabitWhenCalledDeleteHabitHabitsThenDeleteTheHabit() = runTest {
        //Given
        val list = habitBuilder(1)

        //When
        entireHabitDao.insertHabit(list[0])
        entireHabitDao.deleteHabits()

        //Then
        val result = entireHabitDao.getAll()
        assertEquals(emptyList<EntireHabit>(),result)
    }

    @Test
    fun givenSomeHabitWhenCalledDeleteHabitHabitsThenDeleteTheHabit() = runTest {
        //Given
        val list = habitBuilder(5)

        //When
        list.forEach { entireHabitDao.insertHabit(it) }
        entireHabitDao.deleteHabits()

        //Then
        val result = entireHabitDao.getAll()
        assertEquals(emptyList<EntireHabit>(),result)
    }

    @Test
    fun givenAHabitAndHabitDayWhenCalledDeleteHabitHabitsThenDeleteTheHabit() = runTest {
        //Given
        val list = habitBuilder(1)
        val habitDay = dailyHabitBuilder(1)

        //When
        entireHabitDao.insertHabit(list[0])
        entireHabitDao.insertDailyHabits(habitDay)
        entireHabitDao.deleteHabits()

        //Then
        val result = entireHabitDao.getAll()
        assertEquals(emptyList<EntireHabit>(),result)
    }

    @Test
    fun givenEntireHabitWhenCalledSetDataThenDeletesThePreviousData() = runTest {
        //Given
        val previousData = Habit(id = 10)
        val entireHabit = listOf(EntireHabit(Habit(id = 1)))

        //When
        entireHabitDao.insertHabit(previousData)
        entireHabitDao.setData(entireHabit)

        //Then
        val result = entireHabitDao.getAll()
        assertEquals(entireHabit,result)
    }

    @Test
    fun givenEntireHabitWhenCalledSetDataThenDeletesReturnTheNotification() = runTest {
        //Given
        val entireHabit = listOf(
            EntireHabit(
                Habit(id = 1, name = "prueba", color = 1), notifications = mutableListOf(
                    HabitNotification(id = 1, habitId = 1, type = TypeNotification.Recurring())
                )
            )
        )

        //When
        val result = entireHabitDao.setData(entireHabit)

        //Then
        assertEquals("prueba",result[0].name)
        assertEquals(1,result[0].color)
        assertEquals(TypeNotification.Recurring(),result[0].typeNotification)
    }

}