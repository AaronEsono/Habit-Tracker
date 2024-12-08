package aeb.proyecto.habittracker.data.dao

import aeb.proyecto.habittracker.data.entities.DailyHabit
import androidx.room.Dao
import androidx.room.Insert

@Dao
interface DailyHabitDao {

    @Insert
    fun insertDailyHabit(dailyHabit: DailyHabit):Long

}