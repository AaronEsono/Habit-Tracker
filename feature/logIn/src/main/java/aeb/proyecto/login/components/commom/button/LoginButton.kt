package aeb.proyecto.login.components.commom.button

import aeb.proyecto.login.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
) {

    CustomRipple (color = MaterialTheme.colorScheme.inverseOnSurface){
        ElevatedButton(
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            contentPadding = PaddingValues(vertical = spacing12),
            modifier = modifier
        ) {
            Text(stringResource(R.string.login_accept), style = MaterialTheme.typography.labelLarge)
        }
    }

}