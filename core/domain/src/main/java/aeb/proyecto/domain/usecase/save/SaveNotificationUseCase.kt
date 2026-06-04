package aeb.proyecto.domain.usecase.save

import aeb.proyecto.alarmmanager.NotificationUtils
import aeb.proyecto.room.model.NotificationWithNameAndColor
import javax.inject.Inject

/**
 * Domain Use Case designed to orchestrate mass hardware-level alarm scheduling
 * during data restoration or synchronization workflows.
 *
 * Loops through a collection of reconstructed notification assets to re-register active
 * system intents inside the Android system kernel, ensuring user reminders remain intact after an import.
 *
 * @property notificationUtils The abstracted infrastructure wrapper handling low-level OS platform alarms.
 */
class SaveNotificationUseCase @Inject constructor(
    private val notificationUtils: NotificationUtils,
) {

    /**
     * Iterates over a bulk profile of recovered notification entities to re-bind their precise
     * temporal alarms inside the active device system layout.
     *
     * @param notifications A collection of notification entities packaged alongside descriptive habit assets.
     */
    fun setNotifications(notifications: List<NotificationWithNameAndColor>) {
        notifications.forEach { notification ->
            notificationUtils.setUpAlarm(notification)
        }
    }

}