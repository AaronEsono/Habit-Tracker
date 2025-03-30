package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.addhabit.constants.Units
import aeb.proyecto.addhabit.constants.listColors
import aeb.proyecto.addhabit.constants.listIcons
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

data class HabitScreen(
    var nameTextField:TextFieldState = TextFieldState(),
    var descriptionTextField:TextFieldState = TextFieldState(),
    var numberTimesTextField:TextFieldState = TextFieldState(initialText = "1"),
    var unit:Units = Units.TIMES,

    val color: Color = listColors[0],
    var icon: ImageVector = listIcons[0],
    var typeHabit: TypeHabit = TypeHabit.DAILY,
    val notifications:List<AddHabitNotification> = listOf(),

    var numberOfDaysWeek:Int = 1,
    var numberOfDaysMonth:Int = 1,
    var dateRecurringStartDate: LocalDate = LocalDate.now(),
    val intervalTextFieldState:TextFieldState = TextFieldState(initialText = "1")
)