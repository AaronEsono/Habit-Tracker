package aeb.proyecto.domain.usecase.save

import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.repository.EntireHabitRepo
import javax.inject.Inject

class SaveHabitsRepositoryUseCase @Inject constructor(
    private val entireHabitRepo: EntireHabitRepo,
){

    fun getAll() = entireHabitRepo.getAll()

    fun setData(data:String):List<NotificationWithNameAndColor> = entireHabitRepo.setData(data)
}