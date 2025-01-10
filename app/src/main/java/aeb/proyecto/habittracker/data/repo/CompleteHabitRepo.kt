package aeb.proyecto.habittracker.data.repo

import aeb.proyecto.habittracker.data.dao.CompleteDaoHabit
import aeb.proyecto.habittracker.data.model.firestoreHabit.CompleteHabit
import aeb.proyecto.habittracker.data.model.notification.NotificationWithName
import javax.inject.Inject

class CompleteHabitRepo @Inject constructor(
    private val completeDaoHabit: CompleteDaoHabit
) {

    fun getAll():List<CompleteHabit>{
        return completeDaoHabit.getAll()
    }

    fun setData(data:List<CompleteHabit>):List<NotificationWithName>{
        return completeDaoHabit.setData(data)
    }

}