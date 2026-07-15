package aeb.proyecto.settings.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek

/**
 * Utility to determine the button container color based on selection state.
 * Reduces boilerplate by using a generic type [T] for comparison.
 * @param current The current value of the option.
 * @param selected The value that is currently selected.
 * @return [MaterialTheme.colorScheme.surfaceContainer] if selected, otherwise [MaterialTheme.colorScheme.background].
 */
@Composable
fun <T> getSelectionContainerColor(current: T, selected: T): Color {
    return if (current == selected) {
        MaterialTheme.colorScheme.surfaceContainer
    } else {
        MaterialTheme.colorScheme.background
    }
}