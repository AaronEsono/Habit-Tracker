package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.DailyHabitDao
import aeb.proyecto.room.entities.DailyHabit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyHabitRepo @Inject constructor(
    private val dailyHabitDao: DailyHabitDao
) {
    fun insertDailyHabit(dailyHabit: DailyHabit):Long {
        return dailyHabitDao.insertDailyHabit(dailyHabit)
    }

    fun updateDailyHabit(dailyHabit: DailyHabit) {
        dailyHabitDao.updateDailyHabit(dailyHabit)
    }

    fun getDailyHabits(id:Long):List<DailyHabit> {
        return dailyHabitDao.getDailyHabits(id)
    }

}