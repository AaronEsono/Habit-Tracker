package aeb.proyecto.addhabit.components.common.divider

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A tailored structural layout separator component wrapping the system [HorizontalDivider].
 * Provides predefined architectural defaults to establish consistent boundary dividers
 * between form sections while adhering strictly to the active application design system.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param thickness The physical vertical stroke width dimension of the line. Defaults to [1.dp].
 * @param color The design system [Color] token used to paint the divider line stroke. Defaults to outline container profiles.
 */
@Composable
fun CustomHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.outline
){
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}