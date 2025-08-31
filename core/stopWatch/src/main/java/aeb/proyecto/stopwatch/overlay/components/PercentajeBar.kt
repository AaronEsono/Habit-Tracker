package aeb.proyecto.stopwatch.overlay.components

import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PercentageBar(
    modifier: Modifier = Modifier,
    percentage: Float,
    filledColor: Color,
    emptyColor: Color,
    pausedColor: Color,
    isPaused : Boolean = false
) {

    val color = remember (isPaused){ if (isPaused) pausedColor else filledColor }

    Box(
        modifier = modifier
            .height(5.dp)
            .width(85.dp)
            .background(emptyColor, RoundedCornerShape(spacing20))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = percentage.coerceIn(0f, 1f))
                .background(color, RoundedCornerShape(spacing20))
        )
    }
}