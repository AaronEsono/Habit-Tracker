package aeb.proyecto.room.dto

import aeb.proyecto.room.entities.relations.EntireHabit

/**
 * Monolithic network/data transfer representation of a holistic habit configuration graph.
 *
 * This DTO decouples the localized persistent database schemas from boundary transaction layers
 * (such as remote Firestore sync or cross-module serialization pipelines), mirroring [EntireHabit].
 *
 * @property habit The decoupled data transfer metadata snapshot of the behavior.
 * @property dailyHabits A mutable collection of historical transaction logs formatted for transport.
 * @property notifications A mutable collection of scheduled reminder rules formatted for transport.
 */
data class EntireHabitDTO(
    val habit: HabitDTO = HabitDTO(),
    val dailyHabits: MutableList<HabitDayDTO> = mutableListOf(),
    val notifications: MutableList<NotificationDTO> = mutableListOf()
)

/**
 * Transforms a local persistent database entity aggregate [EntireHabit] into a decoupled [EntireHabitDTO].
 *
 * This extension maps the entire relational structural tree (metadata, logs, and alerts)
 * targeting data transport or serialization.
 *
 * @return A fully populated, transport-ready [EntireHabitDTO] snapshot.
 */
fun EntireHabit.convertToDTO(): EntireHabitDTO {
    return EntireHabitDTO(
        habit = habit.convertToDTO(),
        dailyHabits = dailyHabits.map { it.convertToDTO() }.toMutableList(),
        notifications = notifications.map { it.convertToDTO() }.toMutableList()
    )
}

/**
 * Reconstructs a localized relational database entity aggregate [EntireHabit] from a transport [EntireHabitDTO].
 *
 * This extension parses the flat transport structures back into thread-safe models compatible
 * with the underlying Room persistence engine engine.
 *
 * @return An isolated [EntireHabit] model ready for database transactions.
 */
fun EntireHabitDTO.convertToEntireHabit(): EntireHabit {
    return EntireHabit(
        habit = habit.convertToHabit(),
        dailyHabits = dailyHabits.map { it.convertToHabitDay() }.toMutableList(),
        notifications = notifications.map { it.convertToHabitNotification() }.toMutableList()
    )
}