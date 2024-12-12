package aeb.proyecto.habittracker.data.repo

import aeb.proyecto.habittracker.data.dao.HabitDao
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.entities.HabitWithNotification
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class HabitRepo @Inject constructor(
    private val habitDao: HabitDao
){

    fun insertHabit(habit: Habit) {
        habitDao.insertHabit(habit)
    }

    fun getHabits(): Flow<List<HabitWithDailyHabit>> {
        return habitDao.getHabits()
    }

    suspend fun deleteHabit(habitId: Long) {
        habitDao.deleteHabitById(habitId)
    }
}