package aeb.proyecto.addhabit.components.typeHabit

import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.components.card.CardLeadingIconButton
import aeb.proyecto.addhabit.components.textField.AddHabitTextField
import aeb.proyecto.addhabit.components.textField.TrailingIcon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun RecurringTypeHabit(
    modifier:Modifier = Modifier,
    focusManager:FocusManager,
    intervalTextFieldState: TextFieldState,
    color:Color,
    date:LocalDate,
    onClick:() -> Unit = {}
){

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
                    .fillMaxWidth(0.6f),
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