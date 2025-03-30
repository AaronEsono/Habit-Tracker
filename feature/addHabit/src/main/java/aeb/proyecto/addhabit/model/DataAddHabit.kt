package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.constants.PICK_TYPE_HABIT
import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.addhabit.constants.Units
import aeb.proyecto.addhabit.constants.getContrastColor
import aeb.proyecto.addhabit.constants.listColors
import aeb.proyecto.addhabit.constants.listIcons
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

data class DataAddHabit(
    var habitScreen: HabitScreen = HabitScreen(),

    var contrastColor:Color = getContrastColor(listColors[0]),

    var isColorSelected:Boolean = false,
    var isIconSelected:Boolean = false,

    val showDialog:Boolean = false,
    val typeDialog:Int = PICK_TYPE_HABIT,

    val notificationSelected:AddHabitNotification = AddHabitNotification(),
    val bottomSheetState: BottomSheetState = BottomSheetState()
)