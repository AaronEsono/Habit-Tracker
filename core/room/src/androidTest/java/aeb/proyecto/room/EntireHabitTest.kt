package aeb.proyecto.room

import aeb.proyecto.room.dao.EntireHabitDao
import aeb.proyecto.room.database.DatabaseHabitTracker
import aeb.proyecto.room.entities.habit.Habit
import aeb.proyecto.room.entities.relations.EntireHabit
import aeb.proyecto.room.repository.EntireHabitRepo
import aeb.proyecto.room.utils.createDailyHabit
import aeb.proyecto.room.utils.createHabit
import aeb.proyecto.room.utils.createNotification
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class EntireHabitTest {

    private lateinit var database: DatabaseHabitTracker
    private lateinit var entireHabitDao: EntireHabitDao
    private lateinit var entireHabitRepo: EntireHabitRepo

    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            DatabaseHabitTracker::class.java
        ).allowMainThreadQueries()
            .build()

        entireHabitDao = database.entireDaoHabit()
        entireHabitRepo = EntireHabitRepo(entireHabitDao)
    }

    //Insert a habit and a list of dailyHabit and notification
    @Test
    fun insertAHabitAndAListOfDailyHabitAndNotification() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")
        val dailyHabit = createDailyHabit(timesDone = 1, date = "date1")
        val dailyHabit2 = createDailyHabit(timesDone = 2, date = "date2")
        val notification = createNotification(hour = 1, minute = 30)
        val notification2= createNotification(hour = 1, minute = 30)

        //When
        val id = entireHabitDao.insertHabit(habit)
        entireHabitDao.insertDailyHabits(listOf(dailyHabit.copy(idHabit = id),dailyHabit2.copy(idHabit = id)))
        entireHabitDao.insertNotification(listOf(notification.copy(habitId = id),notification2.copy(habitId = id)))

        //Then
        val entireHabit = entireHabitRepo.getAll()

        assert(entireHabit.isNotEmpty())

        assertEquals(1,entireHabit.size)
        assertEquals(2,entireHabit[0].dailyHabits.size)
        assertEquals(2,entireHabit[0].notifications.size)

        assert(entireHabit[0].habit.name == "habit1")
    }

    //Get notifications with name and color
    @Test
    fun getNotificationsWithNameAndColor() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1", color = 1)
        val notification = createNotification(hour = 1, minute = 30)
        val notification2 = createNotification(hour = 2, minute = 30)

        //When
        val id = entireHabitDao.insertHabit(habit)
        entireHabitDao.insertNotification(listOf(notification.copy(habitId = id),notification2.copy(habitId = id)))

        //Then
        val notifications = entireHabitDao.getAllNotifications()

        assert(notifications.isNotEmpty())
        assertEquals(2,notifications.size)
        assert(notifications[0].name == "habit1")
        assert(notifications[1].name == "habit1")
        assert(notifications[0].color == 1)
        assert(notifications[0].hour == 1 || notifications[0].hour == 2)
    }

    // error insert daily habit without habit
    @Test
    fun errorInsertDailyHabitWithoutHabit() = runBlocking {
        //Given
        val dailyHabit = createDailyHabit(timesDone = 1, date = "date1")

        //When
        assertThrows(SQLiteConstraintException::class.java){
            entireHabitDao.insertDailyHabits(listOf(dailyHabit))
        }

        //Then
        val dailyHabits = entireHabitDao.getAll()

        assertEquals(0,dailyHabits.size)
    }

    // error insert notification without habit
    @Test
    fun errorInsertNotificationWithoutHabit() = runBlocking {
        //Given
        val notification = createNotification(hour = 1, minute = 30)

        //When
        assertThrows(SQLiteConstraintException::class.java){
            entireHabitDao.insertNotification(listOf(notification))
        }

        //Then
        val dailyHabits = entireHabitDao.getAll()

        assertEquals(0,dailyHabits.size)
    }

    //Delete all data in room
    @Test
    fun deleteAllDataInRoom() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")
        val dailyHabit = createDailyHabit(timesDone = 1, date = "date1")
        val dailyHabit2 = createDailyHabit(timesDone = 2, date = "date2")
        val notification = createNotification(hour = 1, minute = 30)
        val notification2= createNotification(hour = 1, minute = 30)

        //When
        val id = entireHabitDao.insertHabit(habit)
        entireHabitDao.insertDailyHabits(listOf(dailyHabit.copy(idHabit = id),dailyHabit2.copy(idHabit = id)))
        entireHabitDao.insertNotification(listOf(notification.copy(habitId = id),notification2.copy(habitId = id)))

        //Then
        entireHabitDao.deleteHabits()
        val entireHabit = entireHabitRepo.getAll()
        val notifications = entireHabitDao.getAllNotifications()

        assertEquals(0,entireHabit.size)
        assertEquals(0,notifications.size)
    }

    //Delete data and insertData
    fun deleteDataAndInsertData() = runBlocking {
        //Given
        //Old data
        val habit = createHabit(name = "habit1")
        val dailyHabit = createDailyHabit(timesDone = 1, date = "date1")
        val dailyHabit2 = createDailyHabit(timesDone = 2, date = "date2")
        val notification = createNotification(hour = 1, minute = 30)
        val notification2= createNotification(hour = 1, minute = 30)

        //When
        val id = entireHabitDao.insertHabit(habit)
        entireHabitDao.insertDailyHabits(listOf(dailyHabit.copy(idHabit = id),dailyHabit2.copy(idHabit = id)))
        entireHabitDao.insertNotification(listOf(notification.copy(habitId = id),notification2.copy(habitId = id)))

        //New data
        val newHabit = createHabit(name = "habit2")
        val newDailyHabit = createDailyHabit(timesDone = 3, date = "date3")
        val notification3 = createNotification(hour = 2, minute = 30)

        val data = listOf(EntireHabit(newHabit, mutableListOf(newDailyHabit),
            mutableListOf(notification3)
        ))

        entireHabitRepo.setData(data)

        //Then
        val alldata = entireHabitDao.getAll()

        assertEquals(1,alldata.size)
        assertEquals(1,alldata[0].dailyHabits.size)
        assertEquals(1,alldata[0].notifications.size)
    }

    //Delete nothing in room
    @Test
    fun deleteNothingInRoom() = runBlocking {
        //When
        entireHabitDao.deleteHabits()

        //Then
        val entireHabit = entireHabitRepo.getAll()

        assertEquals(0,entireHabit.size)
    }

    //Check the time insertar a large number of dailyHabits in room
    @Test
    fun checkTheTimeInsertarALargeNumberOfDailyHabitsInRoom() = runBlocking {
        //Given
        val numberOfDailyHabits = 5_000
        val numberOfHabits = 10

        val habits = (0..numberOfHabits).map {
            val dailyHabits = (0..numberOfDailyHabits).map {
                createDailyHabit(timesDone = 1, date = "date$it")
            }

            EntireHabit(
                habit = Habit(name = "habit$it"),
                notifications = mutableListOf(),
                dailyHabits = dailyHabits.toMutableList()
            )
        }

        //When
        val insertTimeStart = System.currentTimeMillis()

        entireHabitRepo.setData(habits)

        val insertTimeEnd = System.currentTimeMillis()

        val insertTime = insertTimeEnd - insertTimeStart

        //Then
        assertTrue("so much time to insert", insertTime < 5000)
    }


    @After
    fun closeDatabase(){
        database.close()
    }
}