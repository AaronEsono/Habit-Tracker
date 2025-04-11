package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.TypeHabit
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
    fun insertDailyHabit(dailyHabit: HabitDay):Long

    @Update
    fun updateDailyHabit(dailyHabit: HabitDay)

    @Update
    fun updateHabit(habit: Habit)

    @Query("SELECT * FROM Habit")
    fun getAllHabits():List<Habit>

    @Query("SELECT * FROM HabitDay WHERE idHabit = :id")
    fun getDailyHabits(id:Long):List<HabitDay>

    @Transaction
    @Query("DELETE FROM Habit WHERE id = :habitId")
    suspend fun deleteHabit(habitId: Long)

    @Transaction
    @Query("SELECT * FROM HABIT")
    fun getHabits(): Flow<List<HabitWithDailyHabit>>

    @Query("SELECT DISTINCT typeHabit FROM Habit")
    fun getExistingTypesHabit():Flow<List<TypeHabit>>
}