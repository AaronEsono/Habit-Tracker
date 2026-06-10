package aeb.proyecto.login.components.common.button

import aeb.proyecto.login.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.ripple.CustomRipple
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

/**
 * A custom filled button component specifically designed for modal bottom sheets
 * within the authentication flow.
 *
 * It features a custom ripple effect, standardized corner radius, and consistent
 * padding, ensuring a professional look that matches the app's design system.
 *
 * @param modifier The [Modifier] to be applied to the button layout.
 * @param onClick The action to be performed when the button is clicked.
 * @param isEnabled Determines whether the button can be interacted with.
 * @param title The string resource ID for the button label.
 */
@Composable
fun BottomSheetFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true,
    @StringRes title: Int = R.string.login_accept
){

    CustomRipple (color = MaterialTheme.colorScheme.surfaceVariant){
        Button(
            shape = RoundedCornerShape(spacing12),
            modifier = modifier,
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            contentPadding = PaddingValues(vertical = spacing12),
            onClick = onClick
        ) {
            Row {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Icon Check button",
                    tint = if(isEnabled) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.padding(horizontal = spacing2))

                Text(
                    stringResource(title),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}