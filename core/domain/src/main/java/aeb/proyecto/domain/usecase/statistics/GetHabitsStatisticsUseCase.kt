package aeb.proyecto.domain.usecase.statistics

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetHabitsStatisticsUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){

    fun getAllHabits(): Flow<List<Habit>> {
        return habitWithDailyHabitRepo.getAllHabits()
    }

    fun getHabitWithDailyHabit(id:Long): Flow<HabitWithDailyHabit?> {
        return habitWithDailyHabitRepo.getHabitWithDailyHabit(id)
    }

    fun getHabitWithDailyHabitsByDate(id:Long,from: LocalDate,to:LocalDate): Flow<HabitWithDailyHabit?> {
        return habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateToDate(id,from,to)
    }

}