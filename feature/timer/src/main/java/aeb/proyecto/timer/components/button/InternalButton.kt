package aeb.proyecto.timer.components.button

import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.repeatingClick.repeatingClickable
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun InternalSegmentedButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Add,
    size: Dp = 44.dp,
    onClickListener: () -> Unit
){
    Box (
        modifier = modifier
            .size(size)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(spacing12))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f))
            .repeatingClickable(
                interactionSource = remember { MutableInteractionSource() },
                maxDelayMillis = 500,
                minDelayMillis = 50,
                delayDecayFactor = .20f,
                onClick = {  onClickListener() }
            )
    ){
        Icon(
            icon,
            contentDescription = "Icon rest set",
            tint = MaterialTheme.colorScheme.inverseOnSurface,
            modifier = Modifier.align(Alignment.Center).fillMaxSize(0.8f)
        )
    }
}