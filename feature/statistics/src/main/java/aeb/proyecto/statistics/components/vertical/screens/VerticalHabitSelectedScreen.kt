package aeb.proyecto.statistics.components.vertical.screens

import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.statistics.components.common.calendar.StatisticsCalendar
import aeb.proyecto.statistics.components.common.header.HeaderTitle
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun VerticalHabitSelectedScreen(
    habitSelected: HabitWithDailyHabit,
    yearMonth: YearMonth,
    startDayOfWeek: DayOfWeek,
    calendarUIState: CalendarUIState<HabitWithDay>,
    onMonthChange: (YearMonth) -> Unit
){
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(top = spacing6)
    ){

        HeaderTitle(
            habit = habitSelected.habit,
            modifier = Modifier.fillMaxHeight(0.07f)
        )

        Spacer(modifier = Modifier.padding(vertical = spacing4))

        StatisticsCalendar(
            modifier = Modifier.padding(horizontal = spacing4),
            yearMonth = yearMonth,
            startDayOfWeek = startDayOfWeek,
            onMonthChange = onMonthChange
        )

    }
}