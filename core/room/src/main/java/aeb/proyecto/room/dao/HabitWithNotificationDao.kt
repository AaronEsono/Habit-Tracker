package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.relations.HabitWithNotification
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface HabitWithNotificationDao {
    @Insert
    fun notification(notification: List<Notification>)

    @Insert
    fun insertHabit(habit: Habit):Long

    @Query("SELECT * FROM Habit WHERE id = :habitId")
    fun getHabitById(habitId: Long): HabitWithNotification

    @Transaction
    fun insertHabitAndNotifications(habit: Habit, notifications: List<Notification>):Long{
        val habitInserted = insertHabit(habit)

        notifications.forEach {
            it.habitId = habitInserted
        }

        notification(notifications)
        return habitInserted
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

    @Query("SELECT * FROM Notification where habitId = :id")
    fun getNotificationById(id:Long):List<Notification>

}