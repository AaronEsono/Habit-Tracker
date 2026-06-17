package aeb.proyecto.statistics.components.common.boxDays

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Renders a single day-of-the-week label for the calendar grid.
 * Configured with platform-specific adjustments to remove font padding,
 * ensuring perfect vertical centering within the allocated [size].
 *
 * @param modifier Applied to the [Box] container.
 * @param text The abbreviated day name (e.g., "Mon").
 * @param textSize The font size calculated to fit the parent container.
 * @param size The total height allocated for this label row.
 */
@Composable
fun TextDayWeek(
    modifier: Modifier = Modifier,
    text:String,
    textSize: TextUnit,
    size: Dp
){
    Box(
        modifier = modifier
            .height(size),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = textSize,
                // Esto elimina el padding interno de la fuente que causa el desvío vertical
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                // Forzamos que la altura de línea sea igual al tamaño del texto
                lineHeight = textSize
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Visible,
        )

    }

}