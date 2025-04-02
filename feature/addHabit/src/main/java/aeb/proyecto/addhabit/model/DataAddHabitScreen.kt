package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.constants.PICK_TYPE_HABIT
import aeb.proyecto.addhabit.constants.getContrastColor
import aeb.proyecto.addhabit.constants.listColors
import androidx.compose.ui.graphics.Color

data class DataAddHabitScreen(
    var habitScreen: AddHabit = AddHabit(),

    var contrastColor:Color = getContrastColor(listColors[0]),

    var isColorSelected:Boolean = false,
    var isIconSelected:Boolean = false,

    val showDialog:Boolean = false,
    val typeDialog:Int = PICK_TYPE_HABIT,

    val notificationSelected:AddHabitNotification = AddHabitNotification(),
    val bottomSheetState: BottomSheetState = BottomSheetState()
)