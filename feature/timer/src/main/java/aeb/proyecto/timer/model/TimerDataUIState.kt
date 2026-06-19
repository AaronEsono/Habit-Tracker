package aeb.proyecto.timer.model

import aeb.proyecto.room.entities.relations.HabitWithDay

/**
 * Represents the configuration state for the Timer setup screen.
 * Used to reflect the user's choices (time, sets, linked habit) before starting the timer.
 */
data class TimerDataUIState(
    val habitLinked: HabitLinkedState = HabitLinkedState.NoData,
    val typeTimer: SegmentedButtonOptions = SegmentedButtonOptions.Timer,
    val hourSelected: HourSelectedState = HourSelectedState.NoData,
    val restHour: HourSelectedState = HourSelectedState.NoData,
    val sets:Int = 1,
    val buttonEnabled:Boolean = false
)

/**
 * Wrapper for a potential linked habit.
 */
sealed class HabitLinkedState(){
    data object NoData : HabitLinkedState()
    data class Data(val data: HabitWithDay) : HabitLinkedState()
}

/**
 * Wrapper for time selections (H, M, S).
 * Uses a [Triple] to store values securely.
 */
sealed class HourSelectedState(){
    data object NoData : HourSelectedState()
    data class Data(val data: Triple<Int,Int,Int>) : HourSelectedState()
}