package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.HabitWithDailyHabitDao
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class HabitWithDailyHabitRepo @Inject constructor(
    private val habitWithDailyHabitDao: HabitWithDailyHabitDao
) {

    fun insert(habit: Habit): Long  {
        return habitWithDailyHabitDao.insertHabit(habit)
    }

    fun insertDailyHabit(dailyHabit: HabitDay): Long {
        return habitWithDailyHabitDao.insertDailyHabit(dailyHabit)
    }

    fun updateHabit(habit: Habit) {
        habitWithDailyHabitDao.updateHabit(habit)
    }

    fun updateDailyHabit(dailyHabit: HabitDay) {
        habitWithDailyHabitDao.updateDailyHabit(dailyHabit)
    }

    suspend fun deleteHabit(habitId: Long) {
        habitWithDailyHabitDao.deleteHabit(habitId)
    }

    fun getDailyHabits(id: Long): List<HabitDay> {
        return habitWithDailyHabitDao.getDailyHabits(id)
    }

    fun getHabits(): Flow<List<HabitWithDailyHabit>> {
        return habitWithDailyHabitDao.getHabits()
    }

    fun getHabitDay(date: LocalDate, idHabit: Long): HabitDay? {
        return habitWithDailyHabitDao.getHabitDay(date, idHabit)
    }

    fun getExistingTypesHabit():Flow<List<String>>{
        return habitWithDailyHabitDao.getExistingTypesHabit()
            .map { types ->
                types
                    .distinctBy { it::class }
                    .map { it.tag }
            }
    }

    fun getHabitWithDailyHabitsByDateAndType(startDate: LocalDate, endDate: LocalDate, tag:String): Flow<List<HabitWithDailyHabit>> {
        return combine(
            habitWithDailyHabitDao.getAllHabits(),
            habitWithDailyHabitDao.getDailyHabitsByDateRange(startDate, endDate)
        ) { habits, filteredDays ->
            habits
                .filter { it.typeHabit.tag == tag }
                .map { habit ->
                    val daysForHabit = filteredDays.filter { it.idHabit == habit.id }
                    HabitWithDailyHabit(habit = habit, dailyHabits = daysForHabit.toMutableList())
                }
        }
    }

    fun getHabit(id:Long):Habit{
        return habitWithDailyHabitDao.getHabit(id)
    }

    fun deleteHabitDay(id:Long,date:LocalDate){
        habitWithDailyHabitDao.deleteHabitDay(id,date)
    }

    fun getHabitWithDay(id:Long,date:LocalDate): HabitWithDay {
        val habit = habitWithDailyHabitDao.getHabit(id)
        val day = habitWithDailyHabitDao.getDayByDate(id,date) ?: HabitDay(date = date, idHabit = id, goalDone = BigDecimal(0))

        return HabitWithDay(habit, day)
    }
}

