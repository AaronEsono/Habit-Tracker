package aeb.proyecto.habittracker.ui.components.card

import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp

@Composable
fun CardInfoAddHabit(
    shape: Shape = RoundedCornerShape(spacing8),
    defaultElevation: Dp = spacing8,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    modifierCard: Modifier = Modifier,
    modifierRow: Modifier = Modifier,
    modifierFinalIcon: Modifier = Modifier,
    alignText: TextAlign = TextAlign.Left,
    colorStartIcon: Color = MaterialTheme.colorScheme.onSurface,
    colorFinalIcon:Color =  MaterialTheme.colorScheme.onSurface,
    icon: ImageVector? = null,
    finalIcon: ImageVector? = null,
    title: String
) {

    Card(
        shape = shape,
        modifier = modifierCard,
        elevation = CardDefaults.cardElevation(
            defaultElevation = defaultElevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = modifierRow,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            icon?.let {
                Icon(icon, "startIcon", tint = colorStartIcon)
            }

            Spacer(modifier = Modifier.padding(horizontal = spacing8))

            TitleSmallText(title, Modifier.weight(1f), alignText)

            finalIcon?.let {
                Icon(
                    finalIcon,
                    "endIcon",
                    tint = colorFinalIcon,
                    modifier = modifierFinalIcon
                )
            }
        }
    }
}