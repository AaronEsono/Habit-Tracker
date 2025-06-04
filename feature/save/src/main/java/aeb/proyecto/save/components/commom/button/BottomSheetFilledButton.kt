package aeb.proyecto.save.components.commom.button

import aeb.proyecto.save.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.TitleSmallText
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun BottomSheetFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    CustomRipple (color = MaterialTheme.colorScheme.surfaceVariant){
        Button(
            shape = RoundedCornerShape(spacing12),
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            contentPadding = PaddingValues(vertical = spacing12),
            onClick = onClick
        ) {
            Row {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Icon Check button",
                    tint = MaterialTheme.colorScheme.inverseSurface
                )

                Spacer(modifier = Modifier.padding(horizontal = spacing2))

                TitleSmallText(
                    stringResource(R.string.save_bottom_sheet_acept),
                    color = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }
    }

}