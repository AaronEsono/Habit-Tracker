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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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

    val orientation = getOrientation()

    val pagerTypesUIState = viewModel.availablePagerTypesUiState.collectAsStateWithLifecycle().value
    val currentPagerSelected = viewModel.currentPagerType.collectAsStateWithLifecycle().value
    val selectedTimeRange = viewModel.selectedTimeRangeUiState.collectAsStateWithLifecycle().value
    val filteredHabitsUiState = viewModel.habitsForSelectedTimeUiState.collectAsStateWithLifecycle().value
    val selectedDate = viewModel.selectedDate.collectAsStateWithLifecycle().value
    val bottomSheetUIState = viewModel.bottomSheetUIState.collectAsStateWithLifecycle().value
    val startDayOfWeek = viewModel.startDayOfWeek.collectAsStateWithLifecycle().value

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.habit_topbar), fontSize = 20.sp)
    }

    ActionIconHabitScreen(
        selectedTimeRange = selectedTimeRange,
        selectedDate = selectedDate
    )

    val context = LocalContext.current

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
                onBottomSheetSelected = viewModel::onBottomSheetSelected,
                onRestart = viewModel::onRestart,
                onClickTimer = { data ->
                    viewModel.onClickTimerHabit(data){
                        navigateToTimer()
                    }
                },
                onClickConfigureHabit = viewModel::onClickConfigureHabit,
                onDismissBottomSheet = viewModel::onDismissBottomSheet,
                onClickCard = {id ->
                    Toast.makeText(context, "Click en $id", Toast.LENGTH_SHORT).show()
                },
                onLongClick = viewModel::onLongClick,
                onClick = viewModel::onClick
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
                onBottomSheetSelected = viewModel::onBottomSheetSelected,
                onRestart = viewModel::onRestart,
                onClickTimer = { data ->
                    viewModel.onClickTimerHabit(data){
                        navigateToTimer()
                    }
                },
                onClickConfigureHabit = viewModel::onClickConfigureHabit,
                onDismissBottomSheet = viewModel::onDismissBottomSheet,
                onClickCard = {id -> },
                onLongClick = viewModel::onLongClick,
                onClick = viewModel::onClick
            )
        }
    }
}