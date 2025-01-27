package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.HabitWithNotificationDao
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.relations.HabitWithNotification
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitWithNotificacionRepo @Inject constructor(
    private val habitWithNofiticationDao: HabitWithNotificationDao
) {

    fun insertaHabit(habit: Habit, notifications: List<Notification>):Long{
        return habitWithNofiticationDao.insertHabitAndNotifications(habit,notifications)
    }

    fun updateHabit(habit: Habit, notifications: List<Notification>){
        habitWithNofiticationDao.updateHabit(habit,notifications)
    }

    fun getHabitById(id:Long): HabitWithNotification {
        return habitWithNofiticationDao.getHabitById(id)
    }

    fun getNotificationById(id:Long):List<Notification>{
        return habitWithNofiticationDao.getNotificationById(id)
    }

}