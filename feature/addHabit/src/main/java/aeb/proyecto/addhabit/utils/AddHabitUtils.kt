package aeb.proyecto.addhabit.utils

import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.unitsHourMode
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun getTextUnits(timeTextField: TextFieldState, firstHourTimesTextField: TextFieldState, typeUnit: UnitHabit):String{
    return when{
        typeUnit !in unitsHourMode ->{
            if(timeTextField.text.toString() == "1") stringResource(typeUnit.title)
            else stringResource(typeUnit.titlePlural)
        }
        else -> {
            if(firstHourTimesTextField.text.toString() == "1") stringResource(typeUnit.title)
            else stringResource(typeUnit.titlePlural)
        }
    }
}