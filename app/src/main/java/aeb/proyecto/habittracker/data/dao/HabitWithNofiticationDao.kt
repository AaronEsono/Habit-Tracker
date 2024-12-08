package aeb.proyecto.habittracker.data.dao

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.Notification
import androidx.room.Dao
import androidx.room.Insert

@Dao
interface HabitWithNofiticationDao {
    @Insert
    fun notification(notification: List<Notification>)

    @Insert
    fun insertHabit(habit: Habit):Long
}