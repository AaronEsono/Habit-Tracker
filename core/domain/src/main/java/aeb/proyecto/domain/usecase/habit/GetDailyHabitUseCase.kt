package aeb.proyecto.domain.usecase.habit

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
    operator fun invoke(from: LocalDate,to:LocalDate, tag: String): Flow<List<HabitWithDailyHabit>> {
        return habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateAndType(from, to, tag)
    }
}