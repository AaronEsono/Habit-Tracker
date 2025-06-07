package aeb.proyecto.domain.usecase.addHabit

import aeb.proyecto.alarmmanager.NotificationUtils
import aeb.proyecto.room.entities.HabitNotification
import aeb.proyecto.room.model.NotificationWithNameAndColor
import javax.inject.Inject

class SetNotificationAddHabitUseCase @Inject constructor(
    private val notificationUtils: NotificationUtils,
) {

    fun setAlarm(notifications:List<NotificationWithNameAndColor>){
        notifications.forEach {
            notificationUtils.setUpAlarm(it)
        }
    }

    fun cancelAlarms(notifications:List<HabitNotification>){
        notifications.forEach {
            notificationUtils.cancelAlarm(it.id)
        }
    }

}