package aeb.proyecto.habit.components.bottomSheet.editHabitDay.card

import aeb.proyecto.habit.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun TimerCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
){
    CustomRipple {
        ElevatedCard(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(spacing8),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Row (
                modifier = Modifier
                    .border(
                        width = spacing2,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(spacing8)
                    )
                    .padding(vertical = spacing8, horizontal = spacing8),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    Icons.Filled.Timer,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = ""
                )

                LabelLargeText(
                    stringResource(R.string.habit_timer),
                    modifier = Modifier.padding(start = spacing4)
                )
            }
        }
    }
}