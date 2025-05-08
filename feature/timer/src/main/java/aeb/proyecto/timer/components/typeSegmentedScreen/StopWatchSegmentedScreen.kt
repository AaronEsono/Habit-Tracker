package aeb.proyecto.timer.components.typeSegmentedScreen

import aeb.proyecto.timer.R
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun StopWatchSegmentedScreen(){

    LabelLargeText(
        stringResource(R.string.timer_stopwatch_start),
        fontSize = 50.sp
    )

}