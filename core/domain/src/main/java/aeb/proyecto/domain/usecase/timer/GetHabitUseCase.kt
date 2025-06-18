package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import javax.inject.Inject

class GetHabitUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){
    fun getAllHabitsWithTimeUnit() = habitWithDailyHabitRepo.getHabitWithTimeUnit()
}