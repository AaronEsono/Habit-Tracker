package aeb.proyecto.habit.components.vertical

import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.FilteredHabitsUiState
import aeb.proyecto.habit.PagerTypesUiState
import aeb.proyecto.habit.R
import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.common.button.AddHabitButton
import aeb.proyecto.habit.components.common.button.BarActionIcon
import aeb.proyecto.habit.components.common.loading.HabitLoading
import aeb.proyecto.habit.components.common.screens.NoHabitScreen
import aeb.proyecto.habit.components.screen.PagerElementScreen
import aeb.proyecto.habit.components.vertical.components.screens.VerticalHabitContentScreen
import aeb.proyecto.habit.model.BottomSheetType
import aeb.proyecto.habit.model.pager.PagerElement
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun VerticalHabitScreen(
    pagerTypesUIState: PagerTypesUiState,
    filteredHabitsUiState: FilteredHabitsUiState,
    currentPagerSelected: CurrentPagerSelection,
    selectedTimeRangeUiState: TimeRangeUiState,
    dateSelected: LocalDate = LocalDate.now(),
    navigateToAddHabit: (Long) -> Unit = {},
    onClickTab: (PagerElement) -> Unit = {},
    onClickTimeRange: (LocalDate) -> Unit = {},
    onBottomSheetSelected: (BottomSheetType) -> Unit = {},
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

                    VerticalHabitContentScreen(
                        pagerElements = pagerTypesUIState.availableTypes,
                        filteredHabitsUIState = filteredHabitsUiState,
                        currentPagerSelected = currentPagerSelected,
                        selectedTimeRangeUiState = selectedTimeRangeUiState,
                        selectedDate = dateSelected,
                        onClickTab = onClickTab,
                        onBottomSheetSelected = onBottomSheetSelected,
                        onClickTimeRange = onClickTimeRange,
                        onLongClick = onLongClick,
                        onClick = onClick
                    )

                    BarActionIcon(
                        onBottomSheetSelected = onBottomSheetSelected
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