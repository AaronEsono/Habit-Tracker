package aeb.proyecto.habittracker.ui.components.calendar

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.ui.components.text.CalendarText
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.theme.primaryColorApp
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing3
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing6
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
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
    onDateClickListener: (CalendarUiState.Date) -> Unit,
) {
    Column {
        var index = 0
        if(isStatistics){
            Row(modifier = Modifier.padding(vertical = spacing4)){
                Constans.DaysWeek.entries.forEach {
                    LabelMediumText(stringResource(id = it.day), modifier = Modifier.weight(1f))
                }
            }
        }
        repeat(6) {
            Row {
                repeat(7) {
                    val item =
                        if (index < dates.size) dates[index] else CalendarUiState.Date("", false)
                    ContentItem(
                        date = item,
                        onClickListener = onDateClickListener,
                        modifier = Modifier.weight(1f),
                        color = color,
                        isStatistics = isStatistics,
                        dailyHabit = calendarViewModel.findHabit(item)
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
    isStatistics: Boolean = false,
    dailyHabit: DailyHabit?,
) {
    var modifierCalendar = modifier

    if(!isStatistics){
        modifierCalendar = modifierCalendar.background(
            color = if (date.isSelected) color.copy(alpha = 0.5f) else Color.Transparent,
            shape = CircleShape
        )
            .padding(10.dp)
            .clickable {
                onClickListener(date)
            }
    }else{
        modifierCalendar = modifierCalendar
            .padding(horizontal = spacing8, vertical = spacing4)
            .aspectRatio(1f)

        val color = if(dailyHabit != null && dailyHabit.timesDone > 1) color.copy(alpha = 0.6f) else color.copy(alpha = 0.2f)

        if(dailyHabit != null && dailyHabit.timesDone != 0){
            modifierCalendar = modifierCalendar
                .border(2.dp, color, RoundedCornerShape(spacing12))
                .background(
                color = color,
                shape = RoundedCornerShape(spacing12)
            )
        }

    }

    Box(
        modifier = modifierCalendar
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

            if (dailyHabit != null && dailyHabit.timesDone != 0 && date.dayOfMonth.isNotEmpty() && !isStatistics) {
                Spacer(modifier = Modifier.padding(vertical = spacing2)) // Padding entre texto y canvas

                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterHorizontally),
                    colors = CardDefaults.cardColors(
                        contentColor = primaryColorApp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = spacing4),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Canvas(modifier = Modifier.size(spacing6)) {
                            drawCircle(
                                color = color,
                                radius = size.minDimension / 2,
                                style = Fill
                            )
                        }

                        CalendarText(
                            "${dailyHabit.timesDone}",
                            modifier = Modifier.padding(start = spacing3)
                        )
                    }
                }
            }
        }
    }
}