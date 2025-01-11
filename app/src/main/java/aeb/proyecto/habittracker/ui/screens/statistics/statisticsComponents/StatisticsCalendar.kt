package aeb.proyecto.habittracker.ui.screens.statistics.statisticsComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.calendar.CalendarContent
import aeb.proyecto.habittracker.ui.components.calendar.CalendarHeader
import aeb.proyecto.habittracker.ui.components.calendar.CalendarViewModel
import aeb.proyecto.habittracker.ui.components.calendar.calendarComponents.CalendarItemStatistics
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun StatisticsCalendar(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    habitWithDailyHabit: HabitWithDailyHabit
){
    calendarViewModel.setHabitsAndColor(habitWithDailyHabit)

    val calendarState = calendarViewModel.uiState.collectAsState().value

    val yearMonth = calendarState.yearMonth
    val month = Constans.Months.entries.find { it.id == yearMonth.month.value }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = spacing8, vertical = spacing4)
            .border(0.1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(spacing12)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column (
            modifier = Modifier.padding(spacing8)
        ){

            CalendarHeader(
                yearMonth = yearMonth,
                showYear = true,
                titleText = stringResource(month?.month ?: R.string.month_1),
                onPreviousMonthButtonClicked = { calendarViewModel.toPreviousMonth(it) },
                onNextMonthButtonClicked = { calendarViewModel.toNextMonth(it) })

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            CalendarContent(
                calendarState.dates
            ) { date,modifier, _ ->
                val color = calendarViewModel.getColorFromDate(date)

                CalendarItemStatistics(
                    date = date,
                    modifier = modifier,
                    backgroundColor = color
                )
            }
        }
    }
}