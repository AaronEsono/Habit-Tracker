package aeb.proyecto.statistics.components.common.boxDays

import aeb.proyecto.ui.text.BodySmallText
import androidx.annotation.Size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TextDayWeek(
    modifier: Modifier = Modifier,
    text:String,
    textSize: TextUnit,
    size: Dp
){

    Box(
        modifier = Modifier.height(size),
        contentAlignment = Alignment.Center
    ) {
        BodySmallText(
            text = text,
            modifier = modifier.height(size).fillMaxWidth().align(Alignment.Center),
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            fontSize = textSize,
        )
    }

}