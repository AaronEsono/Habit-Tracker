package aeb.proyecto.ui.calendar.content

import aeb.proyecto.ui.calendar.model.CalendarUIState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun <T> CalendarContent(
    modifier:Modifier = Modifier,
    verticalPadding:Dp = 0.dp,
    horizontalPadding:Dp = 0.dp,
    dates: List<CalendarUIState.DateCalendar<T>>,
    itemContent: @Composable (CalendarUIState.DateCalendar<T>?,modifier: Modifier) -> Unit
){

    Column (
        modifier = modifier
    ){
        var index = 0
        repeat(6) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = verticalPadding),
                horizontalArrangement = Arrangement.spacedBy(horizontalPadding)){
                repeat(7) {
                    val item = if (index < dates.size) dates[index] else null
                    itemContent(item,Modifier.weight(1f))
                    index++
                }
            }
        }
    }
}

fun LocalDate.isInYearMonth(yearMonth: YearMonth): Boolean {
    return this.year == yearMonth.year && this.month == yearMonth.month
}