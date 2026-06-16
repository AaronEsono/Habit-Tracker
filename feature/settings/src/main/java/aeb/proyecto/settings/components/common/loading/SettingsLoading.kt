package aeb.proyecto.settings.components.common.loading

import aeb.proyecto.settings.R
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
import androidx.compose.ui.res.stringResource

/**
 * A custom loading indicator for the Settings screen.
 * Uses an infinite pulse animation to indicate processing state.
 */
@Composable
fun SettingsLoading(){
    val infiniteTransition = rememberInfiniteTransition(label = "habit loading")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "rotation animation"
    )

    Column (
        modifier = Modifier.fillMaxSize().padding(top = spacing16),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .size(spacing64)
                .graphicsLayer {
                    scaleX = progress
                    scaleY = progress
                    alpha = 1f - progress
                }
                .border(
                    width = spacing6,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
        )

        LabelMediumText(
            stringResource(R.string.settings_loading),
            modifier = Modifier.padding(top = spacing10))
    }
}