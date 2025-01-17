package aeb.proyecto.habittracker.ui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun LabelSmallText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    fontWeight: FontWeight? = null
){
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        textAlign = textAlign,
        color = color,
        maxLines = maxLines,
        overflow = overflow,
        fontWeight = fontWeight
    )

}