package aeb.proyecto.habit.components.common.navigationIcon

import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import java.time.LocalDate

@Composable
fun ActionIconHabitScreen(
    selectedTimeRange: TimeRangeUiState,
    selectedDate:LocalDate
){
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
                ProvideAppBarNavigationIcon(){}
            }
        }
    }
}