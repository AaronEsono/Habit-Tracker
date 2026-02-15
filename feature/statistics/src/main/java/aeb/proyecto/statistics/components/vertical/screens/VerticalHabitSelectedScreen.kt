package aeb.proyecto.statistics.components.vertical.screens

import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.statistics.components.common.calendar.StatisticsCalendar
import aeb.proyecto.statistics.components.common.header.HeaderTitle
import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.text.LabelMediumText
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.DayOfWeek
import java.time.YearMonth

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun VerticalHabitSelectedScreen(
    habitSelected: HabitWithDailyHabit,
    boxUIState: List<BoxUIState>,
    yearMonth: YearMonth,
    startDayOfWeek: DayOfWeek,
    calendarUIState: CalendarUIState<HabitWithDay>,
    onMonthChange: (YearMonth) -> Unit
){


    BoxWithConstraints {
        val headerHeight = maxHeight * 0.07f

        Column (
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(top = spacing6)
        ){

            HeaderTitle(
                habit = habitSelected.habit,
                modifier = Modifier.height(headerHeight)
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            StatisticsCalendar(
                modifier = Modifier.padding(horizontal = spacing4),
                yearMonth = yearMonth,
                startDayOfWeek = startDayOfWeek,
                calendarUIState = calendarUIState,
                onMonthChange = onMonthChange
            )
        }
    }

}