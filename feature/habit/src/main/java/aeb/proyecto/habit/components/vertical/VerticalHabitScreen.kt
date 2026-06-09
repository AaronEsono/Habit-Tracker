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

/**
 * The root container screen for the habit dashboard in vertical orientation.
 *
 * This component acts as the high-level state orchestrator. It evaluates the
 * [pagerTypesUIState] to determine the UI state:
 * - [PagerTypesUiState.Loading]: Renders a loading state.
 * - [PagerTypesUiState.Success]: Renders the dashboard via [VerticalHabitContentScreen]
 * or the [NoHabitScreen] if no categories exist.
 *
 * It positions the [AddHabitButton] globally at the bottom-right of the screen.
 *
 * @param pagerTypesUIState State containing available habit categories.
 * @param filteredHabitsUiState Current list of habits to display.
 * @param currentPagerSelected The currently active tab.
 * @param selectedTimeRangeUiState The active time range configuration.
 * @param bottomSheetUIState State for active modal bottom sheets.
 * @param startDayOfWeek User configuration for the start day of the week.
 * @param dateSelected The currently selected date.
 * @param navigateToAddHabit Action to navigate to the habit creation flow.
 * @param onClickTab Callback to change categories.
 * @param onClickTimeRange Callback to update the time range.
 * @param onBottomSheetSelectDateSelected Action to open the date selection sheet.
 * @param onDismissBottomSheet Action to close any active sheet.
 * @param onRestart Action to reset habit progress.
 * @param onClickConfigureHabit Action to log progress.
 * @param onClickTimer Action to start a habit timer.
 * @param onClickCard Action to view habit details.
 * @param onLongClick Secondary calendar interaction.
 * @param onClick Primary interaction for habit progress.
 * @param onClickEdit Action to edit habit metadata.
 * @param onClickDelete Action to initiate habit deletion.
 * @param onAcceptDeleteHabit Action to confirm deletion.
 */
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