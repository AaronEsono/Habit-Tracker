package aeb.proyecto.statistics.components.common.calendar

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.calendar.content.CalendarContent
import aeb.proyecto.ui.calendar.content.CalendarDays
import aeb.proyecto.ui.calendar.content.CalendarHeader
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.YearMonth

/**
 * Main container component for the habit statistics calendar.
 * Orchestrates the header, day-of-week labels, and the interactive calendar content.
 *
 * @param modifier Applied to the outer container.
 * @param yearMonth The current [YearMonth] being displayed.
 * @param startDayOfWeek The preferred start day for the weekly grid (e.g., Monday).
 * @param calendarUIState Holds the list of [HabitWithDay] items for the current period.
 * @param onMonthChange Callback triggered when the user navigates between months.
 */
@Composable
fun StatisticsCalendar(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth,
    startDayOfWeek: DayOfWeek,
    calendarUIState: CalendarUIState<HabitWithDay>,
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
            modifier = Modifier.fillMaxWidth().padding(top = spacing4, bottom = spacing8),
            yearMonth = yearMonth,
            onPreviousMonthButtonClicked = onMonthChange,
            onNextMonthButtonClicked = onMonthChange
        )

        CalendarDays(
            modifier = Modifier.padding(bottom = spacing4, start = spacing6, end = spacing6),
            startDay = startDayOfWeek
        )

        CalendarContent(
            modifier = Modifier.padding(bottom = spacing6, start = spacing10, end = spacing10),
            dates = calendarUIState.dates,
            verticalPadding = spacing6,
            horizontalPadding = spacing8
        ) { item, modifierItem ->
            CalendarItem(
                modifier = modifierItem,
                day = item?.dateOfMonth ?: yearMonth.atDay(1),
                monthSelected = yearMonth.atDay(1),
                habitWithDay = item?.data
            )
        }
    }

}