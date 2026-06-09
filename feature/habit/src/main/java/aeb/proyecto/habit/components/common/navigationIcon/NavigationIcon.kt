package aeb.proyecto.habit.components.common.navigationIcon

import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import java.time.LocalDate

/**
 * A navigation controller component for the habit screen's app bar.
 *
 * This component reacts to changes in the [TimeRangeUiState], conditionally rendering
 * the date navigation icon ([DateActionIcon]) for daily or recurring views,
 * or clearing the navigation slot for other states (e.g., summary or monthly views).
 *
 * @param selectedTimeRange The current UI state representing the view mode (Daily, Recurring, etc.).
 * @param selectedDate The currently active date used to update the [DateActionIcon].
 */
@Composable
fun ActionIconHabitScreen(
    selectedTimeRange: TimeRangeUiState,
    selectedDate:LocalDate
){
    // Transitions between different UI states when the time range changes
    AnimatedContent(
        targetState = selectedTimeRange
    ) { selectedTimeRange ->
        when(selectedTimeRange){
            is TimeRangeUiState.Daily -> {
                ProvideAppBarNavigationIcon(selectedDate){ DateActionIcon(selectedDate) }
            }
            is TimeRangeUiState.Recurring -> {
                ProvideAppBarNavigationIcon(selectedDate){ DateActionIcon(selectedDate) }
            }
            else -> {
                // Clear the navigation area for non-date-specific views
                ProvideAppBarNavigationIcon(){}
            }
        }
    }
}