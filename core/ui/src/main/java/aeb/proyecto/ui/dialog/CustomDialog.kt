package aeb.proyecto.ui.dialog

import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.Dialog

/**
 * Reusable architectural dialogue window wrapper abstraction over the platform [Dialog] infrastructure.
 * Encases the bare dialog canvas inside an elevated Material 3 [Card] structure to guarantee unified
 * shape radiuses, elevations, and background canvas containment layouts across modal interaction screens.
 *
 * @param modifier The structural composition modifier layout adjustment token.
 * @param shape The geometric boundary outline pattern applied to the container, defaults to an 8dp corner radius.
 * @param containerColor The surface background color bound by default to the active semantic card system tokens.
 * @param onDismissRequest The programmatic callback closure triggered when the user clicks outside the modal boundary or presses back.
 * @param content The declarative architectural sub-view dialog layout injected into the card canvas slot.
 */
@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(spacing8),
    containerColor: Color = CardDefaults.cardColors().containerColor,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = modifier,
            shape = shape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = spacing8
            ),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            )
        ) {
            content()
        }
    }
}