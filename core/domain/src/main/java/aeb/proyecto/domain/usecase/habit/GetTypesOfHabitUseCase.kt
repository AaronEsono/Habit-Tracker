package aeb.proyecto.domain.usecase.habit

import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Use case para obtener los tipos de hábitos existentes */
class GetTypesOfHabitUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
) {
    operator fun invoke(): Flow<List<String>> {
        return habitWithDailyHabitRepo.getExistingTypesHabit()
    }
}