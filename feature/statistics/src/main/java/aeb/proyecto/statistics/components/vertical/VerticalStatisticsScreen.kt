package aeb.proyecto.statistics.components.vertical

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.statistics.components.common.loading.StatisticsLoading
import aeb.proyecto.statistics.components.vertical.screens.ContentVerticalStatisticsScreen
import aeb.proyecto.statistics.components.vertical.screens.NoContentVerticalStatisticsScreen
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
    yearMonth: YearMonth,
    startDayOfWeek: DayOfWeek,
    calendarUIState: CalendarUIState<HabitWithDay>,
    onCLickCard: (id:Long) -> Unit,
    onMonthChange: (YearMonth) -> Unit
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
                        habitSelected = statisticsState.state.habitSelected,
                        yearMonth = yearMonth,
                        startDayOfWeek = startDayOfWeek,
                        calendarUIState = calendarUIState,
                        onClickCard = onCLickCard,
                        onMonthChange = onMonthChange
                    )
                }
            }
        }
    }
}