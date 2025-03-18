package aeb.proyecto.login.components.button

import aeb.proyecto.login.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun LoginGoogleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){

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
                Image(painterResource(R.drawable.ic_google), contentDescription = "Google image")

                Spacer(modifier = Modifier.padding(horizontal = spacing2))

                LabelLargeText(
                    stringResource(R.string.login_google),
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                )
            }
        }
    }

}