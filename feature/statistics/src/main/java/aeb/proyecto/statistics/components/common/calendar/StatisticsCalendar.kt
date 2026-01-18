package aeb.proyecto.statistics.components.common.calendar

import aeb.proyecto.ui.calendar.content.CalendarDays
import aeb.proyecto.ui.calendar.content.CalendarHeader
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun StatisticsCalendar(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth,
    startDayOfWeek: DayOfWeek,
    onMonthChange: (YearMonth) -> Unit,
){


    Column(
        modifier = modifier
            .clip(RoundedCornerShape(spacing6))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(spacing6))
            .background(MaterialTheme.colorScheme.surfaceTint)
            .fillMaxWidth(),
    ) {

        CalendarHeader(
            modifier = Modifier.padding(top = spacing4, bottom = spacing8),
            yearMonth = yearMonth,
            onPreviousMonthButtonClicked = onMonthChange,
            onNextMonthButtonClicked = onMonthChange
        )

        CalendarDays(
            modifier = Modifier.padding(bottom = spacing4),
            startDay = startDayOfWeek
        )


        // y aqui el cuerpo
    }

}