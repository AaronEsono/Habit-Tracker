package aeb.proyecto.habittracker.data.dao

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HabitDao {
    @Insert
    fun insertHabit(habit: Habit)

    @Query("SELECT * FROM HABIT")
    fun getHabits(): List<HabitWithDailyHabit>
}