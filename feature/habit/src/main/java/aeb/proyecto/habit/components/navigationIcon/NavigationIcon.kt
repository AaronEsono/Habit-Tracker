package aeb.proyecto.habit.components.navigationIcon

import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                ProvideAppBarNavigationIcon(selectedDate){DateActionIcon(selectedDate)}
            }
            is TimeRangeUiState.Recurring -> {
                ProvideAppBarNavigationIcon(selectedDate){DateActionIcon(selectedDate)}
            }
            else -> {
                ProvideAppBarNavigationIcon(){}
            }
        }
    }
}