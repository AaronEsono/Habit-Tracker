package aeb.proyecto.habit.components.common.timeRange.components

import aeb.proyecto.habit.components.common.timeRange.components.dayCard.DayCard
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import java.time.LocalDate

val horizontalPadding = spacing6

@Composable
fun DailyTimeRange(
    selectedDate:LocalDate,
    numberOfElements:Int = 8,
    daysOnRange:List<LocalDate>,
    onClick: (LocalDate) -> Unit = {}
){

    val screenWidthDp  = LocalConfiguration.current.screenWidthDp.dp

    val itemSize = remember {
        val totalSpacing = horizontalPadding * (numberOfElements - 1)
        (screenWidthDp - totalSpacing) / numberOfElements
    }
    val state = rememberLazyListState(
        initialFirstVisibleItemIndex = daysOnRange.indexOf(selectedDate) - (numberOfElements / 2).coerceAtLeast(0) + 1
    )

    LazyRow (
        state = state,
        modifier = Modifier.padding(vertical = spacing6),
        contentPadding = PaddingValues(horizontal = itemSize / 2),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding),
    ){
        items(daysOnRange.size, key = {it}){ index ->
            DayCard(
                modifier = Modifier
                    .width(itemSize),
                date = daysOnRange[index],
                isSelected = selectedDate(daysOnRange[index], selectedDate),
                onClick = onClick
            )
        }
    }
}

fun selectedDate(day:LocalDate,selectedDate:LocalDate):Boolean{
    return day == selectedDate
}