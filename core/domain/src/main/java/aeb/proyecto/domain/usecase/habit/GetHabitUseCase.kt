package aeb.proyecto.domain.usecase.habit

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import javax.inject.Inject

class GetHabitUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
) {

    fun getHabit(id:Long):Habit{
        return habitWithDailyHabitRepo.getHabit(id)
    }

}