package aeb.proyecto.habittracker.data.repo

import aeb.proyecto.habittracker.data.dao.DailyHabitDao
import aeb.proyecto.habittracker.data.entities.DailyHabit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyHabitRepo @Inject constructor(
    private val dailyHabitDao: DailyHabitDao
) {
    fun insertDailyHabit(dailyHabit: DailyHabit):Long {
        return dailyHabitDao.insertDailyHabit(dailyHabit)
    }

}