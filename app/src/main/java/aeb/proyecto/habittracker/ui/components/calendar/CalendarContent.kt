package aeb.proyecto.habittracker.ui.components.calendar

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@Composable
fun CalendarContent(
    dates: List<CalendarUiState.Date>,
    calendarViewModel: CalendarViewModel,
    color: Color,
    showDays: Boolean = false,
    modifierRow: Modifier = Modifier,
    modifierRowText: Modifier = Modifier,
    onDateClickListener: (CalendarUiState.Date) -> Unit = {},
    contentItem: @Composable (CalendarUiState.Date, DailyHabit?, Color,Color, Modifier, (CalendarUiState.Date) -> Unit) -> Unit
) {
    Column {
        var index = 0
        if (showDays) {
            Row(modifier = modifierRow.padding(vertical = spacing4)) {
                Constans.DaysWeek.entries.forEach {
                    LabelMediumText(stringResource(id = it.day), modifier = modifierRowText.weight(1f))
                }
            }
        }

        repeat(6) {
            Row {
                repeat(7) {
                    val item = if (index < dates.size) dates[index] else CalendarUiState.Date("", false)
                    val habit = calendarViewModel.findHabit(item)
                    val getColor = calendarViewModel.setBackground(habit)

                    contentItem(item, habit, color, getColor, Modifier.weight(1f), onDateClickListener)
                    index++
                }
            }
        }
    }
}