package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.model.NotificationWithName
import aeb.proyecto.room.relations.EntireHabit
import androidx.compose.ui.graphics.Color
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface EntireHabitDao {
    @Query("SELECT * FROM Habit")
    fun getAll():List<EntireHabit>

    @Query("DELETE FROM Notification")
    fun deleteNotifications()

    @Query("DELETE FROM DailyHabit")
    fun deleteDailyHabits()

    @Query("DELETE FROM Habit")
    fun deleteHabits()

    @Insert
    fun insertHabit(habit: Habit):Long

    @Insert
    fun insertDailyHabit(dailyHabit: DailyHabit)

    @Insert
    fun insertNotification(notification: Notification):Long

    @Transaction
    fun deleteAll(){
        deleteNotifications()
        deleteDailyHabits()
        deleteHabits()
    }

    @Transaction
    fun setData(data:List<EntireHabit>):List<NotificationWithName>{
        val notifications:MutableList<NotificationWithName> = mutableListOf()
        deleteAll()

        data.forEach { habitComplete ->
            val id = insertHabit(habitComplete.habit)

            habitComplete.dailyHabits.forEach{ dailyHabit ->
                dailyHabit.idHabit = id
                insertDailyHabit(dailyHabit)
            }

            habitComplete.notifications.forEach{ notification ->
                notification.habitId = id
                val id = insertNotification(notification)

                notifications.add(NotificationWithName(notification.copy(id = id),habitComplete.habit.name, Color(habitComplete.habit.color)))
            }
        }

        return notifications
    }

}