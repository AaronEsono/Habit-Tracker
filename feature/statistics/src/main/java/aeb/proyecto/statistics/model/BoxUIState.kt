package aeb.proyecto.statistics.model

import java.time.LocalDate

data class BoxUIState(
    val day: LocalDate,
    val dayState: DayBoxState
)

sealed class DayBoxState{
    data object NotDone: DayBoxState()
    data object Done: DayBoxState()
    data object Uncompleted: DayBoxState()
}
