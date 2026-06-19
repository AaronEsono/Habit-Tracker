package aeb.proyecto.timer.model

import aeb.proyecto.room.entities.relations.TimeEntryWithHabit

/**
 * Represents the state of the historical time entries list.
 */
sealed class TimeEntryState {

    /** Indicates that there are no recorded sessions to display. */
    data object EmptyList: TimeEntryState()

    /** Contains the list of retrieved time entries paired with their linked habit. */
    data class TimeEntries(val timeEntries: List<TimeEntryWithHabit>): TimeEntryState()
}