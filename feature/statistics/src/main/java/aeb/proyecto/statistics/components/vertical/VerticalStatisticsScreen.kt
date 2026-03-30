package aeb.proyecto.statistics.components.vertical

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.statistics.components.common.loading.StatisticsLoading
import aeb.proyecto.statistics.components.vertical.screens.ContentVerticalStatisticsScreen
import aeb.proyecto.statistics.components.vertical.screens.NoContentVerticalStatisticsScreen
import aeb.proyecto.statistics.model.BoxUIState
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

@Composable
fun VerticalStatisticsScreen(
    statisticsState: StatisticsState,
    boxUIState: List<BoxUIState>,
    graphicsState: GraphicsState,
    yearMonth: YearMonth,
    yearGraphicsSelected: Int,
    startDayOfWeek: DayOfWeek,
    calendarUIState: CalendarUIState<HabitWithDay>,
    onCLickCard: (id:Long) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    onYearSelected: (Boolean) -> Unit = {}
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
                        habitSelected = statisticsState.state.habitSelected,
                        yearMonth = yearMonth,
                        yearGraphicsSelected = yearGraphicsSelected,
                        startDayOfWeek = startDayOfWeek,
                        calendarUIState = calendarUIState,
                        onClickCard = onCLickCard,
                        onMonthChange = onMonthChange,
                        onYearSelected = onYearSelected
                    )
                }
            }
        }
    }
}