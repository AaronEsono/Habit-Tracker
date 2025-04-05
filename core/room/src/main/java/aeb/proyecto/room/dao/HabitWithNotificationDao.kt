package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.habit.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.entities.notification.HabitNotification
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
    fun insertNotifications(notification: List<HabitNotification>)

    @Insert
    fun insertHabit(habit: Habit):Long

    @Update
    fun updateHabit(habit: Habit)

    @Update
    fun updateNotification(notification: List<HabitNotification>)

    @Query("""
        SELECT HABITNOTIFICATION.id AS id, HABITNOTIFICATION.time AS time, Habit.name AS name, Habit.color AS color,
        HABITNOTIFICATION.type AS typeNotification
        FROM HABITNOTIFICATION
        INNER JOIN Habit ON HABITNOTIFICATION.habitId = Habit.id
        WHERE Habit.id = :id
    """)
    fun getAllNotificationsWithId(id:Long):List<NotificationWithNameAndColor>

    @Query("DELETE FROM HABITNOTIFICATION WHERE habitId = :id")
    fun deleteNotifications(id:Long)

    @Query("SELECT * FROM HABITNOTIFICATION where habitId = :id")
    fun getNotificationById(id:Long):List<HabitNotification>

    @Transaction
    @Query("SELECT * FROM Habit WHERE id = :habitId")
    fun getHabitById(habitId: Long): HabitWithNotification

    @Transaction
    fun saveHabit(habitWithNotification: HabitWithNotification):Long{
        val id = insertHabit(habitWithNotification.habit)

        if (habitWithNotification.notifications.isNotEmpty()) {
            insertNotifications(habitWithNotification.notifications.map { it.copy(habitId = id) })
        }

        return id
    }

    @Transaction
    fun updateHabit(habitWithNotification: HabitWithNotification):Long{
        val id = habitWithNotification.habit.id
        updateHabit(habitWithNotification.habit)

        deleteNotifications(habitWithNotification.habit.id)

        if (habitWithNotification.notifications.isNotEmpty()) {
            insertNotifications(habitWithNotification.notifications.map { it.copy(habitId = id) })
        }

        return id
    }
}