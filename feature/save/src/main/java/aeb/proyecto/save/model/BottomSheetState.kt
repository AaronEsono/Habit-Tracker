package aeb.proyecto.save.model

/**
 * Encapsulates the visibility and configuration state of the synchronization bottom sheets.
 * * @property showBottomSheet Controls the visibility of the modal.
 * @property dataBottomSheet Defines the specific content type displayed in the modal.
 */
data class BottomSheetState(
    val showBottomSheet: Boolean = false,
    val dataBottomSheet: DataBottomSheet = DataBottomSheet.SAVE_HABIT
)