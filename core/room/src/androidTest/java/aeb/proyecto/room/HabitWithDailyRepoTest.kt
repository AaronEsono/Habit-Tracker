package aeb.proyecto.room

import aeb.proyecto.room.dao.HabitWithDailyHabitDao
import aeb.proyecto.room.database.DatabaseHabitTracker
import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class HabitWithDailyRepoTest {

    private lateinit var database: DatabaseHabitTracker
    private lateinit var habitWithDailyHabitDao: HabitWithDailyHabitDao
    private lateinit var habitWithDailyHabitRepo: HabitWithDailyHabitRepo

    @Before
    fun setUpDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            DatabaseHabitTracker::class.java
        ).allowMainThreadQueries()
            .build()

        habitWithDailyHabitDao = database.habitWithDailyHabitDao()
        habitWithDailyHabitRepo = HabitWithDailyHabitRepo(habitWithDailyHabitDao)
    }

    // Insert new habit and check if exist
    @Test
    fun insertNewHabitAndCheckIfExist() = runBlocking{
        //Given
        val habit = createHabit(name = "habit1")

        //When
        val value = habitWithDailyHabitRepo.insert(habit)

        //Then
        assertNotNull(value)
    }

    // insert daily habit associated to habit
    @Test
    fun insertDailyHabitAssociatedToHabit() = runBlocking{
        //Given
        val habit = createHabit(name = "habit1")
        val dailyHabit = createDailyHabit(date = "date1")

        //When
        val habitId = habitWithDailyHabitRepo.insert(habit)
        habitWithDailyHabitRepo.insertDailyHabit(dailyHabit.copy(idHabit = habitId))

        //Then
        val dailyHabits = habitWithDailyHabitRepo.getDailyHabits(habitId)
        val habitWithDailyHabit = habitWithDailyHabitRepo.getAllHabits().find{ it.id == habitId }

        assertNotNull(dailyHabits)
        assertEquals(1,dailyHabits.size)

        assertNotNull(habitWithDailyHabit)
        assertEquals(habitWithDailyHabit?.id,dailyHabits[0].idHabit)
    }

    // update habit existed
    @Test
    fun updateHabitExisted() = runBlocking{
        //Given
        val habit = createHabit(name = "habit1")

        //When
        val habitId = habitWithDailyHabitRepo.insert(habit)
        val habitUpdated = habit.copy(id = habitId, name = "habit2")
        habitWithDailyHabitRepo.updateHabit(habitUpdated)

        //Then
        val habitWithDailyHabitList = habitWithDailyHabitRepo.getAllHabits()
        val habitWithDailyHabit = habitWithDailyHabitList.find{ it.id == habitId }

        assertNotNull(habitWithDailyHabit)
        assertEquals("habit2",habitWithDailyHabit?.name)
        assertEquals(1,habitWithDailyHabitList.size)
    }

    // update daily habit associated to habit
    @Test
    fun updateDailyHabitExisted() = runBlocking{
        //Given
        val habit = createHabit(name = "habit1")
        val dailyHabit = createDailyHabit(date = "date1", timesDone = 0)

        //When
        val habitId = habitWithDailyHabitRepo.insert(habit)
        val dailyHabitId = habitWithDailyHabitRepo.insertDailyHabit(dailyHabit.copy(idHabit = habitId))

        val dailyHabitUpdated = dailyHabit.copy(id = dailyHabitId, idHabit = habitId, timesDone = 1, date = "date2")
        habitWithDailyHabitRepo.updateDailyHabit(dailyHabitUpdated)


        //Then
        val dailyHabits = habitWithDailyHabitRepo.getDailyHabits(habitId)

        assertNotNull(dailyHabits)
        assertEquals(1,dailyHabits.size)
        assertEquals(1,dailyHabits[0].timesDone)
        assertEquals("date2",dailyHabits[0].date)
    }

    // Delete habit and daily habits associated to habit
    @Test
    fun deleteHabitAndDailyHabitsAssociatedToHabit() = runBlocking{
        //Given
        val habit = createHabit(name = "habit1")
        val dailyHabit = createDailyHabit(date = "date1")
        val dailyHabit2 = createDailyHabit(date = "date2")

        //When
        val habitId = habitWithDailyHabitRepo.insert(habit)
        habitWithDailyHabitRepo.insertDailyHabit(dailyHabit.copy(idHabit = habitId))
        habitWithDailyHabitRepo.insertDailyHabit(dailyHabit2.copy(idHabit = habitId))

        habitWithDailyHabitRepo.deleteHabit(habitId)

        //Then
        val dailyHabits = habitWithDailyHabitRepo.getDailyHabits(habitId)
        val habits = habitWithDailyHabitRepo.getAllHabits()

        assertEquals(0,dailyHabits.size)
        assertEquals(0,habits.size)
    }

    // Obtain all habits
    @Test
    fun obtainAllHabits() = runBlocking{
        //Given
        val habit = createHabit(name = "habit1")
        val habit2 = createHabit(name = "habit2")

        //When
        habitWithDailyHabitRepo.insert(habit)
        habitWithDailyHabitRepo.insert(habit2)

        //Then
        val habits = habitWithDailyHabitRepo.getAllHabits()
        assertEquals(2,habits.size)
        assert(habits[0].name == "habit1" || habits[0].name == "habit2")
        assert(habits[1].name == "habit1" || habits[1].name == "habit2")
    }

    // Obtain daily habits associated to habit
    @Test
    fun obtainDailyHabitsAssociateToHabit() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")
        val dailyHabit = createDailyHabit(date = "date1")
        val dailyHabit2 = createDailyHabit(date = "date2")

        //When
        val habitId = habitWithDailyHabitRepo.insert(habit)
        habitWithDailyHabitRepo.insertDailyHabit(dailyHabit.copy(idHabit = habitId))
        habitWithDailyHabitRepo.insertDailyHabit(dailyHabit2.copy(idHabit = habitId))

        //Then
        val dailyHabits = habitWithDailyHabitRepo.getDailyHabits(habitId)
        assertEquals(2,dailyHabits.size)
        assert(dailyHabits[0].date == "date1" || dailyHabits[0].date == "date2")
        assert(dailyHabits[1].date == "date1" || dailyHabits[1].date == "date2")
    }

    // Flow obtains correct data after changes
    @Test
    fun flowObtainsCorrectDataAfterChanges() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")
        val habit2 = createHabit(name = "habit2")
        val dailyHabit = createDailyHabit(date = "date1")

        //When
        val flowHabits = habitWithDailyHabitRepo.getHabits()

        val habitId = habitWithDailyHabitRepo.insert(habit)
        habitWithDailyHabitRepo.insert(habit2)

        habitWithDailyHabitRepo.insertDailyHabit(dailyHabit.copy(idHabit = habitId))

        //Then
        val habits = flowHabits.first()
        val dailyHabits = habits.flatMap { it.dailyHabits }

        assertEquals(2,habits.size)
        assertEquals(1,dailyHabits.size)
        assert(habits[0].habit.name == "habit1" || habits[0].habit.name == "habit2")
    }

    //Delete a habit that not exist
    @Test
    fun deleteHabitThatNotExist() = runBlocking{
        //Given
        val habit = createHabit(name = "habit1")

        //When
        val habitId = habitWithDailyHabitRepo.insert(habit)
        habitWithDailyHabitRepo.deleteHabit(habitId + 1)

        //Then
        val habits = habitWithDailyHabitRepo.getAllHabits()

        assertEquals(1,habits.size)
        assertEquals("habit1",habits[0].name)
    }

    //Insert a dailyHabit no associated to habit
    @Test
    fun insertDailyHabitNoAssociatedToHabit() = runBlocking{
        //Given
        val dailyHabit = createDailyHabit()

        // When
        assertThrows(SQLiteConstraintException::class.java) {
            habitWithDailyHabitRepo.insertDailyHabit(dailyHabit)
        }

        // Then
        val dailyHabits = habitWithDailyHabitRepo.getDailyHabits(dailyHabit.idHabit)
        assertEquals(0, dailyHabits.size)
    }

    // Update a dailyHabit that not exist
    @Test
    fun updateADailyHabitThatNotExist() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")
        var dailyHabit = createDailyHabit(date = "date1")

        //When
        val habitId = habitWithDailyHabitRepo.insert(habit)
        val dailyHabitId = habitWithDailyHabitRepo.insertDailyHabit(dailyHabit.copy(idHabit = habitId))

        dailyHabit = dailyHabit.copy(idHabit = habitId, date = "date2", id = dailyHabitId + 1)
        habitWithDailyHabitRepo.updateDailyHabit(dailyHabit)

        //Then
        val dailyHabits = habitWithDailyHabitRepo.getDailyHabits(habitId)
        assertEquals(1,dailyHabits.size)
        assertEquals("date1",dailyHabits[0].date)
    }

    //Obtain daily habits from a habit that not exist
     @Test
     fun obtainDailyHabitsFromHabitThatNotExist() = runBlocking {
         //Given
        val habit = createHabit(name = "habit1")
        val dailyHabit = createDailyHabit(date = "date1")

        //When
        val habitId = habitWithDailyHabitRepo.insert(habit)
        habitWithDailyHabitRepo.insertDailyHabit(dailyHabit.copy(idHabit = habitId))

        //Then
        val dailyHabits = habitWithDailyHabitRepo.getDailyHabits(habitId + 1)

        assertEquals(0,dailyHabits.size)
    }

    //Concurrent insert and updates in room
    @Test
    fun concurrentInsertAndUpdatesInRoom() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")
        var dailyHabit = createDailyHabit(date = "date1")

        //When
        val id = habitWithDailyHabitRepo.insert(habit)
        dailyHabit = dailyHabit.copy(idHabit = id)

        coroutineScope {
            val job1 = launch {
                var dailyHabit1 = dailyHabit.copy(date = "date1")
                val habitId =habitWithDailyHabitRepo.insertDailyHabit(dailyHabit1)

                dailyHabit1 = dailyHabit1.copy(date = "date4",id = habitId)
                habitWithDailyHabitRepo.updateDailyHabit(dailyHabit1)
            }

            val job2 = launch {
                var dailyHabit2 = dailyHabit.copy(date = "date2")
                val habitId = habitWithDailyHabitRepo.insertDailyHabit(dailyHabit2)

                dailyHabit2 = dailyHabit2.copy(date = "date3",id = habitId)
                habitWithDailyHabitRepo.updateDailyHabit(dailyHabit2)
            }

            job1.join()
            job2.join()
        }

        //Then
        val dailyHabits = habitWithDailyHabitRepo.getDailyHabits(1)

        assertEquals(2,dailyHabits.size)
        assert(dailyHabits[0].date == "date3" || dailyHabits[0].date == "date4")
    }


    @After
    fun closeDatabase() {
        database.close()
    }

    fun createHabit(
        name: String = "prueba",
        description: String? = "prueba",
        color: Int = 0,
        icon: String = "prueba",
        times: Int = 0,
        unit: Int = 0
    ): Habit {
        return Habit(
            name = name,
            description = description,
            color = color,
            icon = icon,
            times = times,
        )
    }

    fun createDailyHabit(idHabit: Long = 0, timesDone: Int = 0, date: String = "prueba"):DailyHabit{
        return DailyHabit(
            idHabit = idHabit,
            timesDone = timesDone,
            date = date
        )
    }

}