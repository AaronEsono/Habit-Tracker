package aeb.proyecto.habit

import aeb.proyecto.habit.components.common.navigationIcon.ActionIconHabitScreen
import aeb.proyecto.habit.components.horizontal.HorizontalHabitScreen
import aeb.proyecto.habit.components.vertical.VerticalHabitScreen
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// NOTA: Iconos de flaticon y svgRepo, poner que los he utilizado y darles credito
// NOTA: Dar credito a la libreria utilizada en la pantalla de estadisticas

// Mirar en un futuro el horizontalRow con los dias, optimizarlo
// Mirar en addHabit como hacer que se deslice los mensuales

/**
 * The entry point for the Habit dashboard screen.
 *
 * This screen is responsible for observing the [HabitViewModel] state and
 * delegating the rendering logic based on the device's current orientation.
 * It acts as a "Fat Screen" or Controller, transforming the reactive stream
 * of data into either a [VerticalHabitScreen] or a [HorizontalHabitScreen].
 *
 * @param viewModel The ViewModel providing the reactive UI state.
 * @param navigateToAddHabit Action to route the user to the habit creation/edit flow.
 * @param navigateToTimer Action to route the user to the active timer screen.
 */
@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    navigateToAddHabit: (Long) -> Unit,
    navigateToTimer : () -> Unit
){

    // Detect device orientation to determine the UI layout strategy
    val orientation = getOrientation()

    // Collect all relevant UI states from the ViewModel
    val pagerTypesUIState = viewModel.availablePagerTypesUiState.collectAsStateWithLifecycle().value
    val currentPagerSelected = viewModel.currentPagerType.collectAsStateWithLifecycle().value
    val selectedTimeRange = viewModel.selectedTimeRangeUiState.collectAsStateWithLifecycle().value
    val filteredHabitsUiState = viewModel.habitsForSelectedTimeUiState.collectAsStateWithLifecycle().value
    val selectedDate = viewModel.selectedDate.collectAsStateWithLifecycle().value
    val bottomSheetUIState = viewModel.bottomSheetUIState.collectAsStateWithLifecycle().value
    val startDayOfWeek = viewModel.startDayOfWeek.collectAsStateWithLifecycle().value

    // Configure the AppBar
    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.habit_topbar), fontSize = 20.sp)
    }

    // Configure the context-aware action icons
    ActionIconHabitScreen(
        selectedTimeRange = selectedTimeRange,
        selectedDate = selectedDate
    )

    // Strategy Pattern: Switch UI based on orientation
    when(orientation){
        Orientation.Portrait -> {
            VerticalHabitScreen(
                pagerTypesUIState = pagerTypesUIState,
                filteredHabitsUiState = filteredHabitsUiState,
                currentPagerSelected = currentPagerSelected,
                selectedTimeRangeUiState = selectedTimeRange,
                startDayOfWeek = startDayOfWeek,
                dateSelected = selectedDate,
                bottomSheetUIState = bottomSheetUIState,
                navigateToAddHabit = navigateToAddHabit,
                onClickTab = viewModel::onPagerTypeSelected,
                onClickTimeRange = viewModel::onClickTimeRange,
                onBottomSheetSelectDateSelected = viewModel::onBottomSheetSelectDateSelected,
                onRestart = viewModel::onRestart,
                onClickTimer = { data ->
                    viewModel.onClickTimerHabit(data){
                        navigateToTimer()
                    }
                },
                onClickConfigureHabit = viewModel::onClickConfigureHabit,
                onDismissBottomSheet = viewModel::onDismissBottomSheet,
                onClickCard = viewModel::onClickCard,
                onLongClick = viewModel::onLongClick,
                onClick = viewModel::onClick,
                onClickEdit = { id ->
                    navigateToAddHabit(id)
                },
                onClickDelete = viewModel::onClickDelete,
                onAcceptDeleteHabit = viewModel::onAcceptDeleteHabit,
            )
        }
        Orientation.Landscape -> {
            HorizontalHabitScreen(
                pagerTypesUIState = pagerTypesUIState,
                filteredHabitsUiState = filteredHabitsUiState,
                currentPagerSelected = currentPagerSelected,
                selectedTimeRangeUiState = selectedTimeRange,
                bottomSheetUIState = bottomSheetUIState,
                startDayOfWeek = startDayOfWeek,
                dateSelected = selectedDate,
                navigateToAddHabit = navigateToAddHabit,
                onClickTab = viewModel::onPagerTypeSelected,
                onClickTimeRange = viewModel::onClickTimeRange,
                onBottomSheetSelectDateSelected = viewModel::onBottomSheetSelectDateSelected,
                onRestart = viewModel::onRestart,
                onClickTimer = { data ->
                    viewModel.onClickTimerHabit(data){
                        navigateToTimer()
                    }
                },
                onClickConfigureHabit = viewModel::onClickConfigureHabit,
                onDismissBottomSheet = viewModel::onDismissBottomSheet,
                onClickCard = viewModel::onClickCard,
                onLongClick = viewModel::onLongClick,
                onClick = viewModel::onClick,
                onClickEdit = { id ->
                    navigateToAddHabit(id)
                },
                onClickDelete = viewModel::onClickDelete,
                onAcceptDeleteHabit = viewModel::onAcceptDeleteHabit,
            )
        }
    }
}