package aeb.proyecto.habittracker.data.dao

import aeb.proyecto.habittracker.data.entities.DailyHabit
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DailyHabitDao {

    @Insert
    fun insertDailyHabit(dailyHabit: DailyHabit):Long

    @Update
    fun updateDailyHabit(dailyHabit: DailyHabit)

    @Query("SELECT * FROM DailyHabit WHERE idHabit = :id")
    fun getDailyHabits(id:Long):List<DailyHabit>
}