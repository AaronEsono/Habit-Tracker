package aeb.proyecto.login.components.button

import aeb.proyecto.login.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(spacing8),
    onClick: () -> Unit = {}
){

    CustomRipple {
        ElevatedButton(
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            border = if(enabled) BorderStroke(spacing1, MaterialTheme.colorScheme.onSurface) else null,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = modifier
        ) {
            LabelLargeText(stringResource(R.string.login_accept))
        }
    }

}