package aeb.proyecto.habit.components.bottomSheet.editHabitDay.screen

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.button.ButtonEditDay
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.textField.TextFieldEditHabit
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils.isValidInput
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
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
    timesLeft: BigDecimal,
    halfTimesLeft: BigDecimal,
    textFieldState: TextFieldState,
    focusManager: FocusManager,
    coroutineScope: CoroutineScope,
    onDismiss: () -> Unit = {},
    sheetState: SheetState,
    onRestart:(id:Long,date:LocalDate) -> Unit,
    onClick:(id:Long, date: LocalDate, goalDone:BigDecimal) -> Unit
){

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

    LabelLargeText(
        stringResource(
            R.string.habit_edit_habit_day_times_left,
            stringResource(habit.unit.titlePlural),
            timesLeft.toPlainString()
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing2),
        fontSize = 15.sp
    )

    //** Unidades para el usuario*/
    Row (
        modifier = Modifier.fillMaxWidth().padding(top = spacing12, start =
            spacing20, end = spacing20
        ),
        verticalAlignment = Alignment.CenterVertically
    ){

        ButtonEditDay(
            modifier = Modifier.weight(1f),
            text = halfTimesLeft.toPlainString()
        ) {
            textFieldState.edit { replace(0,length,halfTimesLeft.toPlainString()) }
        }

        ButtonEditDay(
            modifier = Modifier.weight(1f).padding(start = spacing12),
            text = timesLeft.toPlainString()
        ) {
            textFieldState.edit { replace(0,length,timesLeft.toPlainString())}
        }
    }

    /** Introducción de unidades */
    TextFieldEditHabit(
        modifier = Modifier.padding(top = spacing10, bottom = spacing4),
        textFieldState = textFieldState,
        focusManager = focusManager,
    )

    /** Botones */
    Row (
        modifier = Modifier.fillMaxWidth().padding(bottom = spacing12, top = spacing10),
        verticalAlignment = Alignment.CenterVertically
    ){

        CustomRipple {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    coroutineScope.launch {
                        onRestart(habit.id,habitDay.date)
                        sheetState.hide()
                        onDismiss()
                    }
                },
                shape = RoundedCornerShape(spacing8),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                LabelLargeText(
                    stringResource(R.string.habit_restart),
                    color = MaterialTheme.colorScheme.inverseOnSurface)
            }
        }

        CustomRipple {
            Button(
                modifier = Modifier.padding(start = spacing10).weight(1f),
                onClick = {
                    coroutineScope.launch {
                        onClick(habit.id,habitDay.date, BigDecimal(textFieldState.text.toString()))
                        sheetState.hide()
                        onDismiss()
                    }
                },
                enabled = isValidInput(textFieldState.text.toString()),
                shape = RoundedCornerShape(spacing8),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(habit.color)
                )
            ) {
                LabelLargeText(
                    stringResource(R.string.habit_accept),
                    color = getContrastColor(Color(habit.color))
                )
            }
        }
    }
}