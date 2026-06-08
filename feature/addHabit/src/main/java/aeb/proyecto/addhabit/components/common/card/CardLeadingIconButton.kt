package aeb.proyecto.addhabit.components.common.card

import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * An enriched interactive container tile featuring a contextual leading graphic asset icon.
 * Displays a descriptive text label centered alongside the leading anchor, utilizing an elastic spacer
 * to align an auto-mirrored directional arrow indicator flush against the trailing layout edge.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param leadingIcon The primary illustrative [ImageVector] asset pinned to the starting edge of the tile layer.
 * @param title The text content printed next to the leading icon identifying the selection target or option context.
 * @param color Personalized design [Color] token representation applied as a tint over the leading vector icon.
 * @param onClick Interactive action lambda execution trigger dispatched when the card layout is pressed.
 */
@Composable
fun CardLeadingIconButton(
    modifier:Modifier = Modifier,
    leadingIcon:ImageVector,
    title:String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick:() -> Unit = {}
){

    CustomRipple {
        ElevatedCard(
            modifier = modifier,
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(spacing8)
        ) {
            Row (
                modifier = Modifier.padding(horizontal = spacing8, vertical = spacing12),
                verticalAlignment = Alignment.CenterVertically
            ){

                Icon(
                    leadingIcon,
                    contentDescription = "Icon Leading",
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )

                LabelLargeText(title, modifier = Modifier.padding(start = spacing8))

                // Elastic layout spacer pushing the navigation chevron to the absolute trailing margin bounds
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Icon Arrow Forward",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }

}