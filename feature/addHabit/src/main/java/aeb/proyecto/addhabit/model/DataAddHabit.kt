package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.constants.PICK_TYPE_HABIT
import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.addhabit.constants.listColors
import aeb.proyecto.addhabit.constants.listIcons
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class DataAddHabit(
    var nameTextField:TextFieldState = TextFieldState(),
    var descriptionTextField:TextFieldState = TextFieldState(),
    var color:Color = listColors[0],
    var icon:ImageVector = listIcons[0],
    var typeHabit:TypeHabit = TypeHabit.DAILY,
    var isColorSelected:Boolean = false,
    var isIconSelected:Boolean = false,
    val showDialog:Boolean = false,
    val typeDialog:Int = PICK_TYPE_HABIT
)