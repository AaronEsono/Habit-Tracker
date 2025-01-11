package aeb.proyecto.habittracker.ui.components.calendar

import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun CalendarContent(
    dates: List<CalendarUiState.Date>,
    showDays: Boolean = false,
    modifierRow: Modifier = Modifier,
    modifierRowText: Modifier = Modifier,
    onDateClickListener: (CalendarUiState.Date) -> Unit = {},
    contentItem: @Composable (CalendarUiState.Date,Modifier,(CalendarUiState.Date) -> Unit) -> Unit
) {
    Column {
        if (showDays) {
            DaysHeader(modifierRow, modifierRowText)
        }

        val totalDays = remember { 6 * 7  }
        val filledDates = dates + List(totalDays - dates.size) { CalendarUiState.Date("", false) } // Rellenar si falta

        val weeks = filledDates.chunked(7)

        weeks.forEach { week ->
            Row {
                week.forEach { date ->
                    contentItem(date, Modifier.weight(1f),onDateClickListener)
                }
            }
        }
    }
}

@Composable
fun DaysHeader(modifierRow: Modifier, modifierRowText: Modifier) {
    Row(modifier = modifierRow.padding(vertical = spacing4)) {
        Constans.DaysWeek.entries.forEach {
            LabelMediumText(stringResource(id = it.day), modifier = modifierRowText.weight(1f))
        }
    }
}