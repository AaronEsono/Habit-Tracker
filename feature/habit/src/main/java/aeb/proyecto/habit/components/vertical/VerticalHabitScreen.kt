package aeb.proyecto.habit.components.vertical

import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.FilteredHabitsUiState
import aeb.proyecto.habit.PagerTypesUiState
import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.common.button.AddHabitButton
import aeb.proyecto.habit.components.common.loading.HabitLoading
import aeb.proyecto.habit.components.common.screens.NoHabitScreen
import aeb.proyecto.habit.components.vertical.components.screens.VerticalHabitContentScreen
import aeb.proyecto.habit.model.BottomSheetUIState
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.habit.model.pager.PagerElement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun VerticalHabitScreen(
    pagerTypesUIState: PagerTypesUiState,
    filteredHabitsUiState: FilteredHabitsUiState,
    currentPagerSelected: CurrentPagerSelection,
    selectedTimeRangeUiState: TimeRangeUiState,
    bottomSheetUIState: BottomSheetUIState,
    startDayOfWeek: DayOfWeek? = DayOfWeek.MONDAY,
    dateSelected: LocalDate = LocalDate.now(),
    navigateToAddHabit: (Long) -> Unit = {},
    onClickTab: (PagerElement) -> Unit = {},
    onClickTimeRange: (LocalDate, Boolean) -> Unit = {_,_ ->},
    onBottomSheetSelectDateSelected: () -> Unit = {},
    onDismissBottomSheet: (typeBottomSheet: TypeBottomSheet) -> Unit = {},
    onRestart: (id:Long,date:LocalDate) -> Unit = { _, _ -> },
    onClickConfigureHabit:(id:Long, date: LocalDate, goalDone: BigDecimal) -> Unit,
    onClickTimer: (Triple<Long,String, BigDecimal>) -> Unit,
    onClickCard: (id: Long) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit,
    onClick: (id:Long,date: LocalDate) -> Unit,
    onClickEdit: (id:Long) -> Unit,
    onClickDelete: (id:Long, color: Int) -> Unit = {_,_ ->},
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
                        startDayOfWeek = startDayOfWeek,
                        bottomSheetUIState = bottomSheetUIState,
                        selectedDate = dateSelected,
                        onClickTab = onClickTab,
                        onBottomSheetSelectDateSelected = onBottomSheetSelectDateSelected,
                        onRestart = onRestart,
                        onClickConfigureHabit = onClickConfigureHabit,
                        onClickTimer = onClickTimer,
                        onDismissBottomSheet = onDismissBottomSheet,
                        onClickTimeRange = onClickTimeRange,
                        onClickCard = onClickCard,
                        onLongClick = onLongClick,
                        onClick = onClick,
                        onClickEdit = onClickEdit,
                        onClickDelete = onClickDelete
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