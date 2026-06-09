package aeb.proyecto.habit.components.common.bottomSheet.configureHabit.screen.incompleteCases

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.card.TimerCard
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.rowButton.RowButton
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.utils.isHourInputValid
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.utils.passToHour
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.button.ButtonConfigureHabit
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.textField.TextFieldConfigureHabit
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.listTime
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.regexTextField.IsOnlyDigit
import aeb.proyecto.ui.regexTextField.IsOnlyZeroTo59
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Specialized input viewport for temporal-based habit progression tracking.
 * Provides granular HH:mm or mm:ss entry controls with smart-fill helper buttons.
 *
 * @param habitWithDay Target habit entity and associated day context.
 * @param leftTimes Remaining temporal duration to fulfill the habit goal.
 * @param halfTimesLeft Calculated 50% milestone target for rapid progression entry.
 * @param onRestart Dispatches a reset event.
 * @param onClickTimer Callback for external chronometer module navigation.
 * @param onClick Commits the time-based progress conversion to the database.
 */
@Composable
fun HourIncompleteMode(
    habitWithDay: HabitWithDay,
    leftTimes: BigDecimal,
    halfTimesLeft:BigDecimal,
    onRestart:(id:Long,date: LocalDate) -> Unit,
    onClickTimer:(Triple<Long,String,BigDecimal>) -> Unit = {},
    onClick:(id:Long, date: LocalDate, goalDone:BigDecimal) -> Unit
){

    val habit = remember(habitWithDay){
        habitWithDay.habit
    }

    val day = remember (habitWithDay){
        habitWithDay.day
    }

    val leftTimesToHour = remember { passToHour(leftTimes, habit.unit) }
    val halfTimesLeftToHour = remember { passToHour(halfTimesLeft, habit.unit) }

    val focusManager = LocalFocusManager.current

    val firstTextFieldState = rememberTextFieldState(initialText = "1")
    val secondTextFieldState = rememberTextFieldState()

    // Enforce input sanitization constraints
    IsOnlyDigit(firstTextFieldState)
    IsOnlyZeroTo59(secondTextFieldState)

    val unitInListTime = remember {
        habit.unit in listTime
    }

    // Header label indicating remaining time
    LabelLargeText(
        stringResource(
            R.string.habit_edit_habit_day_times_left,
            stringResource(habit.unit.titlePlural),
            leftTimesToHour
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing2)
    )

    // Optional Chronometer anchor
    if(unitInListTime){
        Row (
            modifier = Modifier.fillMaxWidth().padding(top = spacing10),
            horizontalArrangement = Arrangement.Center
        ){
            TimerCard(
                onClick = { onClickTimer(Triple(habit.id,day.date.toString(),leftTimes)) }
            )
        }
    }

    // Rapid input helper buttons (Smart-Fill)
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = spacing10, start =
                    spacing20, end = spacing20
            ),
        verticalAlignment = Alignment.CenterVertically
    ){

        ButtonConfigureHabit(
            modifier = Modifier.weight(1f),
            text = halfTimesLeftToHour
        ) {
            firstTextFieldState.edit { replace(0,length,halfTimesLeftToHour.split(":")[0]) }
            secondTextFieldState.edit { replace(0,length,halfTimesLeftToHour.split(":")[1]) }
        }

        Spacer(modifier = Modifier.padding(horizontal = spacing12))

        ButtonConfigureHabit(
            modifier = Modifier
                .weight(1f),
            text = leftTimesToHour
        ) {
            firstTextFieldState.edit { replace(0,length,leftTimesToHour.split(":")[0]) }
            secondTextFieldState.edit { replace(0,length,leftTimesToHour.split(":")[1]) }
        }
    }

    // Dual-column numeric input field block
    Row (
        Modifier
            .fillMaxWidth()
            .padding(top = spacing10, bottom = spacing4, start = spacing20, end = spacing20),
        verticalAlignment = Alignment.CenterVertically
    ){

        Column (
            modifier = Modifier.weight(1f)
        ){
            TextFieldConfigureHabit(
                modifier = Modifier.height(50.dp),
                textFieldState = firstTextFieldState,
                focusManager = focusManager,
                imeAction = ImeAction.Next,
                contentPadding = PaddingValues(horizontal = spacing12)
            )

            LabelSmallText(
                stringResource(habit.unit.titlePlural),
                color = MaterialTheme.colorScheme.outline
            )
        }

        LabelMediumText(
            stringResource(R.string.habit_dots),
            modifier = Modifier
                .padding(horizontal = spacing4)
                .offset(y = (-10).dp),
            fontSize = 40.sp)

        Column (
            modifier = Modifier.weight(1f)
        ){
            TextFieldConfigureHabit(
                modifier = Modifier.height(50.dp),
                textFieldState = secondTextFieldState,
                focusManager = focusManager,
                placeholder = {
                    Text(
                        stringResource(R.string.habit_zero),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                contentPadding = PaddingValues(horizontal = spacing12)
            )

            LabelSmallText(
                stringResource(
                    if (habit.unit == UnitHabit.HOURS) {
                        UnitHabit.MINUTES.titlePlural
                    } else {
                        UnitHabit.SECONDS.titlePlural
                    }
                ),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }

    // Final transactional commit controls
    RowButton(
        isEnabled = isHourInputValid(firstTextFieldState,secondTextFieldState),
        color = Color(habit.color),
        onClick = { onClick(habit.id, day.date,
            convertToBigDecimal(firstTextFieldState, secondTextFieldState, habit.unit)
        )},
        onClickRestart = { onRestart(habit.id, day.date) }
    )
}

/**
 * Normalizes dual-field human-readable time inputs into a singular atomic base unit (seconds).
 * Acts as the data bridge between the UI-layer inputs and the domain-layer persistence model.
 * * @param firstTextFieldState The leading time field (Hours or Minutes depending on [unitHabit]).
 * @param secondTextFieldState The trailing time field (Minutes or Seconds depending on [unitHabit]).
 * @param unitHabit The configuration unit context determining the multiplier logic.
 * @return A unified [BigDecimal] representing the total duration in atomic seconds.
 */
fun convertToBigDecimal(firstTextFieldState: TextFieldState, secondTextFieldState: TextFieldState, unitHabit: UnitHabit): BigDecimal {
    val first = firstTextFieldState.text.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO
    val second = secondTextFieldState.text.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO

    return when(unitHabit){
        UnitHabit.HOURS -> {
            // Hours to seconds: (h * 3600) + (m * 60)
            first.multiply(BigDecimal(3600)).add(second.multiply(BigDecimal(60)))
        }
        UnitHabit.MINUTES -> {
            // Minutes to seconds: (m * 60) + s
            first.multiply(BigDecimal(60)).add(second)
        }
        else -> {BigDecimal.ZERO}
    }
}