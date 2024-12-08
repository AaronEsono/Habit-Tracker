package aeb.proyecto.habittracker.data.repo

import aeb.proyecto.habittracker.data.dao.DailyHabitDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyHabitRepo @Inject constructor(
    private val dailyHabitDao: DailyHabitDao
) {
}