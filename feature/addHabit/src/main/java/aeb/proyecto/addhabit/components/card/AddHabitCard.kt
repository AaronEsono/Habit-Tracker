package aeb.proyecto.addhabit.components.card

import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.BodyLargeText
import aeb.proyecto.ui.text.BodyMediumText
import aeb.proyecto.ui.text.LabelLargeText
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
import androidx.compose.material3.ElevatedCard
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
fun AddHabitCard(
    modifier: Modifier = Modifier,
    title:String = "",
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit = {}
){

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CustomRipple {
            ElevatedCard(
                shape = RoundedCornerShape(spacing8),
                modifier = Modifier
                    .clip(RoundedCornerShape(spacing8))
                    .clickable { onClick() },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Icon(
                    icon, contentDescription = "icon card",
                    tint = color,
                    modifier = Modifier
                        .padding(vertical = spacing4, horizontal = spacing6)
                        .size(35.dp)
                )
            }
        }

        Spacer(modifier = Modifier.padding(horizontal = spacing4))

        BodyMediumText(title)
    }

}