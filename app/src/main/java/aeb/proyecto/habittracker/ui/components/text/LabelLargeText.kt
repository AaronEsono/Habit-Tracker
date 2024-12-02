package aeb.proyecto.habittracker.ui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LabelLargeText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = Color.White
){
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelLarge,
        textAlign = textAlign,
        color = color
    )

}