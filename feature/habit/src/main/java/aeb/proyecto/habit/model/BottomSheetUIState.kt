package aeb.proyecto.habit.model

import aeb.proyecto.room.entities.relations.HabitWithDay

data class BottomSheetUIState(
    val isEnabled: Boolean = false,
    val typeOfBottomSheet: TypeBottomSheet = TypeBottomSheet.SelectDate,
)

sealed class TypeBottomSheet {
    data object SelectDate: TypeBottomSheet()
    data class EditHabitDay(val habitWithDay: HabitWithDay): TypeBottomSheet()
}