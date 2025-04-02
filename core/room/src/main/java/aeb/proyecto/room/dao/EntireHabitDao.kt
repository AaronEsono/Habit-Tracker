package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.habit.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.entities.relations.EntireHabit
import aeb.proyecto.room.model.NotificationWithNameAndColor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface EntireHabitDao {

    @Insert
    fun insertHabit(habit: Habit):Long

    @Insert
    fun insertDailyHabits(dailyHabit: List<DailyHabit>)

    @Insert
    fun insertNotification(notification: List<Notification>)

    @Query("DELETE FROM Habit")
    fun deleteHabits()

    @Query("""
        SELECT Notification.id AS id, Notification.hour AS hour, Notification.minute AS minute, Habit.name AS name, Habit.color AS color
        FROM Notification
        INNER JOIN Habit ON Notification.habitId = Habit.id
    """)
    fun getAllNotifications():List<NotificationWithNameAndColor>

    @Transaction
    @Query("SELECT * FROM Habit")
    fun getAll():List<EntireHabit>

    //Guardamos todos los valores, borramos los antiguos y devolvemos las notificaciones para el alarmManager
    @Transaction
    fun setData(data:List<EntireHabit>):List<NotificationWithNameAndColor>{
        //Borramos datos antiguos
        deleteHabits()

        //Insertamos los nuevos datos
        data.forEach { habitComplete ->
            //Insertamos el habito
            val id = insertHabit(habitComplete.habit)

            //Seteamos el id e insertarmos los dailyHabits
            val dailyHabits = habitComplete.dailyHabits.map { dailyHabit -> dailyHabit.copy(idHabit = id) }
            insertDailyHabits(dailyHabits)

            //Seteamos el id e insertamos las notificationes
            val notificationsHabit = habitComplete.notifications.map { notification -> notification.copy(habitId = id) }
            insertNotification(notificationsHabit)
        }

        //Devolvemos las notificaciones para el alarmManager con su id, nombre y color
        return getAllNotifications()
    }
}