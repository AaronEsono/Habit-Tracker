package aeb.proyecto.statistics.model

import java.time.LocalDate

/**
 * Data model representing the summary statistics for habit goal completion.
 *
 * @property numberOfDaysCompleted Total count of days the habit goal has been achieved.
 * @property numberOfBestStreak The maximum number of consecutive days the habit was completed.
 * @property consistencyPercentage Percentage representing how consistent the habit was performed (0-100).
 * @property bestStreakDates Pair containing the start and end [LocalDate] of the best streak.
 * @property numberOfCurrentStreak The number of consecutive days the habit has been completed up to today.
 * @property currentStreakDates Pair containing the start and end [LocalDate] of the current streak.
 */
data class GoalsDoneState(
    val numberOfDaysCompleted:Int = 0,
    val numberOfBestStreak:Int = 0,
    val consistencyPercentage: Int = 0,
    val bestStreakDates: Pair<LocalDate, LocalDate> = Pair(LocalDate.now(), LocalDate.now()),
    val numberOfCurrentStreak: Int = 0,
    val currentStreakDates: Pair<LocalDate, LocalDate> = Pair(LocalDate.now(), LocalDate.now()),
)