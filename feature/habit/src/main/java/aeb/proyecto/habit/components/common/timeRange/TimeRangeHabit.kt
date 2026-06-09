package aeb.proyecto.habit.components.common.timeRange

import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.common.timeRange.components.DailyTimeRange
import aeb.proyecto.habit.components.common.timeRange.components.MonthlyTimeRange
import aeb.proyecto.habit.components.common.timeRange.components.WeeklyTimeRange
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.LocalDate

/**
 * A selector component that renders the appropriate date range navigation UI.
 *
 * This component acts as a high-level router, selecting and displaying the
 * specific time range UI (Daily, Weekly, Monthly, or Recurring) based on
 * [selectedTimeRangeUiState]. It dynamically adjusts the number of visible
 * elements based on the device orientation.
 *
 * @param selectedTimeRangeUiState The current state object defining the time range type and bounds.
 * @param selectedDate The currently active date, used by daily/recurring ranges.
 * @param onClickTimeRange Callback for interacting with date range changes,
 * providing the new [LocalDate] and a boolean indicating direction/reset.
 */
@Composable
fun TimeRangeHabit(
    selectedTimeRangeUiState: TimeRangeUiState,
    selectedDate: LocalDate = LocalDate.now(),
    onClickTimeRange: (LocalDate, Boolean) -> Unit = {_,_ ->},
){

    val orientation = getOrientation()

    // Adjusts the buffer of dates to display based on screen real estate
    val numberOfElements = remember {
        when(orientation){
            Orientation.Portrait -> 8
            Orientation.Landscape -> 13
        }
    }

    // Transitions between different time range views smoothly
    AnimatedContent(
        targetState = selectedTimeRangeUiState::class
    ) { timeRangeClass ->
        when (timeRangeClass) {
            TimeRangeUiState.Empty::class -> Unit
            TimeRangeUiState.Daily::class -> {
                val daily = selectedTimeRangeUiState as? TimeRangeUiState.Daily ?: return@AnimatedContent
                DailyTimeRange(selectedDate =selectedDate, daysOnRange = daily.days, onClick = onClickTimeRange, numberOfElements = numberOfElements)
            }
            TimeRangeUiState.Weekly::class -> {
                val weekly = selectedTimeRangeUiState as? TimeRangeUiState.Weekly ?: return@AnimatedContent
                WeeklyTimeRange(weekly.startOfWeek, weekly.endOfWeek, onClick = onClickTimeRange)
            }
            TimeRangeUiState.Monthly::class -> {
                val monthly = selectedTimeRangeUiState as? TimeRangeUiState.Monthly ?: return@AnimatedContent
                MonthlyTimeRange(monthly.startOfMonth, monthly.endOfMonth, onClick = onClickTimeRange)
            }
            TimeRangeUiState.Recurring::class -> {
                val recurring = selectedTimeRangeUiState as? TimeRangeUiState.Recurring ?: return@AnimatedContent
                DailyTimeRange(selectedDate =selectedDate, daysOnRange = recurring.days, onClick = onClickTimeRange, numberOfElements = numberOfElements)
            }
            else -> Unit
        }
    }

}