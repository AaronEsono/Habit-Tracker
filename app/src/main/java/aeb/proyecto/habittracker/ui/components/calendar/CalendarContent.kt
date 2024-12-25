package aeb.proyecto.habittracker.ui.components.calendar

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import aeb.proyecto.habittracker.ui.components.calendar.calendarComponents.CalendarBottomSheetCanvas
import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Dimmens.spacing10
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CalendarContent(
    dates: List<CalendarUiState.Date>,
    color: Color,
    calendarViewModel: CalendarViewModel,
    isStatistics: Boolean = false,
    onDateClickListener: (CalendarUiState.Date) -> Unit = {},
) {
    Column {
        var index = 0
        if (isStatistics) {
            Row(modifier = Modifier.padding(vertical = spacing4)) {
                Constans.DaysWeek.entries.forEach {
                    LabelMediumText(stringResource(id = it.day), modifier = Modifier.weight(1f))
                }
            }
        }
        repeat(6) {
            Row {
                repeat(7) {
                    val item = if (index < dates.size) dates[index] else CalendarUiState.Date("", false)
                    val habit = calendarViewModel.findHabit(item)
                    val getColor = calendarViewModel.setBackground(habit)

                    ContentItem(
                        date = item,
                        onClickListener = onDateClickListener,
                        modifier = Modifier.weight(1f),
                        color = color,
                        dailyHabit = habit,
                        getColor,
                        isStatistics
                    )
                    index++
                }
            }
        }
    }
}

@Composable
fun ContentItem(
    date: CalendarUiState.Date,
    onClickListener: (CalendarUiState.Date) -> Unit,
    modifier: Modifier = Modifier,
    color: Color,
    dailyHabit: DailyHabit?,
    backgroundColor:Color,
    isStatistics: Boolean = false
) {

    val showCanvas = dailyHabit?.let { it.timesDone != 0 && date.dayOfMonth.isNotEmpty() && !isStatistics } == true

    val modifierBox = if(!isStatistics){
        modifier
            .background(color = if (date.isSelected) color.copy(alpha = 0.5f) else Color.Transparent, shape = CircleShape)
            .padding(spacing10)
            .clickable { onClickListener(date) }
    }else{
        modifier
            .padding(horizontal = spacing8, vertical = spacing4)
            .border(2.dp,backgroundColor, RoundedCornerShape(spacing12))
            .background(color = backgroundColor, RoundedCornerShape(spacing12))
            .aspectRatio(1f)
    }

    Box(
        modifier = modifierBox
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            BodyMediumText(
                text = date.dayOfMonth,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontStyle = if (date.isSelected) FontStyle.Italic else null,
                fontWeight = if (date.isSelected) FontWeight.Bold else null
            )

            if (showCanvas) {
                dailyHabit?.let {
                    CalendarBottomSheetCanvas(color, dailyHabit)
                }
            }
        }
    }
}