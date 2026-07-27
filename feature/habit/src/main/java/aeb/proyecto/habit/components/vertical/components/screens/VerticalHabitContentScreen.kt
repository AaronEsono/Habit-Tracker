package aeb.proyecto.habit.components.vertical.components.screens

import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.FilteredHabitsUiState
import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.common.bottomSheet.deleteHabit.DeleteHabitBottomSheet
import aeb.proyecto.habit.components.common.button.BarActionIcon
import aeb.proyecto.habit.components.common.loading.HabitLoading
import aeb.proyecto.habit.components.common.pager.PageSelected
import aeb.proyecto.habit.components.common.timeRange.TimeRangeHabit
import aeb.proyecto.habit.components.vertical.components.bottomSheet.configureHabit.VerticalConfigureHabitBottomSheet
import aeb.proyecto.habit.components.vertical.components.bottomSheet.editHabit.VerticalEditHabitBottomSheet
import aeb.proyecto.habit.components.vertical.components.bottomSheet.selectDate.VerticalSelectDateBottomSheet
import aeb.proyecto.habit.components.vertical.components.screens.typeHabit.VerticalDailyHabitScreen
import aeb.proyecto.habit.components.vertical.components.screens.typeHabit.VerticalMonthlyHabitScreen
import aeb.proyecto.habit.components.vertical.components.screens.typeHabit.VerticalRecurringHabitScreen
import aeb.proyecto.habit.components.vertical.components.screens.typeHabit.VerticalWeeklyHabitScreen
import aeb.proyecto.habit.model.BottomSheetUIState
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.habit.model.pager.PagerElement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * The primary habit dashboard container for vertical orientations.
 *
 * This component orchestrates the vertical layout by layering the action bar,
 * tab navigation, time range selectors, and the specific habit screen content.
 * It monitors [BottomSheetUIState] to inject the appropriate modal bottom sheets
 * into the composition overlay.
 *
 * @param pagerElements List of categories to display in the tab row.
 * @param filteredHabitsUIState Current state of habit data (Loading/Success/Empty).
 * @param currentPagerSelected The active tab selection.
 * @param selectedTimeRangeUiState The active time range configuration.
 * @param bottomSheetUIState Global state controlling the visibility of bottom sheets.
 * @param startDayOfWeek User configuration for the start day of the week.
 * @param selectedDate The currently active date.
 * @param onClickTab Callback to change the active habit category.
 * @param onBottomSheetSelectDateSelected Action to trigger the date selection sheet.
 * @param onDismissBottomSheet Action to close any active bottom sheet.
 * @param onRestart Action to reset habit progress.
 * @param onClickConfigureHabit Action to record habit progress.
 * @param onClickTimer Action to launch a habit timer.
 * @param onClickTimeRange Action to update the time range.
 * @param onClickCard Action to view habit details.
 * @param onLongClick Callback for secondary interactions on habit cards.
 * @param onClick Callback for primary progress interactions.
 * @param onClickEdit Action to navigate to the habit edit form.
 * @param onClickDelete Action to initiate habit deletion.
 * @param onAcceptDeleteHabit Action to confirm and perform habit deletion.
 */
@Composable
fun VerticalHabitContentScreen(
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
    onClickTimeRange: (LocalDate, Boolean) -> Unit = {_,_ ->},
    onClickCard: (id: Long) -> Unit,
    onLongClick: (id:Long,date:LocalDate) -> Unit,
    onClick: (id: Long, date: LocalDate) -> Unit,
    onClickEdit: (id: Long) -> Unit,
    onClickDelete: (id:Long, color: Int) -> Unit = {_,_ ->},
    onAcceptDeleteHabit:(id:Long) -> Unit,
){

    BarActionIcon(
        onBottomSheetSelected = onBottomSheetSelectDateSelected
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .testTag("habit_content_screen")
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
                                VerticalDailyHabitScreen(
                                    selectedDate, filteredHabitsUIState.habits,
                                    onClickCard = onClickCard,
                                    onLongClick = onLongClick,
                                    onClick = onClick
                                )
                            }
                        }
                        PagerElement.WEEKLY -> {
                            if(selectedTimeRangeUiState is TimeRangeUiState.Weekly){
                                VerticalWeeklyHabitScreen(
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
                                VerticalMonthlyHabitScreen(
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
                                VerticalRecurringHabitScreen(
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
        VerticalConfigureHabitBottomSheet(
            habitWithDay = bottomSheetUIState.enabledConfigureHabitState.habitWithDay,
            onDismiss = onDismissBottomSheet,
            onRestart = onRestart,
            onClickTimer = onClickTimer,
            onClick = onClickConfigureHabit
        )
    }

    if(bottomSheetUIState.enabledSelectDateState.enabled){
        VerticalSelectDateBottomSheet(
            onDismiss = onDismissBottomSheet,
            selectedDate = selectedDate,
            onClick = onClickTimeRange
        )
    }

    if(bottomSheetUIState.enabledEditHabitState.enabled){
        VerticalEditHabitBottomSheet(
            idHabit = bottomSheetUIState.enabledEditHabitState.idHabit,
            onDismiss = onDismissBottomSheet,
            startDayOfWeek = startDayOfWeek,
            onClickEdit = onClickEdit,
            onClickDelete = onClickDelete,
            onClick = onClick,
            onLongClick = onLongClick
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