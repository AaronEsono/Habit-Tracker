package aeb.proyecto.timer.model

import aeb.proyecto.room.entities.relations.HabitWithDay

data class TimerDataUIState(
    val habitLinked: HabitLinkedState = HabitLinkedState.NoData,
    val typeTimer: SegmentedButtonOptions = SegmentedButtonOptions.Timer,
    val hourSelected: HourSelectedState = HourSelectedState.NoData,
    val restHour: HourSelectedState = HourSelectedState.NoData
)

sealed class HabitLinkedState(){
    data object NoData : HabitLinkedState()
    data class Data(val data: HabitWithDay) : HabitLinkedState()
}

sealed class HourSelectedState(){
    data object NoData : HourSelectedState()
    data class Data(val data: Triple<Int,Int,Int>) : HourSelectedState()
}