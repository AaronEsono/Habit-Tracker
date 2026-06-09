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

/**
 * The root screen component for the Habit dashboard in landscape mode.
 *
 * This component acts as a high-level state machine. It evaluates the [pagerTypesUIState]
 * to determine the appropriate view:
 * 1. [PagerTypesUiState.Loading]: Displays a loading indicator.
 * 2. [PagerTypesUiState.Error]: Handles error states.
 * 3. [PagerTypesUiState.Success]: Renders the habit dashboard [HorizontalHabitContentScreen]
 * or the [NoHabitScreen] if no habit categories are available.
 * * It also manages the global entry point for adding new habits via [AddHabitButton].
 *
 * @param pagerTypesUIState The state of available habit categories.
 * @param filteredHabitsUiState The state of the habit list for the current category.
 * @param currentPagerSelected The current tab selection state.
 * @param selectedTimeRangeUiState The active time range state.
 * @param bottomSheetUIState State for all modal bottom sheets.
 * @param startDayOfWeek User configuration for the start day of the calendar week.
 * @param dateSelected The currently active date.
 * @param navigateToAddHabit Action to navigate to the "Add New Habit" screen.
 * @param onClickTab Callback to change the active habit category.
 * @param onClickTimeRange Action to update the time range.
 * @param onBottomSheetSelectDateSelected Action to open the date picker.
 * @param onDismissBottomSheet Action to close any open bottom sheet.
 * @param onRestart Action to reset habit progress.
 * @param onClickConfigureHabit Action to record progress on a habit.
 * @param onClickTimer Action to launch a habit timer.
 * @param onClickCard Action to view habit details.
 * @param onLongClick Callback for secondary interactions on habit cards.
 * @param onClick Callback for primary progress interactions.
 * @param onClickEdit Action to enter edit mode for a habit.
 * @param onClickDelete Action to initiate habit deletion.
 * @param onAcceptDeleteHabit Action to finalize habit deletion.
 */
@Composable
fun HorizontalHabitScreen(
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
    onClickCard: (id:Long) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit,
    onClick: (id:Long,date: LocalDate) -> Unit,
    onClickEdit: (id:Long) -> Unit,
    onClickDelete: (id:Long, color: Int) -> Unit = {_,_ ->},
    onAcceptDeleteHabit:(id:Long) -> Unit,
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
                        startDayOfWeek = startDayOfWeek,
                        selectedDate = dateSelected,
                        onClickTab = onClickTab,
                        onBottomSheetSelectDateSelected = onBottomSheetSelectDateSelected,
                        onDismissBottomSheet = onDismissBottomSheet,
                        onRestart = onRestart,
                        onClickConfigureHabit = onClickConfigureHabit,
                        onClickTimer = onClickTimer,
                        onClickTimeRange = onClickTimeRange,
                        onClickCard = onClickCard,
                        onLongClick = onLongClick,
                        onClick = onClick,
                        onClickEdit = onClickEdit,
                        onClickDelete = onClickDelete,
                        onAcceptDeleteHabit = onAcceptDeleteHabit
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