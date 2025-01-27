package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.relations.HabitWithDailyHabit
import androidx.room.Dao
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