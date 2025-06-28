package aeb.proyecto.habit

import aeb.proyecto.habit.components.bottomSheet.editHabitDay.BottomSheetEditHabitDay
import aeb.proyecto.habit.components.bottomSheet.selectDate.BottomSheetSelectDate
import aeb.proyecto.habit.components.loading.HabitLoading
import aeb.proyecto.habit.components.navigationIcon.ActionIconHabitScreen
import aeb.proyecto.habit.components.screen.NoHabitScreen
import aeb.proyecto.habit.components.screen.PagerElementScreen
import aeb.proyecto.habit.model.BottomSheetType
import aeb.proyecto.habit.model.pager.PagerElement
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate

// NOTA: Iconos de flaticon y svgRepo

// Mirar en un futuro el horizontalRow con los dias, optimizarlo
// Mirar en addHabit como hacer que se deslice los mensuales

/** Pantalla para mostrar los hábitos e intercactuar con ellos,
 * como añadir nuevos dailyHabits, borrar hábitos o editarlos.
 * */
@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    navigateToAddHabit: (Long) -> Unit,
    navigateToTimer : () -> Unit
){

    val pagerTypesUIState = viewModel.availablePagerTypesUiState.collectAsStateWithLifecycle().value
    val currentPagerSelected = viewModel.currentPagerType.collectAsStateWithLifecycle().value
    val selectedTimeRange = viewModel.selectedTimeRangeUiState.collectAsStateWithLifecycle().value
    val filteredHabitsUiState = viewModel.habitsForSelectedTimeUiState.collectAsStateWithLifecycle().value
    val selectedDate = viewModel.selectedDate.collectAsStateWithLifecycle().value
    val dataHabitUIState = viewModel.dataHabitUIState.collectAsStateWithLifecycle().value

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.habit_topbar), fontSize = 20.sp)
    }

    ActionIconHabitScreen(
        selectedTimeRange = selectedTimeRange,
        selectedDate = selectedDate
    )

    HabitScreen(
        pagerTypesUIState = pagerTypesUIState,
        filteredHabitsUiState = filteredHabitsUiState,
        currentPagerSelected = currentPagerSelected,
        selectedTimeRangeUiState = selectedTimeRange,
        dateSelected = selectedDate,
        navigateToAddHabit = navigateToAddHabit,
        onClickTab = viewModel::onPagerTypeSelected,
        onClickTimeRange = viewModel::onClickTimeRange,
        onBottomSheetSelected = viewModel::onBottomSheetSelected,
        onLongClick = viewModel::onLongClick,
        onClick = viewModel::onClick
    )

    if(dataHabitUIState.bottomSheetState.isExpanded){
        when(dataHabitUIState.bottomSheetState.type){
            BottomSheetType.SELECT_DATE -> {
                BottomSheetSelectDate(
                    onDismiss = viewModel::onDismissBottomSheet,
                    selectedDate = selectedDate,
                    onClick = viewModel::onClickTimeRange
                )
            }
        }
    }

    if(dataHabitUIState.showEditHabitDayBT.showEditHabitDayBT){
        BottomSheetEditHabitDay(
            habit = dataHabitUIState.showEditHabitDayBT.habit,
            habitDay = dataHabitUIState.showEditHabitDayBT.habitDay,
            onDismiss = viewModel::onDismissEdit,
            onRestart = viewModel::onRestart,
            onClick = viewModel::onClick,
            onClickTimer = { data ->
                viewModel.onClickTimerHabit(data){
                    navigateToTimer()
                }
            }
        )
    }
}

@Composable
internal fun HabitScreen(
    pagerTypesUIState: PagerTypesUiState,
    filteredHabitsUiState: FilteredHabitsUiState,
    currentPagerSelected: CurrentPagerSelection,
    selectedTimeRangeUiState: TimeRangeUiState,
    dateSelected: LocalDate = LocalDate.now(),
    navigateToAddHabit: (Long) -> Unit = {},
    onClickTab: (PagerElement) -> Unit = {},
    onClickTimeRange: (LocalDate) -> Unit = {},
    onBottomSheetSelected: (BottomSheetType) -> Unit = {},
    onLongClick: (id:Long,date:LocalDate) -> Unit,
    onClick: (id:Long,date:LocalDate) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()){
        when (pagerTypesUIState) {
            PagerTypesUiState.Error -> Unit
            PagerTypesUiState.Loading -> {
                HabitLoading()
            }
            is PagerTypesUiState.Success -> {
                if (pagerTypesUIState.availableTypes.isEmpty()) {
                    NoHabitScreen()
                } else {
                    PagerElementScreen(
                        pagerElements = pagerTypesUIState.availableTypes,
                        filteredHabitsUIState = filteredHabitsUiState,
                        currentPagerSelected = currentPagerSelected,
                        selectedTimeRangeUiState = selectedTimeRangeUiState,
                        selectedDate = dateSelected,
                        onClickTab = onClickTab,
                        onClickTimeRange = onClickTimeRange,
                        onLongClick = onLongClick,
                        onClick = onClick
                    )

                    ProvideAppBarActions {
                        Icon(
                            painter = painterResource(R.drawable.ic_find_date),
                            contentDescription = "calendar icon",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(end = spacing6)
                                .size(25.dp)
                                .clickable {onBottomSheetSelected(BottomSheetType.SELECT_DATE)}
                        )
                    }
                }
            }
        }

        CustomRipple {
            FloatingActionButton(
                onClick = { navigateToAddHabit(-1) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(spacing16)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Floating action button")
            }
        }
    }
}