package aeb.proyecto.habit.components.common.timeRange.components

import aeb.proyecto.habit.components.common.timeRange.components.dayCard.DayCard
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import java.time.LocalDate

val horizontalPadding = spacing6

/**
 * A horizontal scrollable list component for daily date selection.
 *
 * This component displays a range of dates as selectable cards. It calculates
 * the appropriate card size based on the screen width and the number of
 * visible elements, and initializes the scroll position to keep the
 * [selectedDate] centrally focused.
 *
 * @param selectedDate The currently active/selected date.
 * @param numberOfElements The number of items to display simultaneously.
 * @param daysOnRange The complete list of [LocalDate] to render in the row.
 * @param onClick Callback triggered when a day card is clicked,
 * providing the selected date and a selection boolean.
 */
@Composable
fun DailyTimeRange(
    selectedDate:LocalDate,
    numberOfElements:Int = 8,
    daysOnRange:List<LocalDate>,
    onClick: (LocalDate, Boolean) -> Unit = {_,_ -> }
){

    val screenWidthDp  = LocalConfiguration.current.screenWidthDp.dp

    // Dynamically calculate individual card width to fit the screen nicely
    val itemSize = remember {
        val totalSpacing = horizontalPadding * (numberOfElements - 1)
        (screenWidthDp - totalSpacing) / numberOfElements
    }

    // Attempt to center the selected date by adjusting the initial scroll index
    val state = remember(daysOnRange) {
        LazyListState(
            firstVisibleItemIndex =
                (daysOnRange.indexOf(selectedDate) - (numberOfElements / 2).coerceAtLeast(0) + 1)
                    .coerceAtLeast(0)
        )
    }

    LazyRow (
        state = state,
        modifier = Modifier
            .padding(vertical = spacing6)
            .testTag("habit_dailyTimeRange"),
        contentPadding = PaddingValues(horizontal = itemSize / 2),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding),
    ){
        items(daysOnRange.size, key = {it}){ index ->
            DayCard(
                modifier = Modifier
                    .width(itemSize)
                    .testTag("habit_day_card_${daysOnRange[index]}"),
                date = daysOnRange[index],
                isSelected = selectedDate(daysOnRange[index], selectedDate),
                onClick = onClick
            )
        }
    }
}

/**
 * Helper to determine if a specific day is the currently selected one.
 */
fun selectedDate(day:LocalDate,selectedDate:LocalDate):Boolean{
    return day == selectedDate
}