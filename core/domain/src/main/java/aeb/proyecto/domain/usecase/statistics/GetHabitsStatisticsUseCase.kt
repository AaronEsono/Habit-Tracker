package aeb.proyecto.domain.usecase.statistics

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitsStatisticsUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){

    fun getAllHabits(): Flow<List<Habit>> {
        return habitWithDailyHabitRepo.getAllHabits()
    }

    fun getHabitWithDailyHabit(id:Long): HabitWithDailyHabit {
        return habitWithDailyHabitRepo.getHabitWithDailyHabit(id)
    }

}