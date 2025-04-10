package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.HabitWithDailyHabitDao
import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.habit.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.TypeHabit
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HabitWithDailyHabitRepo @Inject constructor(
    private val habitWithDailyHabitDao: HabitWithDailyHabitDao
) {

    fun insert(habit: Habit): Long  {
        return habitWithDailyHabitDao.insertHabit(habit)
    }

    fun insertDailyHabit(dailyHabit: DailyHabit): Long {
        return habitWithDailyHabitDao.insertDailyHabit(dailyHabit)
    }

    fun updateHabit(habit: Habit) {
        habitWithDailyHabitDao.updateHabit(habit)
    }

    fun updateDailyHabit(dailyHabit: DailyHabit) {
        habitWithDailyHabitDao.updateDailyHabit(dailyHabit)
    }

    suspend fun deleteHabit(habitId: Long) {
        habitWithDailyHabitDao.deleteHabit(habitId)
    }

    fun getDailyHabits(id: Long): List<DailyHabit> {
        return habitWithDailyHabitDao.getDailyHabits(id)
    }

    fun getAllHabits(): List<Habit> {
        return habitWithDailyHabitDao.getAllHabits()
    }

    fun getHabits(): Flow<List<HabitWithDailyHabit>> {
        return habitWithDailyHabitDao.getHabits()
    }

    fun getExistingTypesHabit():Flow<List<String>>{
        return habitWithDailyHabitDao.getExistingTypesHabit()
            .map { types ->
                types
                    .distinctBy { it::class }
                    .map { it.tag }
            }
    }
}