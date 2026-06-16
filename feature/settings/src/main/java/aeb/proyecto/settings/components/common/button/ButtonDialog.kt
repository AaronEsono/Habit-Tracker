package aeb.proyecto.settings.components.common.button

import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.BodyMediumText
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A customized Elevated button specifically for dialog options.
 * Fixed width of 150.dp ensures consistency in selection grids.
 *
 * @param modifier Modifier for external adjustments.
 * @param containerColor Background color, usually determined by [getSelectionContainerColor].
 * @param paddingValues Internal spacing for the button content.
 * @param onClick Action triggered on button press.
 * @param content The label/text or icon to display inside.
 */
@Composable
fun ButtonDialog(
    modifier:Modifier = Modifier,
    containerColor:Color = MaterialTheme.colorScheme.background,
    paddingValues: PaddingValues =  ButtonDefaults.ContentPadding,
    onClick: () -> Unit,
    content: @Composable () -> Unit = {}
) {
    CustomRipple{
        ElevatedButton(
            modifier = modifier.width(150.dp),
            onClick = { onClick() },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = containerColor,
            ),
            shape = RoundedCornerShape(spacing12),
            contentPadding = paddingValues,
        ) {
            content()
        }
    }
}

/**
 * Standardized body text component for buttons inside dialogs.
 * Ensures consistent padding, center alignment, and ellipsis for long labels.
 */
@Composable
fun BodyMediumTextButtonDialog(text:String){
    BodyMediumText(
        text = text,
        modifier = Modifier.padding(horizontal = spacing8, vertical = spacing2),
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}