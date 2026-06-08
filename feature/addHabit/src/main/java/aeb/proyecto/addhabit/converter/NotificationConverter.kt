package aeb.proyecto.addhabit.converter

import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.room.entities.HabitNotification

/**
 * Maps a presentation layer notification model instance into a domain/data layer entity.
 * This conversion ensures that transient UI state models remain completely isolated from
 * data architecture structures.
 *
 * @param notificationScreen The active [AddHabitNotification] presentation state snapshot to transform.
 * @return A fresh [HabitNotification] instance prepared for domain operations or persistence layers.
 */
fun fromNotificationScreen(notificationScreen: AddHabitNotification): HabitNotification {
    return HabitNotification(
        time = notificationScreen.time,
        type = notificationScreen.type
    )
}

/**
 * Maps a domain/data layer notification entity back into a presentation layer UI state model.
 * Rehydrates historical or persistent storage layers to update interactive frontend layouts.
 *
 * @param habitNotification The underlying [HabitNotification] entity retrieved from core storage or domain flows.
 * @return An isolated [AddHabitNotification] instance ready to be tracked inside the UI state engine.
 */
fun toNotificationScreen(habitNotification: HabitNotification): AddHabitNotification{
    return AddHabitNotification(
        time = habitNotification.time,
        type = habitNotification.type
    )
}