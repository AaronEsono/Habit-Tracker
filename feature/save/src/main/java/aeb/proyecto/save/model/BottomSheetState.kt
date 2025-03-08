package aeb.proyecto.save.model

data class BottomSheetState(
    val showBottomSheet: Boolean = false,
    val dataBottomSheet: DataBottomSheet = DataBottomSheet.SAVE_HABIT
)