package aeb.proyecto.habit.components.bottomSheet.editHabitDay.button

import aeb.proyecto.ui.dimmens.Dimmens
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ButtonEditDay(
    modifier:Modifier = Modifier,
    text:String,
    onClick:(String) -> Unit
){

    CustomRipple {
        ElevatedButton(
            modifier = modifier,
            onClick = {onClick(text)},
            shape = RoundedCornerShape(spacing8),
            border = BorderStroke(1.dp,MaterialTheme.colorScheme.outline),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            LabelLargeText(text)
        }
    }

}