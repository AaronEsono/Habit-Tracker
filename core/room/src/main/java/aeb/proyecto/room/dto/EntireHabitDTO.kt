package aeb.proyecto.room.dto

import aeb.proyecto.room.entities.relations.EntireHabit

data class EntireHabitDTO(
    val habit: HabitDTO = HabitDTO(),
    val dailyHabits: MutableList<HabitDayDTO> = mutableListOf(),
    val notifications: MutableList<NotificationDTO> = mutableListOf()
)

fun EntireHabit.convertToDTO(): EntireHabitDTO {
    return EntireHabitDTO(
        habit = habit.convertToDTO(),
        dailyHabits = dailyHabits.map { it.convertToDTO() }.toMutableList(),
        notifications = notifications.map { it.convertToDTO() }.toMutableList()
    )
}

fun EntireHabitDTO.convertToEntireHabit(): EntireHabit {
    return EntireHabit(
        habit = habit.convertToHabit(),
        dailyHabits = dailyHabits.map { it.convertToHabitDay() }.toMutableList(),
        notifications = notifications.map { it.convertToHabitNotification() }.toMutableList()
    )
}