package aeb.proyecto.habittracker.data.dao

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.model.firestoreHabit.CompleteHabit
import aeb.proyecto.habittracker.data.model.notification.NotificationWithName
import androidx.compose.ui.graphics.Color
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CompleteDaoHabit {
    @Query("SELECT * FROM Habit")
    fun getAll():List<CompleteHabit>

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
    fun setData(data:List<CompleteHabit>):List<NotificationWithName>{
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