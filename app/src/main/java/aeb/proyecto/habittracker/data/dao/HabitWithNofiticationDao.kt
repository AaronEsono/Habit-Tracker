package aeb.proyecto.habittracker.data.dao

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithNotification
import aeb.proyecto.habittracker.data.entities.Notification
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface HabitWithNofiticationDao {
    @Insert
    fun notification(notification: List<Notification>)

    @Insert
    fun insertHabit(habit: Habit):Long

    @Query("SELECT * FROM Habit WHERE id = :habitId")
    fun getHabitById(habitId: Long): HabitWithNotification

    @Transaction
    fun insertHabitAndNotifications(habit: Habit, notifications: List<Notification>){
        val habitInserted = insertHabit(habit)

        notifications.forEach {
            it.habitId = habitInserted
        }

        notification(notifications)
    }

    @Update
    fun updateHabit(habit: Habit)

    @Update
    fun updateNotification(notification: List<Notification>)

    @Query("DELETE FROM NOTIFICATION WHERE habitId = :id")
    fun deleteNotifications(id:Long)

    @Transaction
    fun updateHabit(habit: Habit, notifications: List<Notification>){
        updateHabit(habit)
        deleteNotifications(habit.id)
        notification(notifications)
    }
}