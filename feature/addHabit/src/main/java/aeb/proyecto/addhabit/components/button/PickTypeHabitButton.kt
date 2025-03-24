package aeb.proyecto.addhabit.components.button

import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.BodyMediumText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PickTypeHabitButton(
    modifier: Modifier = Modifier,
    title:String = "",
    subtitle:String = "",
    onClick: () -> Unit = {}
){

    CustomRipple {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(spacing8),
            border = BorderStroke(spacing2, MaterialTheme.colorScheme.onSurface)
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ){
                BodyMediumText(title, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.padding(vertical = spacing2))

                LabelMediumText(subtitle)
            }
        }
    }

}