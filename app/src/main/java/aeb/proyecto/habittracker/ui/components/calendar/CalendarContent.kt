package aeb.proyecto.habittracker.ui.components.calendar

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.ui.components.text.CalendarText
import aeb.proyecto.habittracker.ui.theme.DarKThemeText
import aeb.proyecto.habittracker.ui.theme.primaryColorApp
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.tflite.support.Empty
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarContent(
    dates: List<CalendarUiState.Date>,
    color: Color,
    habit: HabitWithDailyHabit,
    yearMonth: YearMonth,
    onDateClickListener: (CalendarUiState.Date) -> Unit,
) {
    Column {
        var index = 0
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
                        yearMonth = yearMonth,
                        dailyHabit = habit.dailyHabits.find { item.dayOfMonth.isNotEmpty() && LocalDate.parse(it.date) == LocalDate.of(yearMonth.year, yearMonth.month, item.dayOfMonth.toInt())
                        }
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
    yearMonth: YearMonth
) {
    Box(
        modifier = modifier
            .background(
                color = if (date.isSelected) color.copy(alpha = 0.5f) else Color.Transparent,
                shape = CircleShape
            )
            .padding(10.dp)
            .clickable {
                if(date.dayOfMonth.isNotEmpty() && LocalDate.of(yearMonth.year, yearMonth.month, date.dayOfMonth.toInt()).isBefore(LocalDate.now().plusDays(1))){
                    onClickListener(date)
                }
            }
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

            Spacer(modifier = Modifier.padding(vertical = spacing2)) // Padding entre texto y canvas

            if (dailyHabit != null && dailyHabit.timesDone != 0 && date.dayOfMonth.isNotEmpty()) {

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