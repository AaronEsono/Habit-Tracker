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
    var nameTextField:TextFieldState = TextFieldState(),
    var descriptionTextField:TextFieldState = TextFieldState(),
    var timesHabit:TextFieldState = TextFieldState(initialText = "1"),
    val unit:Units = Units.TIMES,

    var color:Color = listColors[0],
    var contrastColor:Color = getContrastColor(listColors[0]),

    var icon:ImageVector = listIcons[0],
    var typeHabit:TypeHabit = TypeHabit.DAILY,

    var numberOfDaysWeek:Int = 1,
    var numberOfDaysMonth:Int = 1,
    var dateRecurringStartDate:LocalDate = LocalDate.now(),
    val intervalTextFieldState:TextFieldState = TextFieldState(initialText = "1"),

    var isColorSelected:Boolean = false,
    var isIconSelected:Boolean = false,

    val showDialog:Boolean = false,
    val typeDialog:Int = PICK_TYPE_HABIT,

    val typeNotificationSelected:TypeNotification = TypeNotification.Daily(),
    val notifications:List<AddHabitNotification> = listOf()
)