package aeb.proyecto.habittracker.ui.components.text

import aeb.proyecto.habittracker.utils.ColorsTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.max

@Composable
fun LabelLargeText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = ColorsTheme.themeText
){
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelLarge,
        textAlign = textAlign,
        color = color,
        maxLines = 1,
        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
    )

}