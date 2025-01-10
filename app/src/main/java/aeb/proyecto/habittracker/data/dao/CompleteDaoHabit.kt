package aeb.proyecto.habittracker.data.dao

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.model.firestoreHabit.CompleteHabit
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
    fun insertNotification(notification: Notification)

    @Transaction
    fun deleteAll(){
        deleteNotifications()
        deleteDailyHabits()
        deleteHabits()
    }

    @Transaction
    fun setData(data:List<CompleteHabit>){
        deleteAll()

        data.forEach { habitComplete ->
            val id = insertHabit(habitComplete.habit)

            habitComplete.dailyHabits.forEach{ dailyHabit ->
                dailyHabit.idHabit = id
                insertDailyHabit(dailyHabit)
            }

            habitComplete.notifications.forEach{ notification ->
                notification.habitId = id
                insertNotification(notification)
            }
        }
    }

}