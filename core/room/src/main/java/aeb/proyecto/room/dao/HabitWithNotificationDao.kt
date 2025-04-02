package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.habit.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.entities.relations.HabitWithNotification
import aeb.proyecto.room.model.NotificationWithNameAndColor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface HabitWithNotificationDao {
    @Insert
    fun insertNotifications(notification: List<Notification>)

    @Insert
    fun insertHabit(habit: Habit):Long

    @Update
    fun updateHabit(habit: Habit)

    @Update
    fun updateNotification(notification: List<Notification>)

    @Query("""
        SELECT Notification.id AS id, Notification.hour AS hour, Notification.minute AS minute, Habit.name AS name, Habit.color AS color
        FROM Notification
        INNER JOIN Habit ON Notification.habitId = Habit.id
        WHERE Habit.id = :id
    """)
    fun getAllNotificationswithId(id:Long):List<NotificationWithNameAndColor>

    @Query("DELETE FROM NOTIFICATION WHERE habitId = :id")
    fun deleteNotifications(id:Long)

    @Query("SELECT * FROM Notification where habitId = :id")
    fun getNotificationById(id:Long):List<Notification>

    @Transaction
    @Query("SELECT * FROM Habit WHERE id = :habitId")
    fun getHabitById(habitId: Long): HabitWithNotification

    @Transaction
    fun insertHabitAndNotifications(habit: Habit, notifications: List<Notification>):Long{
        val habitInserted = insertHabit(habit)

        if (notifications.isNotEmpty()) {
            insertNotifications(notifications.map { it.copy(habitId = habitInserted) })
        }

        return habitInserted
    }

    @Transaction
    fun updateHabit(habit: Habit, notifications: List<Notification>){
        updateHabit(habit)

        deleteNotifications(habit.id)

        if (notifications.isNotEmpty()) {
            insertNotifications(notifications.map { it.copy(habitId = habit.id) })
        }
    }
}