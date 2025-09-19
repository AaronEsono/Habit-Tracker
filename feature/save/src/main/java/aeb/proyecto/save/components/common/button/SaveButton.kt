package aeb.proyecto.save.components.common.button

import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.TitleSmallText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SaveButton(
    modifier:Modifier = Modifier,
    title:String,
    onClick: () -> Unit = {},
){

    CustomRipple{
        ElevatedButton(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            border = BorderStroke(spacing1, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(spacing12),
            contentPadding = PaddingValues(vertical = spacing12, horizontal = spacing8),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            TitleSmallText(title, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }

}