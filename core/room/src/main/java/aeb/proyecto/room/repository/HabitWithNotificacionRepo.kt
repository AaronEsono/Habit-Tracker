package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.HabitWithNotificationDao
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.entities.relations.HabitWithNotification
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitWithNotificacionRepo @Inject constructor(
    private val habitWithNotificationDao: HabitWithNotificationDao
) {

    fun insertHabit(habit: Habit, notifications: List<Notification>):Long{
        return habitWithNotificationDao.insertHabitAndNotifications(habit,notifications)
    }

    fun updateHabit(habit: Habit, notifications: List<Notification>){
        habitWithNotificationDao.updateHabit(habit,notifications)
    }

    fun getHabitById(id:Long): HabitWithNotification {
        return habitWithNotificationDao.getHabitById(id)
    }

    fun getNotificationById(id:Long):List<Notification>{
        return habitWithNotificationDao.getNotificationById(id)
    }

}