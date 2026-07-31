package aeb.proyecto.login.components.common.button

import aeb.proyecto.login.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * Social authentication button for Google sign-in integration.
 * Maintains brand consistency with the application's design system while
 * incorporating the distinct Google visual identity.
 *
 * @param modifier Structural Modifier parameters to be applied to the button.
 * @param onClick Execution callback for the Google OAuth sign-in flow.
 */
@Composable
fun LoginGoogleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){

    // Custom ripple wrapper for brand-specific touch feedback
    CustomRipple (color = MaterialTheme.colorScheme.inverseOnSurface){
        ElevatedButton(
            onClick = onClick,
            modifier = modifier,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(spacing4)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                // Social provider branding icon
                Image(painterResource(R.drawable.im_google),
                    contentDescription = "Google image",
                    modifier = Modifier.size(35.dp))

                Spacer(modifier = Modifier.padding(horizontal = spacing4))

                // Action label
                LabelLargeText(
                    stringResource(R.string.login_google),
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                )
            }
        }
    }

}