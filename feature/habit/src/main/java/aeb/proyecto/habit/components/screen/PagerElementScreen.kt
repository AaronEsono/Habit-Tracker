package aeb.proyecto.habit.components.screen

import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.FilteredHabitsUiState
import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.common.loading.HabitLoading
import aeb.proyecto.habit.components.common.pager.PageSelected
import aeb.proyecto.habit.components.common.timeRange.TimeRangeHabit
import aeb.proyecto.habit.components.screen.typeHabits.DailyHabitsScreen
import aeb.proyecto.habit.components.screen.typeHabits.MonthlyHabitsScreen
import aeb.proyecto.habit.model.pager.PagerElement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.LocalDate

/**
 *  Pantalla para mostrar los hábitos de un tipo en específico.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerElementScreen(
    pagerElements: List<PagerElement>,
    filteredHabitsUIState: FilteredHabitsUiState,
    currentPagerSelected: CurrentPagerSelection,
    selectedTimeRangeUiState: TimeRangeUiState,
    selectedDate: LocalDate = LocalDate.now(),
    onClickTab: (PagerElement) -> Unit = {},
    onClickTimeRange: (LocalDate) -> Unit = {},
    onLongClick: (id:Long,date:LocalDate) -> Unit,
    onClick: (id: Long, date: LocalDate) -> Unit
){

    Column (
        modifier = Modifier.fillMaxSize()
    ){

        PageSelected(
            pagerElements = pagerElements,
            currentPagerSelected = currentPagerSelected,
            onClickTab = onClickTab
        )

        TimeRangeHabit(
            selectedTimeRangeUiState = selectedTimeRangeUiState,
            selectedDate = selectedDate,
            onClickTimeRange = onClickTimeRange
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)

        // Contenido de los hábitos
        when (filteredHabitsUIState) {
            is FilteredHabitsUiState.Loading, is FilteredHabitsUiState.Error, is FilteredHabitsUiState.Empty -> {
                HabitLoading()
            }
            is FilteredHabitsUiState.Success -> {
                if (currentPagerSelected is CurrentPagerSelection.Selected) {
                    when (currentPagerSelected.pagerSelected.pagerElement) {
                        PagerElement.DAILY -> {
                            DailyHabitsScreen(
                                selectedDate, filteredHabitsUIState.habits,
                                onLongClick = onLongClick,
                                onClick = onClick
                            )
                        }

                        PagerElement.WEEKLY -> {

                        }
                        PagerElement.MONTHLY -> {
                            MonthlyHabitsScreen()
                        }
                        PagerElement.RECURRING -> {

                        }
                    }
                }
            }
        }
    }
}