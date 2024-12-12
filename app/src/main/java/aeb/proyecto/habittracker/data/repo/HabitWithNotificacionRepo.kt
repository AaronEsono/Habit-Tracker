package aeb.proyecto.habittracker.data.repo

import aeb.proyecto.habittracker.data.dao.HabitWithNofiticationDao
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithNotification
import aeb.proyecto.habittracker.data.entities.Notification
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitWithNotificacionRepo @Inject constructor(
    private val habitWithNofiticationDao: HabitWithNofiticationDao
) {

    fun insertaHabit(habit: Habit, notifications: List<Notification>){
        habitWithNofiticationDao.insertHabitAndNotifications(habit,notifications)
    }

    fun updateHabit(habit: Habit, notifications: List<Notification>){
        habitWithNofiticationDao.updateHabit(habit,notifications)
    }

    fun getHabitById(id:Long): HabitWithNotification {
        return habitWithNofiticationDao.getHabitById(id)
    }

}