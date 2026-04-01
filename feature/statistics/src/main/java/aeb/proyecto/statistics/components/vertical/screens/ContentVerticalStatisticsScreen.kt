package aeb.proyecto.statistics.components.vertical.screens

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.statistics.components.common.card.HeaderCard
import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.statistics.model.GraphicsState
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun ContentVerticalStatisticsScreen(
    habits: List<Habit>,
    boxUIState: List<BoxUIState>,
    graphicsState: GraphicsState,
    hourlyGraphicsState: GraphicsState,
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

    Column (
        modifier = Modifier.fillMaxSize().padding(top = spacing6)
    ){

        LazyRow(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.1f).padding(horizontal = spacing8),
            horizontalArrangement = Arrangement.spacedBy(spacing8)
        ){
            items(
                habits.size,
                key = {habits[it].id}
            ){ index ->
                HeaderCard(
                    habit = habits[index],
                    modifier = Modifier.weight(1f),
                    selected = habits[index].id == habitSelected.habit.id,
                    onClickCard = onClickCard
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = spacing2))

        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = spacing2)

        VerticalHabitSelectedScreen(
            habitSelected = habitSelected,
            boxUIState = boxUIState,
            graphicsState = graphicsState,
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