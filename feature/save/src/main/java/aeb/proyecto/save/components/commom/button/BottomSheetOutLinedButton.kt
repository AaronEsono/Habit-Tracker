package aeb.proyecto.save.components.commom.button

import aeb.proyecto.save.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.TitleSmallText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource


@Composable
fun BottomSheetOutLinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){

    CustomRipple (){
        OutlinedButton(
            shape = RoundedCornerShape(spacing12),
            modifier = modifier,
            contentPadding = PaddingValues(vertical = spacing12),
            onClick = onClick,
            border = BorderStroke(spacing2, MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            Row {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "Icon Cancel button",
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.padding(horizontal = spacing2))

                TitleSmallText(
                    stringResource(R.string.save_bottom_sheet_cancel),
                )
            }
        }
    }

}