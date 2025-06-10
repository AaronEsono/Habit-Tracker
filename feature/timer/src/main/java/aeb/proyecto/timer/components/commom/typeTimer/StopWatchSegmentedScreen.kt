package aeb.proyecto.timer.components.commom.typeTimer

import aeb.proyecto.timer.R
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource

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