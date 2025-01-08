package aeb.proyecto.habittracker.ui.components.card

import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CardPickUnitAddHabit(
    modifier: Modifier = Modifier,
    modifierRow: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    thick: Dp = 0.dp,
    defaultElevation: Dp = 0.dp,
    colorCardBorder: Color = MaterialTheme.colorScheme.onSurface,
    shape: RoundedCornerShape = RoundedCornerShape(spacing8),
    onClick: () -> Unit = {},
    unit: Constans.Units,
) {

    Card(
        shape = shape,
        modifier = modifier
            .wrapContentWidth()
            .clickable { onClick() }
            .border(thick, colorCardBorder, shape),
        elevation = CardDefaults.cardElevation(
            defaultElevation = defaultElevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = modifierRow
                .height(40.dp)
                .wrapContentWidth()
                .padding(horizontal = spacing8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(unit.icon),
                "",
                modifier = Modifier.size(25.dp),
                tint = iconTint
            )

            Spacer(modifier = Modifier.padding(horizontal = spacing4))

            LabelMediumText(stringResource(unit.title))
        }
    }
}