package aeb.proyecto.habittracker.data.dao

import aeb.proyecto.habittracker.data.entities.DailyHabit
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface DailyHabitDao {

    @Insert
    fun insertDailyHabit(dailyHabit: DailyHabit):Long

    @Transaction
    @Update
    fun updateDailyHabit(dailyHabit: DailyHabit)

}