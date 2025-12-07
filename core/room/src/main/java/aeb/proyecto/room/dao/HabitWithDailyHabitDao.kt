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
import java.time.LocalDate

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
    fun getAllHabits():Flow<List<Habit>>

    @Query("SELECT * FROM HabitDay WHERE idHabit = :id")
    fun getDailyHabits(id:Long):List<HabitDay>

    @Transaction
    @Query("DELETE FROM Habit WHERE id = :habitId")
    fun deleteHabit(habitId: Long)

    @Transaction
    @Query("SELECT * FROM HABIT")
    fun getHabits(): Flow<List<HabitWithDailyHabit>>

    @Transaction
    @Query("SELECT * FROM HABIT where id = :id")
    fun getHabitWithDailyHabits(id:Long): HabitWithDailyHabit

    @Query("SELECT DISTINCT typeHabit FROM Habit")
    fun getExistingTypesHabit():Flow<List<TypeHabit>>

    @Query("SELECT * FROM HabitDay WHERE date BETWEEN :startDate AND :endDate")
    fun getDailyHabitsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<HabitDay>>

    @Query("SELECT * FROM HabitDay WHERE date BETWEEN :startDate AND :endDate AND idHabit = :id")
    fun getDailyHabitsByDateRangeById(id:Long, startDate: LocalDate, endDate: LocalDate): Flow<List<HabitDay>>

    @Query("SELECT * FROM HabitDay WHERE date = :date AND idHabit = :idHabit")
    fun getHabitDay(date: LocalDate, idHabit: Long): HabitDay?

    @Query("SELECT * FROM Habit WHERE id = :id")
    fun getHabit(id:Long):Habit

    @Query("SELECT * FROM Habit WHERE id = :id")
    fun getHabitFlow(id:Long): Flow<Habit>

    @Query("SELECT * FROM Habit WHERE id = :id")
    fun getHabitOrNull(id:Long):Habit?

    @Query("DELETE FROM HABITDAY where idHabit = :id AND date = :date")
    fun deleteHabitDay(id:Long,date:LocalDate)

    @Query("SELECT * FROM HabitDay WHERE date = :date AND idHabit = :id")
    fun getDayByDate(id:Long, date:LocalDate): HabitDay?
}