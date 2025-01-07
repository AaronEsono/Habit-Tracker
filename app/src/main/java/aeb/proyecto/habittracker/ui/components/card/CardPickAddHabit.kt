package aeb.proyecto.habittracker.ui.components.card

import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing6
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CardPickColorAddHabit(
    @StringRes text: Int,
    modifierRow: Modifier = Modifier.wrapContentSize(),
    modifierCard: Modifier = Modifier.wrapContentSize(),
    defaultElevation: Dp = spacing8,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    shape: Shape = RoundedCornerShape(spacing8),
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = modifierRow,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            shape = shape,
            modifier = modifierCard
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(
                defaultElevation = defaultElevation
            ),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            )
        ) {
            Icon(
                icon, contentDescription = "",
                tint = color,
                modifier = Modifier
                    .padding(vertical = spacing4, horizontal = spacing6)
                    .size(35.dp)
            )
        }

        Spacer(modifier = Modifier.padding(horizontal = spacing4))

        BodyMediumText(stringResource(text))
    }

}