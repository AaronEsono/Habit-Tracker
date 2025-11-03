package aeb.proyecto.habit.model

import aeb.proyecto.room.entities.relations.HabitWithDay

data class BottomSheetUIState(
    val enabledSelectDateState : TypeBottomSheet.SelectDate = TypeBottomSheet.SelectDate(),
    val enabledConfigureHabitState : TypeBottomSheet.ConfigureHabit = TypeBottomSheet.ConfigureHabit(),
    val enabledEditHabitState: TypeBottomSheet.EditHabit = TypeBottomSheet.EditHabit()
)

sealed class TypeBottomSheet {
    data class SelectDate(val enabled: Boolean = false): TypeBottomSheet()
    data class ConfigureHabit(val habitWithDay: HabitWithDay = HabitWithDay(), val enabled: Boolean = false): TypeBottomSheet()
    data class EditHabit(val idHabit: Long = -1L, val enabled: Boolean = false): TypeBottomSheet()
}