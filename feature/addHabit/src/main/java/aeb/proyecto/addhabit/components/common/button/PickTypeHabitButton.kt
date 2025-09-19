package aeb.proyecto.addhabit.components.common.button

import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.BodyMediumText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
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
            border = BorderStroke(spacing2, MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ){
                BodyMediumText(title, fontWeight = FontWeight.Bold)
                LabelMediumText(subtitle)
            }
        }
    }

}