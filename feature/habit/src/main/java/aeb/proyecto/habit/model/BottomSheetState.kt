package aeb.proyecto.habit.model

data class BottomSheetState(
    var isExpanded: Boolean = false,
    val type: BottomSheetType = BottomSheetType.SELECT_DATE,
)



enum class BottomSheetType{
    SELECT_DATE
}