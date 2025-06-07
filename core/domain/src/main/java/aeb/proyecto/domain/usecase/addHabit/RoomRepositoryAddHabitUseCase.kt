package aeb.proyecto.domain.usecase.addHabit

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithNotification
import aeb.proyecto.room.repository.HabitWithNotificacionRepo
import android.app.Notification
import javax.inject.Inject

class RoomRepositoryAddHabitUseCase @Inject constructor(
    private val habitWithNotificacionRepo: HabitWithNotificacionRepo,
) {

    fun getNotificationsById(id:Long) = habitWithNotificacionRepo.getNotificationById(id)
    fun getAllNotifications(id:Long) = habitWithNotificacionRepo.getAllNotificationsWithId(id)
    fun getHabitById(id:Long) = habitWithNotificacionRepo.getHabitById(id)
    fun insertHabit(habit: HabitWithNotification) = habitWithNotificacionRepo.insertHabit(habit)
    fun updateNotification(notification: HabitWithNotification) = habitWithNotificacionRepo.updateHabit(notification)
}