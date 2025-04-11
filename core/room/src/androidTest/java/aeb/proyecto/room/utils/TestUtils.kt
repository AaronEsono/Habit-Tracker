package aeb.proyecto.room.utils

import aeb.proyecto.room.entities.Habit

fun createDailyHabit(idHabit: Long = 0, timesDone: Int = 0, date: String = "prueba"): DailyHabit {
    return DailyHabit(
        idHabit = idHabit,
        timesDone = timesDone,
        date = date
    )
}

fun createHabit(
    name: String = "prueba",
    description: String? = "prueba",
    color: Int = 0,
    icon: String = "prueba",
    times: Int = 0,
    unit: Int = 0
): Habit {
    return Habit(
        name = name,
        description = description,
        color = color,
        icon = icon,
        times = times,
        unit = unit
    )
}

fun createNotification(hour: Int = 1, minute: Int = 30, habitId: Long = 1): Notification {
    return Notification(
        hour = hour,
        minute = minute,
        habitId = habitId
    )
}
