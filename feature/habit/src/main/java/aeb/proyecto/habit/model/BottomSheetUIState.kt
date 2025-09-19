package aeb.proyecto.habit.model

data class BottomSheetUIState(
    val isEnabled: Boolean = false,
    val typeOfBottomSheet: TypeBottomSheet = TypeBottomSheet.SelectDate,
)