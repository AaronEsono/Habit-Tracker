package aeb.proyecto.habit.components.bottomSheet.editHabitDay.screen

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.button.ButtonEditDay
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.screen.incompleteCases.HourIncompleteMode
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.screen.incompleteCases.UnitIncompleteMode
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.textField.TextFieldEditHabit
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils.halfTimesLeft
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils.isValidInput
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils.timesLeft
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.model.classes.unitsHourMode
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.regexTextField.IsOnlyDigit
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncompleteDay(
    habit: Habit,
    habitDay: HabitDay,
    onRestart:(id:Long,date:LocalDate) -> Unit,
    onClick:(id:Long, date: LocalDate, goalDone:BigDecimal) -> Unit
){

    val timesLeft = remember { timesLeft(habit.goal, habitDay.goalDone) }
    val halfTimesLeft = remember { halfTimesLeft(timesLeft, habit.unit) }

    /**Informacion seleccion y faltantes*/
    /** Si quedan habitos por hacer, esta pantalla */
    LabelLargeText(
        stringResource(
            R.string.habit_edit_habit_day_times_label,
            stringResource(habit.unit.titlePlural).lowercase()
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing16),
        fontSize = 16.sp
    )

    when{
        habit.unit !in unitsHourMode -> {
            UnitIncompleteMode(
                habit = habit,
                day = habitDay,
                leftTimes = timesLeft,
                halfTimesLeft = halfTimesLeft,
                onRestart = { id, date -> onRestart(id,date) },
                onClick =  { id, date, goalDone -> onClick(id,date,goalDone)}
            )
        }
        else -> {
            HourIncompleteMode(
                habit = habit,
                day = habitDay,
                leftTimes = timesLeft,
                halfTimesLeft = halfTimesLeft,
                onRestart = { id, date -> onRestart(id,date) },
                onClick =  { id, date, goalDone -> onClick(id,date,goalDone)}
            )
        }
    }

}