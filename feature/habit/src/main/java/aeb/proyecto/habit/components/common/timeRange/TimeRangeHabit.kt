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

@Composable
fun TimeRangeHabit(
    selectedTimeRangeUiState: TimeRangeUiState,
    selectedDate: LocalDate = LocalDate.now(),
    onClickTimeRange: (LocalDate, Boolean) -> Unit = {_,_ ->},
){

    val orientation = getOrientation()

    val numberOfElements = remember {
        when(orientation){
            Orientation.Portrait -> 8
            Orientation.Landscape -> 13
        }
    }

    //Aqui mostramos los rangos de las fechas
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