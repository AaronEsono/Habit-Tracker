package aeb.proyecto.statistics.model

import java.time.LocalDate

/**
 * Represents the state of a single day in the statistics grid.
 * @property day The [LocalDate] associated with this box.
 * @property dayState The completion status of the habit for this specific day.
 */
data class BoxUIState(
    val day: LocalDate,
    val dayState: DayBoxState
)

/**
 * Defines the possible completion states for a habit on a specific day.
 * Used to determine the visual representation (e.g., color, progress indicator)
 * in the statistics calendar.
 */
sealed class DayBoxState{
    data object NotDone: DayBoxState()
    data object Done: DayBoxState()
    data object Uncompleted: DayBoxState()
}
