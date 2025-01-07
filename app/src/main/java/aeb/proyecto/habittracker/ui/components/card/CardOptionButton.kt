package aeb.proyecto.habittracker.ui.components.card

import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CardOptionButton(
    modifierCard: Modifier = Modifier,
    modifierRow: Modifier = Modifier.fillMaxWidth().padding(spacing12),
    modifierIcon: Modifier = Modifier.size(25.dp),
    modifierText: Modifier = Modifier.padding(start = spacing12),
    elevation: Dp = spacing8,
    shape: Shape = RectangleShape,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    tintStartIcon: Color = MaterialTheme.colorScheme.onSurface,
    tintEndIcon: Color = MaterialTheme.colorScheme.onSurface,
    @DrawableRes startIcon: Int,
    endIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowRight,
    @StringRes title: Int,
    onClick: () -> Unit,
) {

    Card(
        shape = shape,
        modifier = modifierCard
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = modifierRow,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = startIcon),
                contentDescription = "startIcon",
                modifier = modifierIcon,
                tint = tintStartIcon
            )

            LabelMediumText(
                text = stringResource(title),
                modifier = modifierText
                    .weight(1f),
                textAlign = TextAlign.Start
            )

            Icon(endIcon, "endIcon", tint = tintEndIcon)
        }
    }
}