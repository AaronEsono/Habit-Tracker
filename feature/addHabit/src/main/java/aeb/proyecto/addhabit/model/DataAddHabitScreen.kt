package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.constants.PICK_TYPE_HABIT
import aeb.proyecto.addhabit.constants.getContrastColor
import aeb.proyecto.addhabit.constants.listColors
import aeb.proyecto.ui.date.DaysWeek
import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek

data class DataAddHabitScreen(
    var habitScreen: AddHabit = AddHabit(),

    var contrastColor:Color = getContrastColor(listColors[0]),
    var dayStartWeek: DayOfWeek = DayOfWeek.MONDAY,

    var isColorSelected:Boolean = false,
    var isIconSelected:Boolean = false,

    val showDialog:Boolean = false,
    val typeDialog:Int = PICK_TYPE_HABIT,

    val notificationSelected:AddHabitNotification = AddHabitNotification(),
    val bottomSheetState: BottomSheetState = BottomSheetState()
)