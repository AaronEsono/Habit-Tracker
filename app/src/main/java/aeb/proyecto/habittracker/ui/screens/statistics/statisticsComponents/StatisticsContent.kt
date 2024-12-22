package aeb.proyecto.habittracker.ui.screens.statistics.statisticsComponents

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.ui.components.calendar.CalendarViewModel
import aeb.proyecto.habittracker.ui.components.dailyHabit.iconByName
import aeb.proyecto.habittracker.ui.components.radioButton.RadioButtonStatistics
import aeb.proyecto.habittracker.ui.screens.statistics.StatisticsViewModel
import aeb.proyecto.habittracker.ui.theme.DarKThemeText
import aeb.proyecto.habittracker.ui.theme.containerTextFieldColor
import aeb.proyecto.habittracker.ui.theme.textColors
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun StatisticsContent(
    habits: List<Habit>,
    statisticsViewModel: StatisticsViewModel,
    calendarViewModel: CalendarViewModel = hiltViewModel()
) {

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(habits[0]) }

    LaunchedEffect (true){
        onOptionSelected(habits[0])
    }

    Column(modifier = Modifier.fillMaxSize()) {

        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .padding(spacing8)
            .selectableGroup()
            .wrapContentHeight()) {
            items(habits){
                RadioButtonStatistics(it,selectedOption){ habit ->
                    onOptionSelected(habit)
                }
            }
        }
        HorizontalDivider(modifier = Modifier.height(1.5.dp).background(DarKThemeText))

        StatisticsHeader(selectedOption)
    }

//    Column(modifier = Modifier.fillMaxSize().padding(horizontal = spacing8)) {
//        CalendarHeader(
//            yearMonth = calendarState.yearMonth,
//            true,
//            onNextMonthButtonClicked = { if(it.month.value != 1) calendarViewModel.toNextMonth(it) },
//            onPreviousMonthButtonClicked = { if(it.month.value != 12) calendarViewModel.toPreviousMonth(it)  })
//        CalendarContent(calendarState.dates, Color(habits[0].color), calendarViewModel,true) {}
//    }


}