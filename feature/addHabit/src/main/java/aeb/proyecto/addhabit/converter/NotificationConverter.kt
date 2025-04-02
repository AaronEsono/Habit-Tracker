package aeb.proyecto.addhabit.converter

import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.room.entities.notification.HabitNotification

fun fromNotificationScreen(notificationScreen: AddHabitNotification): HabitNotification{
    return HabitNotification(
        time = notificationScreen.time,
        type = notificationScreen.type
    )
}

fun toNotificationScreen(habitNotification: HabitNotification): AddHabitNotification{
    return AddHabitNotification(
        time = habitNotification.time,
        type = habitNotification.type
    )
}