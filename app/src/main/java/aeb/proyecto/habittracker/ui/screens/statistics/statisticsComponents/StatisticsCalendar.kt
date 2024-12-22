package aeb.proyecto.habittracker.ui.screens.statistics.statisticsComponents

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.calendar.CalendarContent
import aeb.proyecto.habittracker.ui.components.calendar.CalendarHeader
import aeb.proyecto.habittracker.ui.components.calendar.CalendarViewModel
import aeb.proyecto.habittracker.ui.theme.borderTextField
import aeb.proyecto.habittracker.ui.theme.containerTextFieldColor
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun StatisticsCalendar(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    habitWithDailyHabit: HabitWithDailyHabit
){

    val calendarState = calendarViewModel.uiState.collectAsState().value
    calendarViewModel.setHabit(habitWithDailyHabit)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = spacing8, vertical = spacing4)
            .border(0.1.dp, borderTextField, RoundedCornerShape(spacing12)),
        colors = CardDefaults.cardColors(
            containerColor = containerTextFieldColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column (
            modifier = Modifier.padding(spacing8)
        ){
            CalendarHeader(
                yearMonth = calendarState.yearMonth,
                true,
                onNextMonthButtonClicked = {  calendarViewModel.toNextMonth(it) },
                onPreviousMonthButtonClicked = { calendarViewModel.toPreviousMonth(it)  }
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            CalendarContent(calendarState.dates, Color(habitWithDailyHabit.habit.color), calendarViewModel, true)
        }
    }
}