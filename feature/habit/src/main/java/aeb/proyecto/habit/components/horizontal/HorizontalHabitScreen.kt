package aeb.proyecto.habit.components.horizontal

import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.FilteredHabitsUiState
import aeb.proyecto.habit.PagerTypesUiState
import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.common.button.AddHabitButton
import aeb.proyecto.habit.components.common.loading.HabitLoading
import aeb.proyecto.habit.components.common.screens.NoHabitScreen
import aeb.proyecto.habit.components.horizontal.components.screens.HorizontalHabitContentScreen
import aeb.proyecto.habit.model.BottomSheetUIState
import aeb.proyecto.habit.model.pager.PagerElement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalDate

@Composable
fun HorizontalHabitScreen(
    pagerTypesUIState: PagerTypesUiState,
    filteredHabitsUiState: FilteredHabitsUiState,
    currentPagerSelected: CurrentPagerSelection,
    selectedTimeRangeUiState: TimeRangeUiState,
    bottomSheetUIState: BottomSheetUIState,
    dateSelected: LocalDate = LocalDate.now(),
    navigateToAddHabit: (Long) -> Unit = {},
    onClickTab: (PagerElement) -> Unit = {},
    onClickTimeRange: (LocalDate, Boolean) -> Unit = {_,_ ->},
    onBottomSheetSelected: () -> Unit = {},
    onDismissBottomSheet: () -> Unit = {},
    onLongClick: (id:Long,date: LocalDate) -> Unit,
    onClick: (id:Long,date: LocalDate) -> Unit
){

    Box(modifier = Modifier.fillMaxSize()){
        when(pagerTypesUIState){
            PagerTypesUiState.Error -> Unit
            PagerTypesUiState.Loading -> {
                HabitLoading()
            }
            is PagerTypesUiState.Success -> {
                if (pagerTypesUIState.availableTypes.isEmpty()) {
                    NoHabitScreen()
                }else{

                    HorizontalHabitContentScreen(
                        pagerElements = pagerTypesUIState.availableTypes,
                        filteredHabitsUIState = filteredHabitsUiState,
                        currentPagerSelected = currentPagerSelected,
                        selectedTimeRangeUiState = selectedTimeRangeUiState,
                        bottomSheetUIState = bottomSheetUIState,
                        selectedDate = dateSelected,
                        onClickTab = onClickTab,
                        onBottomSheetSelected = onBottomSheetSelected,
                        onDismissBottomSheet = onDismissBottomSheet,
                        onClickTimeRange = onClickTimeRange,
                        onLongClick = onLongClick,
                        onClick = onClick
                    )
                }

                AddHabitButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    navigateToAddHabit = navigateToAddHabit
                )
            }
        }
    }

}