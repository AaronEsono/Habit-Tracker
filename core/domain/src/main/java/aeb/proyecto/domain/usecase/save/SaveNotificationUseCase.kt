package aeb.proyecto.domain.usecase.save

import aeb.proyecto.alarmmanager.NotificationUtils
import aeb.proyecto.room.model.NotificationWithNameAndColor
import javax.inject.Inject

class SaveNotificationUseCase @Inject constructor(
    private val notificationUtils: NotificationUtils,
) {

    fun setNotifications(notifications: List<NotificationWithNameAndColor>) {
        notifications.forEach { notification ->
            notificationUtils.setUpAlarm(notification)
        }
    }

}