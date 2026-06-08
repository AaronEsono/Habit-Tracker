package aeb.proyecto.addhabit.utils

import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.unitsHourMode
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Dynamically resolves and evaluates the appropriate localized text string representation (singular or plural)
 * for a designated tracking unit based on the current active value in the text input buffers.
 *
 * Separates evaluation branches between standard numeric metric formats and dedicated hour-split tracking structures.
 *
 * @param timeTextField The reactive input state buffer tracker monitoring generic discrete counts.
 * @param firstHourTimesTextField The temporal hour text input state buffer tracker monitoring metric thresholds.
 * @param typeUnit The active measurement classification parameter [UnitHabit] defining the context labels.
 * @return The localized, context-aware string literal resource corresponding to singular or plural boundaries.
 */
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