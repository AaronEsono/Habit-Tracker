package aeb.proyecto.habit.components.common.bottomSheet.configureHabit.card

import aeb.proyecto.habit.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
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

/**
 * Interactive elevation card atom designed to link habit configuration sheets with the Timer module.
 * Embeds the platform standard Material Design timer vector asset next to localized typography labels,
 * ensuring absolute sizing parity and border layout alignment with companion bottom-sheet components.
 *
 * @param modifier Structural Modifier ecosystem parameters applied directly over the host card frame.
 * @param onClick Forwarding routing callback loop executed instantly when the user taps the card canvas.
 */
@Composable
fun TimerCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
){
    // Apply centralized platform touch ripple mechanics
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
                // System contextual vector tracking anchor
                Icon(
                    Icons.Filled.Timer,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = ""
                )

                // High-hierarchy standardized typographical labe
                LabelLargeText(
                    stringResource(R.string.habit_timer),
                    modifier = Modifier.padding(start = spacing4)
                )
            }
        }
    }
}