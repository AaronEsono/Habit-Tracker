package aeb.proyecto.room.model.classes

import java.time.LocalDate

/**
 * Sealed architectural hierarchy defining the behavioral recurrence and scheduling cadence of a habit.
 *
 * This structure encapsulates specialized configuration parameters for different execution models,
 * allowing the tracking engine to dynamically evaluate completion streaks and reset conditions.
 *
 * @property tag A unique, unified string discriminator token utilized for database serialization and parsing.
 */
sealed class TypeHabit(val tag: String) {
    /**
     * Represents a standard everyday routine layout with no shifting structural constraints.
     */
    data object Daily : TypeHabit(DAILY_TAG)

    /**
     * Represents a week-bounded behavioral target setup.
     *
     * @property numberDays The quantitative frequency target of active days required within the weekly window.
     * @property weeklyGoal Specifies if the global metric completion goal acts as an aggregated accumulation.
     */
    data class Weekly(val numberDays: Int, val weeklyGoal:Boolean) : TypeHabit(WEEKLY_TAG)

    /**
     * Represents a month-bounded behavioral target setup.
     *
     * @property numberTimes The quantitative frequency target of active sessions required within the monthly window.
     * @property monthlyGoal Specifies if the global metric completion goal acts as an aggregated accumulation.
     */
    data class Monthly(val numberTimes: Int, val monthlyGoal:Boolean) : TypeHabit(MONTHLY_TAG)

    /**
     * Represents a customized cyclical gap interval routine anchored to a definitive starting point.
     *
     * @property date The baseline [LocalDate] calendar timestamp tracking when the recurrence sequence originates.
     * @property interval The steady numerical frequency gap interval separating required execution days.
     */
    data class Recurring(val date: LocalDate, val interval: Int) : TypeHabit(RECURRING_TAG)
}

const val DAILY_TAG = "DAILY"
const val WEEKLY_TAG = "WEEKLY"
const val MONTHLY_TAG = "MONTHLY"
const val RECURRING_TAG = "RECURRING"
