package aeb.proyecto.statistics.components.horizontal.screens

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.statistics.components.common.card.HeaderCard
import aeb.proyecto.statistics.components.common.donutChart.PieChartData
import aeb.proyecto.statistics.components.vertical.screens.VerticalHabitSelectedScreen
import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.statistics.model.GoalsDoneState
import aeb.proyecto.statistics.model.GraphicsState
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun ContentHorizontalStatisticsScreen(
    habits: List<Habit>,
    boxUIState: List<BoxUIState>,
    graphicsState: GraphicsState,
    hourlyGraphicsState: GraphicsState,
    goalDoneState: GoalsDoneState,
    pieChartState: List<PieChartData>,
    habitSelected: HabitWithDailyHabit,
    yearMonth: YearMonth,
    yearGraphicsSelected: Int,
    yearHourlyGraphicsSelected: Int,
    startDayOfWeek: DayOfWeek,
    calendarUIState: CalendarUIState<HabitWithDay>,
    onClickCard: (id:Long) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    onYearSelected: (Boolean) -> Unit = {},
    onHourYearSelected: (Boolean) -> Unit = {}
){

    Row(
        modifier = Modifier.fillMaxSize()
    ){

        //Arreglar esto, no va, se quedan los elementos arriba juntos:
        LazyColumn(
            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.1f).padding(horizontal = spacing6),
            verticalArrangement = Arrangement.spacedBy(spacing8)
        ){
            items(
                habits.size,
                key = {habits[it].id}
            ){ index ->
                HeaderCard(
                    habit = habits[index],
                    modifier = Modifier.fillMaxWidth(),
                    selected = habits[index].id == habitSelected.habit.id,
                    onClickCard = onClickCard
                )
            }
        }

        Spacer(modifier = Modifier.padding(start = spacing2))

        VerticalDivider(color = MaterialTheme.colorScheme.outline, thickness = spacing2)

        HorizontalHabitSelectedScreen(
            habitSelected = habitSelected,
            boxUIState = boxUIState,
            graphicsState = graphicsState,
            goalDoneState = goalDoneState,
            pieChartState = pieChartState,
            hourlyGraphicsState = hourlyGraphicsState,
            yearMonth = yearMonth,
            yearGraphicsSelected = yearGraphicsSelected,
            yearHourlyGraphicsSelected = yearHourlyGraphicsSelected,
            startDayOfWeek = startDayOfWeek,
            calendarUIState = calendarUIState,
            onMonthChange = onMonthChange,
            onYearSelected = onYearSelected,
            onHourYearSelected = onHourYearSelected
        )
    }


}