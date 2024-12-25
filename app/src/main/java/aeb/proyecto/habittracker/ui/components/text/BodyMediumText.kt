package aeb.proyecto.habittracker.ui.components.text

import aeb.proyecto.habittracker.utils.ColorsTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BodyMediumText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = ColorsTheme.themeText,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null
){
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = textAlign,
        color = color,
        fontStyle = fontStyle,
        fontWeight = fontWeight
    )

}