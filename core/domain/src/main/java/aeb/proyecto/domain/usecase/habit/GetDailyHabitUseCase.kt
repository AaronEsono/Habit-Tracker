package aeb.proyecto.domain.usecase.habit

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.DAILY_TAG
import aeb.proyecto.room.model.classes.MONTHLY_TAG
import aeb.proyecto.room.model.classes.RECURRING_TAG
import aeb.proyecto.room.model.classes.WEEKLY_TAG
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

/** Use case para obtener los hábitos de un tipo en un rango de tiempo */
class GetDailyHabitUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){
    fun getDailyHabitsByType(from: LocalDate,to:LocalDate, tag: String): Flow<List<HabitWithDailyHabit>> {
        return habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateAndType(from, to, tag)
    }

    fun getDailyHabitByDate(id:Long,date:LocalDate): HabitDay?{
        return habitWithDailyHabitRepo.getHabitDay(date,id)
    }

    fun insertHabitDay(habitDay: HabitDay){
        habitWithDailyHabitRepo.insertDailyHabit(habitDay)
    }

    fun updateHabitDay(habitDay: HabitDay){
        habitWithDailyHabitRepo.updateDailyHabit(habitDay)
    }

    fun deleteHabitDay(id:Long,date:LocalDate){
        habitWithDailyHabitRepo.deleteHabitDay(id,date)
    }

    fun deleteHabit(id:Long){
        habitWithDailyHabitRepo.deleteHabit(id)
    }

}