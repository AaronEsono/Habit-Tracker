package aeb.proyecto.statistics.components.vertical

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.statistics.components.common.donutChart.PieChartData
import aeb.proyecto.statistics.components.common.loading.StatisticsLoading
import aeb.proyecto.statistics.components.vertical.screens.ContentVerticalStatisticsScreen
import aeb.proyecto.statistics.components.vertical.screens.NoContentVerticalStatisticsScreen
import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.statistics.model.GoalsDoneState
import aeb.proyecto.statistics.model.GraphicsState
import aeb.proyecto.statistics.model.StatisticsState
import aeb.proyecto.statistics.model.StatisticsSuccessState
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.DayOfWeek
import java.time.YearMonth

/**
 * Entry point for the Statistics module in vertical orientation.
 * Acts as a state controller, delegating the UI rendering to the appropriate
 * composable based on the current [StatisticsState].
 *
 * @param statisticsState The current state of the statistics data (Loading, Success, Error).
 * @param boxUIState Statistics data for the weekly/daily view.
 * @param graphicsState State for the trends/graphics view.
 * @param hourlyGraphicsState State for the hourly distribution view.
 * @param goalDoneState Progress metrics for goals.
 * @param pieChartState Data for the donut/pie chart distribution.
 * @param yearMonth Current [YearMonth] for the calendar view.
 * @param yearGraphicsSelected Year selected for annual trends.
 * @param yearHourlyGraphicsSelected Year selected for hourly trends.
 * @param startDayOfWeek User-defined start day of the week.
 * @param calendarUIState State holding calendar date information.
 * @param onCLickCard Callback when a user selects a habit from the list.
 * @param onMonthChange Callback for calendar month navigation.
 * @param onYearSelected Callback for annual graph navigation.
 * @param onHourYearSelected Callback for hourly graph navigation.
 */
@Composable
fun VerticalStatisticsScreen(
    statisticsState: StatisticsState,
    boxUIState: List<BoxUIState>,
    graphicsState: GraphicsState,
    hourlyGraphicsState: GraphicsState,
    goalDoneState: GoalsDoneState,
    pieChartState: List<PieChartData>,
    yearMonth: YearMonth,
    yearGraphicsSelected: Int,
    yearHourlyGraphicsSelected: Int,
    startDayOfWeek: DayOfWeek,
    calendarUIState: CalendarUIState<HabitWithDay>,
    onCLickCard: (id:Long) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    onYearSelected: (Boolean) -> Unit = {},
    onHourYearSelected: (Boolean) -> Unit = {}
){

    when(statisticsState){
        is StatisticsState.Error -> Unit
        StatisticsState.Loading -> {
            StatisticsLoading()
        }
        is StatisticsState.Success -> {
            when(statisticsState.state){
                StatisticsSuccessState.Empty -> {
                    NoContentVerticalStatisticsScreen()
                }
                is StatisticsSuccessState.Habits ->{
                    ContentVerticalStatisticsScreen(
                        habits = statisticsState.state.habits,
                        boxUIState = boxUIState,
                        graphicsState = graphicsState,
                        goalDoneState = goalDoneState,
                        pieChartState = pieChartState,
                        hourlyGraphicsState = hourlyGraphicsState,
                        habitSelected = statisticsState.state.habitSelected,
                        yearMonth = yearMonth,
                        yearGraphicsSelected = yearGraphicsSelected,
                        yearHourlyGraphicsSelected = yearHourlyGraphicsSelected,
                        startDayOfWeek = startDayOfWeek,
                        calendarUIState = calendarUIState,
                        onClickCard = onCLickCard,
                        onMonthChange = onMonthChange,
                        onYearSelected = onYearSelected,
                        onHourYearSelected = onHourYearSelected
                    )
                }
            }
        }
    }
}