package aeb.proyecto.domain.usecase.addHabit

import aeb.proyecto.alarmmanager.NotificationUtils
import aeb.proyecto.room.entities.HabitNotification
import aeb.proyecto.room.model.NotificationWithNameAndColor
import javax.inject.Inject

/**
 * Domain Use Case acting as the orchestration gateway between the application rules and
 * the hardware-level Android alarm scheduling subsystems.
 *
 * Loops through input notifications to schedule or purge active system intents, insuring user
 * reminders fire precisely without clogging or leaking platform system resources.
 *
 * @property notificationUtils The abstracted infrastructure wrapper handling low-level OS platform alarms.
 */
class SetNotificationAddHabitUseCase @Inject constructor(
    private val notificationUtils: NotificationUtils,
) {

    /**
     * Iterates over a batch profile of configured notifications to bind their precise temporal alarms
     * inside the system kernel.
     *
     * @param notifications A collection of notification entities packaged alongside descriptive habit assets.
     */
    fun setAlarm(notifications:List<NotificationWithNameAndColor>){
        notifications.forEach {
            notificationUtils.setUpAlarm(it)
        }
    }

    /**
     * Intercepts and completely tears down active hardware alarms linked to a batch of notifications,
     * freeing platform system slots.
     *
     * @param notifications A collection of targeted notifications scheduled for eviction.
     */
    fun cancelAlarms(notifications:List<HabitNotification>){
        notifications.forEach {
            notificationUtils.cancelAlarm(it.id)
        }
    }

}