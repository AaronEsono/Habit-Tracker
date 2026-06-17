package aeb.proyecto.statistics

import aeb.proyecto.statistics.components.horizontal.HorizontalStatisticsScreen
import aeb.proyecto.statistics.components.vertical.VerticalStatisticsScreen
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * The root entry point for the Statistics feature. This Composable monitors
 * the device orientation and lifecycle states to delegate UI rendering to either
 * the [VerticalStatisticsScreen] or the [HorizontalStatisticsScreen].
 *
 * @param viewModel The Hilt-injected ViewModel managing the statistics data flow.
 */
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
){

    // Scaffold title setup
    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.topbar_habit), fontSize = 20.sp)
    }

    // Observing orientation and all state flows from the ViewModel
    val orientation = getOrientation()
    val statisticsState = viewModel.statisticsState.collectAsStateWithLifecycle().value
    val yearMonth = viewModel.yearMonth.collectAsStateWithLifecycle().value
    val calendarState = viewModel.calendarUIState.collectAsStateWithLifecycle().value
    val startDayOfWeek = viewModel.dayOfWeek.collectAsStateWithLifecycle().value
    val boxUIState = viewModel.boxUIState.collectAsStateWithLifecycle().value
    val graphicsState = viewModel.graphicsState.collectAsStateWithLifecycle().value
    val hourlyGraphicsState = viewModel.hourlyGraphicsState.collectAsStateWithLifecycle().value
    val yearHourlyGraphicsSelected = viewModel.yearHourlyGraphicsSelected.collectAsStateWithLifecycle().value
    val yearGraphicsSelected = viewModel.yearGraphicsSelected.collectAsStateWithLifecycle().value
    val goalsDoneState = viewModel.goalsDoneState.collectAsStateWithLifecycle().value
    val pieChartState = viewModel.pieChartState.collectAsStateWithLifecycle().value

    // Adaptive layout routing based on device orientation
    when(orientation){
        Orientation.Portrait -> {
            VerticalStatisticsScreen(
                statisticsState = statisticsState,
                boxUIState = boxUIState,
                graphicsState = graphicsState,
                hourlyGraphicsState = hourlyGraphicsState,
                goalDoneState = goalsDoneState,
                pieChartState = pieChartState,
                yearMonth = yearMonth,
                yearHourlyGraphicsSelected = yearHourlyGraphicsSelected,
                yearGraphicsSelected = yearGraphicsSelected,
                startDayOfWeek = startDayOfWeek,
                calendarUIState = calendarState,
                onCLickCard = viewModel::onCLickCard,
                onMonthChange = viewModel::onMonthButtonClicked,
                onYearSelected = viewModel::onYearSelected,
                onHourYearSelected = viewModel::onHourYearSelected
            )
        }
        Orientation.Landscape -> {
            HorizontalStatisticsScreen(
                statisticsState = statisticsState,
                boxUIState = boxUIState,
                graphicsState = graphicsState,
                hourlyGraphicsState = hourlyGraphicsState,
                goalDoneState = goalsDoneState,
                pieChartState = pieChartState,
                yearMonth = yearMonth,
                yearHourlyGraphicsSelected = yearHourlyGraphicsSelected,
                yearGraphicsSelected = yearGraphicsSelected,
                startDayOfWeek = startDayOfWeek,
                calendarUIState = calendarState,
                onCLickCard = viewModel::onCLickCard,
                onMonthChange = viewModel::onMonthButtonClicked,
                onYearSelected = viewModel::onYearSelected,
                onHourYearSelected = viewModel::onHourYearSelected
            )
        }
    }
}