package aeb.proyecto.habit.components.common.bottomSheet.configureHabit.button

import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * High-performance tactical button atom tailored for quantitative increment panels inside bottom sheets.
 * Encapsulates customized ripple effects and Material 3 design constraints, passing its visible textual
 * buffer downstream upon interaction to allow centralized parent orchestration.
 *
 * @param modifier Structural Modifier ecosystem parameters applied directly over the host button frame.
 * @param text The visual alphanumeric token label displayed inside the layout container.
 * @param onClick Intercepting action callback loop; delivers the button's underlying string token on click.
 */
@Composable
fun ButtonConfigureHabit(
    modifier:Modifier = Modifier,
    text:String,
    onClick:(String) -> Unit
){

    // Encapsulate structural touch feedback using the custom platform ripple signature
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
            // Emits standard high-hierarchy typographical labels natively
            LabelLargeText(text)
        }
    }

}