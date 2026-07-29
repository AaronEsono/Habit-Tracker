package aeb.proyecto.addhabit.components.common.card

import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.BodyMediumText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/**
 * An interactive, elevated navigational row button styled as a full-width container tile.
 * Displays a descriptive title aligned to the left edge and an auto-mirrored forward indicator arrow
 * pushed to the right boundary, commonly used to route users toward deep configuration parameter dialogs.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param title The text string printed on the leading edge of the tile identifying its destination or parameter state.
 * @param onClick Interactive action lambda execution trigger dispatched when the card layout is pressed.
 */
@Composable
fun AddHabitCardButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {}
) {

    CustomRipple {
        ElevatedCard(
            onClick = onClick,
            modifier = modifier.testTag("add_habit_card_button"),
            shape = RoundedCornerShape(spacing8),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = spacing12, vertical = spacing12),
                horizontalArrangement = Arrangement.Center
            ) {

                BodyMediumText(title)

                // Elastic spacing spacer to dynamically drive the forward arrow icon onto the trailing boundary edge
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "icon card button",
                    modifier = Modifier.size(18.dp)
                )

            }
        }
    }
}