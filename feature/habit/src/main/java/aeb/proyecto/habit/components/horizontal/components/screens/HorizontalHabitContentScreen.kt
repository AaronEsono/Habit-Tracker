package aeb.proyecto.habit.components.horizontal.components.screens

import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.FilteredHabitsUiState
import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.common.bottomSheet.deleteHabit.DeleteHabitBottomSheet
import aeb.proyecto.habit.components.common.button.BarActionIcon
import aeb.proyecto.habit.components.common.loading.HabitLoading
import aeb.proyecto.habit.components.common.pager.PageSelected
import aeb.proyecto.habit.components.common.timeRange.TimeRangeHabit
import aeb.proyecto.habit.components.horizontal.components.bottomSheet.configureHabit.HorizontalConfigureHabitBottomSheet
import aeb.proyecto.habit.components.horizontal.components.bottomSheet.editHabit.HorizontalEditHabitBottomSheet
import aeb.proyecto.habit.components.horizontal.components.bottomSheet.selectDate.HorizontalSelectDateBottomSheet
import aeb.proyecto.habit.components.horizontal.components.screens.typeHabit.HorizontalDailyHabitScreen
import aeb.proyecto.habit.components.horizontal.components.screens.typeHabit.HorizontalMonthlyHabitScreen
import aeb.proyecto.habit.components.horizontal.components.screens.typeHabit.HorizontalRecurringHabitScreen
import aeb.proyecto.habit.components.horizontal.components.screens.typeHabit.HorizontalWeeklyHabitScreen
import aeb.proyecto.habit.components.vertical.components.bottomSheet.editHabit.VerticalEditHabitBottomSheet
import aeb.proyecto.habit.model.BottomSheetUIState
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.habit.model.pager.PagerElement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun HorizontalHabitContentScreen(
    pagerElements: List<PagerElement>,
    filteredHabitsUIState: FilteredHabitsUiState,
    currentPagerSelected: CurrentPagerSelection,
    selectedTimeRangeUiState: TimeRangeUiState,
    bottomSheetUIState: BottomSheetUIState,
    startDayOfWeek: DayOfWeek? = DayOfWeek.MONDAY,
    selectedDate: LocalDate = LocalDate.now(),
    onClickTab: (PagerElement) -> Unit = {},
    onBottomSheetSelectDateSelected: () -> Unit = {},
    onDismissBottomSheet: (typeBottomSheet: TypeBottomSheet) -> Unit = {},
    onRestart: (id:Long,date:LocalDate) -> Unit = { _, _ -> },
    onClickConfigureHabit:(id:Long, date: LocalDate, goalDone: BigDecimal) -> Unit,
    onClickTimer: (Triple<Long,String, BigDecimal>) -> Unit,
    onClickTimeRange: (LocalDate, Boolean) -> Unit = { _, _ ->},
    onClickCard: (id:Long) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit,
    onClick: (id: Long, date: LocalDate) -> Unit,
    onClickEdit: (id: Long) -> Unit,
    onClickDelete: (id:Long, color: Int) -> Unit = {_,_ ->},
    onAcceptDeleteHabit:(id:Long) -> Unit,
) {

    BarActionIcon(
        onBottomSheetSelected = onBottomSheetSelectDateSelected
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

        when(filteredHabitsUIState){
            is FilteredHabitsUiState.Loading, is FilteredHabitsUiState.Error, is FilteredHabitsUiState.Empty -> {
                HabitLoading()
            }
            is FilteredHabitsUiState.Success -> {
                if(currentPagerSelected is CurrentPagerSelection.Selected){
                    when(currentPagerSelected.pagerSelected.pagerElement){
                        PagerElement.DAILY -> {
                            if(selectedTimeRangeUiState is TimeRangeUiState.Daily){
                                HorizontalDailyHabitScreen(
                                    selectedDate, filteredHabitsUIState.habits,
                                    onClickCard = onClickCard,
                                    onLongClick = onLongClick,
                                    onClick = onClick
                                )
                            }
                        }
                        PagerElement.WEEKLY -> {
                            if(selectedTimeRangeUiState is TimeRangeUiState.Weekly){
                                HorizontalWeeklyHabitScreen(
                                    selectedTimeRangeUiState,
                                    filteredHabitsUIState.habits,
                                    onClickCard = onClickCard,
                                    onLongClick = onLongClick,
                                    onClick = onClick
                                )
                            }
                        }
                        PagerElement.MONTHLY -> {
                            if(selectedTimeRangeUiState is TimeRangeUiState.Monthly){
                                HorizontalMonthlyHabitScreen(
                                    timeRange = selectedTimeRangeUiState,
                                    startDayOfWeek = startDayOfWeek,
                                    filteredHabitsUIState.habits,
                                    onClickCard = onClickCard,
                                    onLongClick = onLongClick,
                                    onClick = onClick
                                )
                            }
                        }
                        PagerElement.RECURRING -> {
                            if(selectedTimeRangeUiState is TimeRangeUiState.Recurring){
                                HorizontalRecurringHabitScreen(
                                    selectedDate, filteredHabitsUIState.habits,
                                    onClickCard = onClickCard,
                                    onLongClick = onLongClick,
                                    onClick = onClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    if(bottomSheetUIState.enabledConfigureHabitState.enabled){
        HorizontalConfigureHabitBottomSheet(
            habitWithDay = bottomSheetUIState.enabledConfigureHabitState.habitWithDay,
            onDismiss = onDismissBottomSheet,
            onRestart = onRestart,
            onClickTimer = onClickTimer,
            onClick = onClickConfigureHabit
        )
    }

    if(bottomSheetUIState.enabledEditHabitState.enabled){
        HorizontalEditHabitBottomSheet(
            idHabit = bottomSheetUIState.enabledEditHabitState.idHabit,
            onDismiss = onDismissBottomSheet,
            startDayOfWeek = startDayOfWeek,
            onClickEdit = onClickEdit,
            onClickDelete = onClickDelete,
            onClick = onClick,
            onLongClick = onLongClick
        )
    }

    if(bottomSheetUIState.enabledSelectDateState.enabled){
        HorizontalSelectDateBottomSheet(
            onDismiss = onDismissBottomSheet,
            selectedDate = selectedDate,
            onClick = onClickTimeRange
        )
    }

    if(bottomSheetUIState.enabledDeleteHabitState.enabled){
        DeleteHabitBottomSheet(
            colorButton = bottomSheetUIState.enabledDeleteHabitState.color,
            onDismiss = { onDismissBottomSheet(TypeBottomSheet.DeleteHabit()) },
            onAcceptDelete = {
                onDismissBottomSheet(TypeBottomSheet.EditHabit())
                onDismissBottomSheet(TypeBottomSheet.DeleteHabit())
                onAcceptDeleteHabit(bottomSheetUIState.enabledDeleteHabitState.id)
            }
        )
    }
}

