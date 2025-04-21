package aeb.proyecto.habit.model

data class DataHabit(
    val bottomSheetState: BottomSheetState = BottomSheetState(),
    val showEditHabitDayBT:EditHabitDayState = EditHabitDayState(),
)