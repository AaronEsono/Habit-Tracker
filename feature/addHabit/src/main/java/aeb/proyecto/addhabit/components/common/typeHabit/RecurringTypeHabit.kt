package aeb.proyecto.addhabit.components.common.typeHabit

import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.components.common.card.CardLeadingIconButton
import aeb.proyecto.addhabit.components.common.textField.AddHabitTextField
import aeb.proyecto.addhabit.components.common.textField.TrailingIcon
import aeb.proyecto.ui.date.utils.getDay
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.month.getMonth
import aeb.proyecto.ui.regexTextField.IsOnlyDigit
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate

/**
 * A specialized layout row tailored to configure custom rolling interval habit iterations.
 * Pairs a localized calendar trigger card (60% width layout block) with a numerical sequence
 * text track field to let users define precise rolling frequency boundaries.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param focusManager The parent directional coordinator pipeline tracking active viewport input nodes.
 * @param intervalTextFieldState Asynchronous backing text state buffer holding the physical cyclic gap integer characters.
 * @param color The primary active design [Color] token representation allocated to paint functional asset nodes.
 * @param date The underlying [LocalDate] timestamp snapshot representing the calculated baseline cycle start day.
 * @param onClick Interactive action callback lambda targeted to inflate date picker overlay sheets.
 */
@Composable
fun RecurringTypeHabit(
    modifier:Modifier = Modifier,
    focusManager:FocusManager,
    intervalTextFieldState: TextFieldState,
    color:Color,
    date:LocalDate,
    onClick:() -> Unit = {}
){

    // Intercept input variations defensively to ensure only digital numbers populate the state buffer
    IsOnlyDigit(intervalTextFieldState)

    Column(
        modifier = modifier
    ){
        LabelMediumText(stringResource(R.string.add_habit_recurring_title_date),
            modifier = Modifier.fillMaxWidth())

        Row(
            modifier = modifier.fillMaxWidth()
        ){

            CardLeadingIconButton(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .testTag("add_habit_recurring_date"),
                leadingIcon = Icons.Filled.DateRange,
                color = color,
                title = getDate(date),
                onClick = onClick
            )

            Spacer(modifier = Modifier.padding(horizontal = spacing4))

            AddHabitTextField(
                textFieldState = intervalTextFieldState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(top = spacing2),
                focusManager = focusManager,
                trailingIcon = { TrailingIcon(intervalTextFieldState) },
                contentPadding = PaddingValues(start = spacing12),
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
        }
    }

}

/**
 * Transforms an isolated [LocalDate] snapshot instance into a fully localized, human-readable layout string.
 * Extracts string references using structural key tokens to maintain grammar accuracy across system language shifts.
 *
 * @param localDate The physical time coordinate model parameter targeted for structural parsing.
 * @return A completely assembled localized date string template.
 */
@Composable
fun getDate(localDate: LocalDate):String{
    val day = localDate.dayOfWeek.name
    val month = localDate.month.value
    val dayMonth = localDate.dayOfMonth

    return stringResource(R.string.add_habit_recurring_date,
        stringResource(getDay(day)),
        stringResource(getMonth(month)),
        dayMonth)
}