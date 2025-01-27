package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.HabitDao
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.relations.HabitWithDailyHabit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitRepo @Inject constructor(
    private val habitDao: HabitDao
){

    fun getAllHabits():List<Habit>{
        return habitDao.getAllHabits()
    }

    fun getHabits(): Flow<List<HabitWithDailyHabit>> {
        return habitDao.getHabits()
    }

    suspend fun deleteHabit(habitId: Long) {
        habitDao.deleteHabit(habitId)
    }
}