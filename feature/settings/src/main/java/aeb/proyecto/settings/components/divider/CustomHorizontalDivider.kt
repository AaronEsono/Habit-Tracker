package aeb.proyecto.settings.components.divider

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun CustomHorizontalDivider(
    modifier : Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outline,
    thickness: Dp = DividerDefaults.Thickness
){

    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = thickness
    )

}