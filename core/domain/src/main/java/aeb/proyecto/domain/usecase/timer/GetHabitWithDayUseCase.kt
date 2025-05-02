package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import java.time.LocalDate
import javax.inject.Inject

class GetHabitWithDayUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
) {

    fun getHabitWithDay(id:Long,date: LocalDate): HabitWithDay {
        return habitWithDailyHabitRepo.getHabitWithDay(id,date)
    }

}