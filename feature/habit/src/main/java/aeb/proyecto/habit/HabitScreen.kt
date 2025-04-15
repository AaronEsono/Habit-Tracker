package aeb.proyecto.habit

import aeb.proyecto.habit.components.loading.HabitLoading
import aeb.proyecto.habit.components.screen.NoHabitScreen
import aeb.proyecto.habit.components.screen.PagerElementScreen
import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate

// NOTA: Iconos de flaticon y svgRepo

/** Pantalla para mostrar los hábitos e intercactuar con ellos,
 * como añadir nuevos dailyHabits, borrar hábitos o editarlos.*/
@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    navigateToAddHabit: (Long) -> Unit
){

    val pagerTypesUIState = viewModel.availablePagerTypesUiState.collectAsStateWithLifecycle().value
    val currentPagerSelected = viewModel.currentPagerType.collectAsStateWithLifecycle().value
    val selectedTimeRange = viewModel.selectedTimeRangeUiState.collectAsStateWithLifecycle().value
    val filteredHabitsUiState = viewModel.habitsForSelectedTimeUiState.collectAsStateWithLifecycle().value
    val selectedDate = viewModel.selectedDate.collectAsStateWithLifecycle().value

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.habit_topbar), fontSize = 20.sp)
    }

    HabitScreen(
        pagerTypesUIState = pagerTypesUIState,
        filteredHabitsUiState = filteredHabitsUiState,
        currentPagerSelected = currentPagerSelected,
        selectedTimeRangeUiState = selectedTimeRange,
        dateSelected = selectedDate,
        navigateToAddHabit = navigateToAddHabit,
        onClickTab = viewModel::onPagerTypeSelected,
        onClickTimeRange = viewModel::onClickTimeRange
    )
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
                        onClickTimeRange = onClickTimeRange
                    )
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