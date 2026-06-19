package aeb.proyecto.timer.components.common.typeTimer

import aeb.proyecto.timer.R
import aeb.proyecto.ui.text.LabelLargeText
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource

/**
 * Renders the primary display for the stopwatch mode.
 * Dynamically adjusts font size based on the available container width
 * to maintain visual impact and readability.
 *
 * @param modifier Applied to the [BoxWithConstraints] container.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun StopWatchSegmentedScreen(){
    BoxWithConstraints {
        val screenWidth = maxWidth
        val density = LocalDensity.current

        val fontSize = with(density) { (screenWidth * 0.20f).toSp() }

        LabelLargeText(
            text = stringResource(R.string.timer_stopwatch_start),
            fontSize = fontSize
        )
    }
}