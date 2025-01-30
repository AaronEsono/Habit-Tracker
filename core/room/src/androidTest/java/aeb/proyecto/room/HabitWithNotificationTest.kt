package aeb.proyecto.room

import aeb.proyecto.room.dao.HabitWithDailyHabitDao
import aeb.proyecto.room.dao.HabitWithNotificationDao
import aeb.proyecto.room.database.DatabaseHabitTracker
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import aeb.proyecto.room.repository.HabitWithNotificacionRepo
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import javax.annotation.meta.When

@RunWith(AndroidJUnit4::class)
@SmallTest
class HabitWithNotificationTest {

    private lateinit var database: DatabaseHabitTracker
    private lateinit var habitWithNotificationDao: HabitWithNotificationDao
    private lateinit var habitWithNotificationRepo: HabitWithNotificacionRepo

    @Before
    fun setUpDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            DatabaseHabitTracker::class.java
        ).allowMainThreadQueries()
            .build()

        habitWithNotificationDao = database.habitWithNotificationDao()
        habitWithNotificationRepo = HabitWithNotificacionRepo(habitWithNotificationDao)
    }

    //Insert an habit and check that exist
    @Test
    fun insertAnHabitAndCheckThatExist() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")

        //When
        val habitId = habitWithNotificationDao.insertHabit(habit)

        //Then
        val habitWithDailyHabit = habitWithNotificationRepo.getHabitById(habitId)

        assertNotNull(habitWithDailyHabit)
        assertEquals("habit1",habitWithDailyHabit.habit.name)
    }

    //Insert and habit and a notification
    @Test
    fun insertAndHabitAndANotification() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")
        val notification = createNotification(hour = 1, minute = 30)
        val notification2 = createNotification(hour = 2, minute = 30)

        //When
        val listNotification = listOf(notification,notification2)
        val habitId = habitWithNotificationDao.insertHabitAndNotifications(habit,listNotification)

        //Then
        val habitWithNotifications = habitWithNotificationRepo.getHabitById(habitId)
        val notifications = habitWithNotifications.notifications

        assertNotNull(habitWithNotifications)
        assertEquals(2,notifications.size)
        assertEquals("habit1",habitWithNotifications.habit.name)
    }

    //Update and habit and a notification
    @Test
    fun updateAndHabitAndANotification() = runBlocking {
        //Given
        var habit = createHabit(name = "habit1")
        var notification = createNotification(hour = 1, minute = 30)
        var notification2 = createNotification(hour = 2, minute = 30)

        //When
        val listNotification = listOf(notification,notification2)
        val habitId = habitWithNotificationDao.insertHabitAndNotifications(habit,listNotification)

        habit = habit.copy(name = "habit2", id = habitId)
        notification = notification.copy(hour = 3)
        notification2 = notification2.copy(hour = 4)
        val listNotificationUpdated = listOf(notification,notification2)


        habitWithNotificationDao.updateHabit(habit.copy(id = habitId),listNotificationUpdated)

        //Then
        val habitWithNotifications = habitWithNotificationRepo.getHabitById(habitId)
        val notifications = habitWithNotifications.notifications
        
        assertNotNull(habitWithNotifications)
        assertEquals(2,notifications.size)
        assertEquals("habit2",habitWithNotifications.habit.name)
        assert(notifications[0].hour == 3 || notifications[0].hour == 4)
    }

    //Get a notification by id
    @Test
    fun getNotificationById() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")
        val notification = createNotification(hour = 1, minute = 30)

        //When
        val habitId = habitWithNotificationDao.insertHabitAndNotifications(habit, listOf(notification))
        
        //Then
        val notificationWithHabit = habitWithNotificationRepo.getNotificationById(habitId)
        
        assertNotNull(notificationWithHabit)
        assertEquals(1,notificationWithHabit.size)
    }
    
    //Insert a notification without a habit selected
    @Test
    fun insertANotificationWithoutAHabitSelected() = runBlocking {
        //Given
        val notification = createNotification(hour = 1, minute = 30)
    
        //When
        assertThrows(SQLiteConstraintException::class.java) {
            habitWithNotificationDao.insertNotifications(listOf(notification))
        }

        //Then
        val notificationWithHabit = habitWithNotificationRepo.getNotificationById(notification.habitId)
        assertEquals(0,notificationWithHabit.size)
    }

    // Delete notifications of a habit
    @Test
    fun deleteNotificationsOfAHabit() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")
        val notification = createNotification(hour = 1, minute = 30)

        //When
        val habitId = habitWithNotificationRepo.insertHabit(habit, listOf(notification))
        habitWithNotificationDao.deleteNotifications(habitId)

        //Then
        val notificationWithHabit = habitWithNotificationRepo.getNotificationById(habitId)

        assertEquals(0,notificationWithHabit.size)
    }

    //Concurrent in insert notifications
    @Test
    fun concurrentInInsertNotifications() = runBlocking {
        //Given
        val habit = createHabit(name = "habit1")
        val habit2 = createHabit(name = "habit2")
        val notification = createNotification(hour = 1, minute = 30)
        val notification2 = createNotification(hour = 2, minute = 30)
        var id1:Long = 10
        var id2:Long = 10

        //When
        coroutineScope {
            val job1 = launch {
                id1 = habitWithNotificationRepo.insertHabit(habit, listOf(notification))
            }

            val job2 = launch {
                id2 = habitWithNotificationRepo.insertHabit(habit2, listOf(notification2))
            }

            job1.join()
            job2.join()

            //Then
            val notificationWithHabit1 = habitWithNotificationRepo.getHabitById(id1)
            val notificationWithHabit2 = habitWithNotificationRepo.getHabitById(id2)

            assertNotNull(notificationWithHabit1)
            assertNotNull(notificationWithHabit2)
            assertEquals(1,notificationWithHabit1.notifications.size)
            assertEquals(1,notificationWithHabit2.notifications.size)
        }
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    fun createNotification(hour:Int = 1, minute:Int = 30, habitId:Long = 1): Notification {
        return Notification(
            hour = hour,
            minute = minute,
            habitId = habitId
        )
    }

}