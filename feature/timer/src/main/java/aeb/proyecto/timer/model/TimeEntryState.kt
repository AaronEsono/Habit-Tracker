package aeb.proyecto.timer.model

import aeb.proyecto.room.entities.relations.TimeEntryWithHabit

sealed class TimeEntryState {
    data object EmptyList: TimeEntryState()
    data class TimeEntries(val timeEntries: List<TimeEntryWithHabit>): TimeEntryState()
}