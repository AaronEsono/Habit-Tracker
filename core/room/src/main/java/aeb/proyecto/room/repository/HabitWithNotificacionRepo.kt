package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.HabitWithNotificationDao
import aeb.proyecto.room.entities.habit.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.entities.notification.HabitNotification
import aeb.proyecto.room.entities.relations.HabitWithNotification
import aeb.proyecto.room.model.NotificationWithNameAndColor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitWithNotificacionRepo @Inject constructor(
    private val habitWithNotificationDao: HabitWithNotificationDao
) {

    fun insertHabit(habitWithNotification: HabitWithNotification):Long{
        return habitWithNotificationDao.saveHabit(habitWithNotification)
    }

    fun updateHabit(habitWithNotification: HabitWithNotification):Long{
        return habitWithNotificationDao.updateHabit(habitWithNotification)
    }

    fun getHabitById(id:Long): HabitWithNotification {
        return habitWithNotificationDao.getHabitById(id)
    }

    fun getNotificationById(id:Long):List<HabitNotification>{
        return habitWithNotificationDao.getNotificationById(id)
    }

    fun getAllNotificationsWithId(id:Long):List<NotificationWithNameAndColor>{
        return habitWithNotificationDao.getAllNotificationsWithId(id)
    }

}