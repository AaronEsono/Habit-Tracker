package aeb.proyecto.habit.components.common.navigationIcon

import aeb.proyecto.ui.date.utils.getTextToday
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate

/**
 * A navigation icon component that displays the currently selected date.
 *
 * This component features a high-contrast pill-shaped container and uses
 * [AnimatedContent] to provide a smooth transition effect when the
 * `selectedDate` state changes.
 *
 * @param selectedDate The [LocalDate] to be displayed in the icon.
 */
@Composable
fun DateActionIcon(
    selectedDate:LocalDate
){
    Box(
        modifier = Modifier
            .offset(x = (-4).dp)
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            .background(MaterialTheme.colorScheme.onSurface)
    ) {
        // Smoothly animates the text transition whenever the date changes
        AnimatedContent(
            targetState = selectedDate
        ) { selectedDate ->
            LabelLargeText(
                getTextToday(selectedDate),
                color = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.padding(
                    top = spacing4,
                    end = spacing20,
                    bottom = spacing4,
                    start = spacing8
                )
            )
        }
    }
}