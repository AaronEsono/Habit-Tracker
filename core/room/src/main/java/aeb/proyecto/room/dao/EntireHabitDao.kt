package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.HabitNotification
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
    fun insertDailyHabits(dailyHabit: List<HabitDay>)

    @Insert
    fun insertNotification(notification: List<HabitNotification>)

    @Query("""
        SELECT HABITNOTIFICATION.id AS id, HABITNOTIFICATION.time AS time, Habit.name AS name, Habit.color AS color,
        HABITNOTIFICATION.type AS typeNotification
        FROM HABITNOTIFICATION
        INNER JOIN Habit ON HABITNOTIFICATION.habitId = Habit.id
    """)
    fun getAllNotifications():List<NotificationWithNameAndColor>

    @Query("DELETE FROM Habit")
    fun deleteHabits()

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