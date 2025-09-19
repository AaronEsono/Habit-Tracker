package aeb.proyecto.habit.components.vertical.components.screens

import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.FilteredHabitsUiState
import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.bottomSheet.selectDate.BottomSheetSelectDate
import aeb.proyecto.habit.components.common.button.BarActionIcon
import aeb.proyecto.habit.components.common.pager.PageSelected
import aeb.proyecto.habit.components.common.timeRange.TimeRangeHabit
import aeb.proyecto.habit.model.BottomSheetType
import aeb.proyecto.habit.model.BottomSheetUIState
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.habit.model.pager.PagerElement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.LocalDate

@Composable
fun VerticalHabitContentScreen(
    pagerElements: List<PagerElement>,
    filteredHabitsUIState: FilteredHabitsUiState,
    currentPagerSelected: CurrentPagerSelection,
    selectedTimeRangeUiState: TimeRangeUiState,
    bottomSheetUIState: BottomSheetUIState,
    selectedDate: LocalDate = LocalDate.now(),
    onClickTab: (PagerElement) -> Unit = {},
    onBottomSheetSelected: () -> Unit = {},
    onDismissBottomSheet: () -> Unit = {},
    onClickTimeRange: (LocalDate) -> Unit = {},
    onLongClick: (id:Long,date:LocalDate) -> Unit,
    onClick: (id: Long, date: LocalDate) -> Unit
){

    BarActionIcon(
        onBottomSheetSelected = onBottomSheetSelected
    )

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
    }

    if(bottomSheetUIState.isEnabled){
        when(bottomSheetUIState.typeOfBottomSheet){
            is TypeBottomSheet.EditHabitDay -> {}
            TypeBottomSheet.SelectDate -> {
                BottomSheetSelectDate(
                    onDismiss = onDismissBottomSheet,
                    selectedDate = selectedDate,
                    onClick = onClickTimeRange
                )
            }
        }
    }

}