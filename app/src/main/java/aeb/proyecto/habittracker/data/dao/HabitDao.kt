package aeb.proyecto.habittracker.data.dao

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.entities.HabitWithNotification
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    fun insertHabit(habit: Habit)

    @Query("SELECT * FROM HABIT")
    fun getHabits(): Flow<List<HabitWithDailyHabit>>

    @Query("DELETE FROM Habit WHERE id = :habitId")
    suspend fun deleteHabitById(habitId: Long)

    @Query("DELETE FROM dailyhabit where idHabit = :habitId")
    suspend fun deleteDailyHabitById(habitId: Long)

    @Query("SELECT * FROM Habit")
    fun getAllHabits():List<Habit>

    @Transaction
    suspend fun deleteHabit(habitId: Long) {
        deleteDailyHabitById(habitId)
        deleteHabitById(habitId)
    }
}