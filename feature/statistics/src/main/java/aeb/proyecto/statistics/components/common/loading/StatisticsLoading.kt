package aeb.proyecto.statistics.components.common.loading

import aeb.proyecto.statistics.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing64
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource

/**
 * Renders a loading animation for the Statistics screen.
 * Features an infinite pulse effect using [rememberInfiniteTransition]
 * that scales and fades a circular ring.
 */
@Composable
fun StatisticsLoading(){
    val infiniteTransition = rememberInfiniteTransition(label = "habit loading")

    // Animates a progress float from 0 to 1 repeatedly to control scaling and alpha
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "rotation animation"
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = spacing16)
            .testTag("statistics_loading"),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // Pulsing circular indicator
        Box(
            modifier = Modifier
                .size(spacing64)
                .graphicsLayer {
                    scaleX = progress
                    scaleY = progress
                    alpha = 1f - progress // Fades out as it expands
                }
                .border(
                    width = spacing6,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
        )

        // Loading message
        LabelMediumText(stringResource(R.string.statistics_loading),
            modifier = Modifier.padding(top = spacing10))
    }
}