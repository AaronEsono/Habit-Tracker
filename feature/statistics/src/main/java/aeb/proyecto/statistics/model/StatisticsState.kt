package aeb.proyecto.statistics.model

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit

/**
 * Sealed class representing the global state of the Statistics screen.
 */
sealed class StatisticsState{
    /** Indicates the screen is currently fetching data. */
    data object Loading: StatisticsState()

    /** Represents an error state, containing the specific error [message]. */
    data class Error(val message: String): StatisticsState()

    /** Indicates a successful data load, containing the nested [StatisticsSuccessState]. */
    data class Success(val state: StatisticsSuccessState): StatisticsState()
}

/**
 * Sealed class representing the different possible successful data scenarios.
 */
sealed class StatisticsSuccessState{
    /** No habits have been found or user has no data. */
    data object Empty: StatisticsSuccessState()

    /**
     * Successful data state containing the full list of [habits]
     * and the currently [habitSelected] for detailed analysis.
     */
    data class Habits(val habits: List<Habit>, val habitSelected: HabitWithDailyHabit): StatisticsSuccessState()
}