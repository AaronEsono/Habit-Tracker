package aeb.proyecto.habittracker.ui.components.card

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CardImport(
    shape: Shape = RoundedCornerShape(spacing8),
    defaultElevation: Dp = spacing8,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    modifier: Modifier = Modifier,
    modifierRow: Modifier = Modifier
        .fillMaxWidth()
        .padding(spacing8),
    content: @Composable () -> Unit
) {
    Card(
        shape = shape,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = defaultElevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(modifier = modifierRow, verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(R.drawable.ic_file),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(40.dp)
            )
            content()
        }
    }
}