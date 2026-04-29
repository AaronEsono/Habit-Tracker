package aeb.proyecto.statistics.components.horizontal

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.statistics.components.common.donutChart.PieChartData
import aeb.proyecto.statistics.components.common.loading.StatisticsLoading
import aeb.proyecto.statistics.components.horizontal.screens.ContentHorizontalStatisticsScreen
import aeb.proyecto.statistics.components.horizontal.screens.NoContentHorizontalStatisticsScreen
import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.statistics.model.GoalsDoneState
import aeb.proyecto.statistics.model.GraphicsState
import aeb.proyecto.statistics.model.StatisticsState
import aeb.proyecto.statistics.model.StatisticsSuccessState
import aeb.proyecto.ui.calendar.model.CalendarUIState
import androidx.compose.runtime.Composable
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun HorizontalStatisticsScreen(
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
                    NoContentHorizontalStatisticsScreen()
                }
                is StatisticsSuccessState.Habits ->{
                    ContentHorizontalStatisticsScreen(
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