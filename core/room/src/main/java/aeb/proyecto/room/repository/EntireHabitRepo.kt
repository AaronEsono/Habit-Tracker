package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.EntireHabitDao
import aeb.proyecto.room.model.NotificationWithName
import aeb.proyecto.room.relations.EntireHabit
import javax.inject.Inject

class EntireHabitRepo @Inject constructor(
    private val completeDaoHabit: EntireHabitDao
) {

    fun getAll():List<EntireHabit>{
        return completeDaoHabit.getAll()
    }

    fun setData(data:List<EntireHabit>):List<NotificationWithName>{
        return completeDaoHabit.setData(data)
    }

}