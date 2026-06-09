package aeb.proyecto.habit.components.common.bottomSheet.configureHabit.screen.incompleteCases

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.card.TimerCard
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.rowButton.RowButton
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.utils.isValidInput
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.button.ButtonConfigureHabit
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.textField.TextFieldConfigureHabit
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.model.classes.listTime
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.regexTextField.IsOnlyDigit
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Specialized input viewport for quantitative-based habit progression tracking.
 * Provides a singular, high-precision text entry field coupled with rapid-fill
 * milestone buttons for efficient daily log updates.
 *
 * @param habitWithDay Target habit entity and associated day context.
 * @param leftTimes Remaining quantitative quantity to fulfill the habit goal.
 * @param halfTimesLeft Calculated 50% milestone target for rapid progression entry.
 * @param onRestart Dispatches a reset event.
 * @param onClickTimer Callback for external chronometer module navigation.
 * @param onClick Commits the quantitative progress value to the database.
 */
@Composable
fun UnitIncompleteMode(
    habitWithDay: HabitWithDay,
    leftTimes:BigDecimal,
    halfTimesLeft:BigDecimal,
    onRestart:(id:Long,date: LocalDate) -> Unit,
    onClickTimer:(Triple<Long,String,BigDecimal>) -> Unit = {},
    onClick:(id:Long, date: LocalDate, goalDone:BigDecimal) -> Unit
){

    val habit = remember (habitWithDay){
        habitWithDay.habit
    }

    val day = remember (habitWithDay){
        habitWithDay.day
    }

    val textFieldState = rememberTextFieldState(initialText = "1")
    IsOnlyDigit(textFieldState,habit.unit)

    val focusManager = LocalFocusManager.current

    val unitInListTime = remember {
        habit.unit in listTime
    }

    // Header label indicating remaining quantitative balance
    LabelLargeText(
        stringResource(
            R.string.habit_edit_habit_day_times_left,
            stringResource(habit.unit.titlePlural),
            leftTimes.toPlainString()
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing2),
        fontSize = 15.sp
    )

    // Optional Chronometer anchor
    if(unitInListTime){
        Row (
            modifier = Modifier.fillMaxWidth().padding(top = spacing10),
            horizontalArrangement = Arrangement.Center
        ){
            TimerCard(
                onClick = {onClickTimer(Triple(habit.id,day.date.toString(),leftTimes))}
            )
        }
    }

    // Rapid input helper buttons (Smart-Fill)
    Row (
        modifier = Modifier.fillMaxWidth().padding(top = spacing10, start =
            spacing20, end = spacing20
        ),
        verticalAlignment = Alignment.CenterVertically
    ){

        ButtonConfigureHabit(
            modifier = Modifier.weight(1f),
            text = halfTimesLeft.toPlainString()
        ) {
            textFieldState.edit { replace(0,length,halfTimesLeft.toPlainString()) }
        }

        ButtonConfigureHabit(
            modifier = Modifier.weight(1f).padding(start = spacing12),
            text = leftTimes.toPlainString()
        ) {
            textFieldState.edit { replace(0,length,leftTimes.toPlainString())}
        }
    }

    // Quantitative input field
    TextFieldConfigureHabit(
        modifier = Modifier.padding(top = spacing10, bottom = spacing4),
        textFieldState = textFieldState,
        focusManager = focusManager,
    )

    // Final transactional commit controls
    RowButton(
        isEnabled = isValidInput(textFieldState.text.toString()),
        color = Color(habit.color),
        onClick = { onClick(habit.id, day.date, textFieldState.text.toString().toBigDecimal())},
        onClickRestart = { onRestart(habit.id, day.date) }
    )
}