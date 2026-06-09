package aeb.proyecto.habit.model

import aeb.proyecto.room.entities.relations.HabitWithDay

/**
 * Immutable layout state aggregation tracking active configuration overlays for contextual sheets.
 */
data class BottomSheetUIState(
    val enabledSelectDateState : TypeBottomSheet.SelectDate = TypeBottomSheet.SelectDate(),
    val enabledConfigureHabitState : TypeBottomSheet.ConfigureHabit = TypeBottomSheet.ConfigureHabit(),
    val enabledEditHabitState: TypeBottomSheet.EditHabit = TypeBottomSheet.EditHabit(),
    val enabledDeleteHabitState: TypeBottomSheet.DeleteHabit = TypeBottomSheet.DeleteHabit()
)

/**
 * Polymorphic structural boundaries mapping targeted sheet layouts and their operational payloads.
 */
sealed class TypeBottomSheet {
    /** Refers to the rolling calendar selector sheet. */
    data class SelectDate(val enabled: Boolean = false): TypeBottomSheet()

    /** Refers to incremental quantitative tracking configuration sheets. */
    data class ConfigureHabit(val habitWithDay: HabitWithDay = HabitWithDay(), val enabled: Boolean = false): TypeBottomSheet()

    /** Refers to modification sheets targeted over baseline habit properties. */
    data class EditHabit(val idHabit: Long = -1L, val enabled: Boolean = false): TypeBottomSheet()

    /** Refers to system confirmation alerts handling absolute cascade deletions. */
    data class DeleteHabit(val enabled: Boolean = false, val color: Int = 0, val id:Long = 0L): TypeBottomSheet()
}