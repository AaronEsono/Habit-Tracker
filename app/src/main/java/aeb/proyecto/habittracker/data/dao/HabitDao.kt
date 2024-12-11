package aeb.proyecto.habittracker.data.dao

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    fun insertHabit(habit: Habit)

    @Query("SELECT * FROM HABIT")
    fun getHabits(): Flow<List<HabitWithDailyHabit>>

    @Query("DELETE FROM Habit WHERE id = :habitId")
    suspend fun deleteHabitById(habitId: Long)
}