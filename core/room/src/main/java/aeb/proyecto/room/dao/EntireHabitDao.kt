package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.relations.EntireHabit
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface EntireHabitDao {
    @Transaction
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

    //Guardamos todos los valores y devolvemos las notificaciones para el alarmManager
    @Transaction
    fun setData(data:List<EntireHabit>):List<NotificationWithNameAndColor>{
        val notifications:MutableList<NotificationWithNameAndColor> = mutableListOf()
        deleteAll()

        data.forEach { habitComplete ->
            val id = insertHabit(habitComplete.habit)

            habitComplete.dailyHabits.forEach{ dailyHabit ->
                dailyHabit.idHabit = id
                insertDailyHabit(dailyHabit)
            }

            habitComplete.notifications.forEach{ notification ->
                notification.habitId = id
                val idRoom = insertNotification(notification)

                notifications.add(NotificationWithNameAndColor(notification.copy(id = idRoom),habitComplete.habit.name, habitComplete.habit.color))
            }
        }

        return notifications
    }

}