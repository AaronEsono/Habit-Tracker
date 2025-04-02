package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.habit.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitWithDailyHabitDao {

    @Insert
    fun insertHabit(habit: Habit):Long

    @Insert
    fun insertDailyHabit(dailyHabit: DailyHabit):Long

    @Update
    fun updateDailyHabit(dailyHabit: DailyHabit)

    @Update
    fun updateHabit(habit: Habit)

    @Query("SELECT * FROM Habit")
    fun getAllHabits():List<Habit>

    @Query("SELECT * FROM DailyHabit WHERE idHabit = :id")
    fun getDailyHabits(id:Long):List<DailyHabit>

    @Transaction
    @Query("DELETE FROM Habit WHERE id = :habitId")
    suspend fun deleteHabit(habitId: Long)

    @Transaction
    @Query("SELECT * FROM HABIT")
    fun getHabits(): Flow<List<HabitWithDailyHabit>>
}