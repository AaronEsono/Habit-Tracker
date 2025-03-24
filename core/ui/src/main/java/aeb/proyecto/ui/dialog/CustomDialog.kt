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