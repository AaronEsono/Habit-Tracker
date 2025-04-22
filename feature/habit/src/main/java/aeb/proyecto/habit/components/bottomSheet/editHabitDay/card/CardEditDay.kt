package aeb.proyecto.habit.components.bottomSheet.editHabitDay.card

import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CardEditDay(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    ElevatedCard(
        shape = RoundedCornerShape(spacing8),
        modifier = modifier.border(
            width = spacing2,
            color = MaterialTheme.colorScheme.outline,
            shape = RoundedCornerShape(spacing8)
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row (
            modifier = Modifier.padding(vertical = spacing6, horizontal = spacing8),
            verticalAlignment = Alignment.CenterVertically
        ){
            content()
        }
    }
}