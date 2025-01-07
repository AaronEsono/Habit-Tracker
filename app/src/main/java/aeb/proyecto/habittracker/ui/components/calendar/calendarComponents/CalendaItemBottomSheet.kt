package aeb.proyecto.habittracker.ui.components.calendar.calendarComponents

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.utils.Dimmens.spacing10
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ContentItemBottomSheet(
    date: CalendarUiState.Date,
    dailyHabit: DailyHabit?,
    onClickListener: (CalendarUiState.Date) -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
    modifierText: Modifier = Modifier,
    modifierColumn: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .background(
                color = if (date.isSelected) color.copy(alpha = 0.5f) else Color.Transparent,
                shape = CircleShape
            )
            .padding(spacing10)
            .clickable { onClickListener(date) }
    ) {
        Column(
            modifier = modifierColumn.align(Alignment.Center)
        ) {
            BodyMediumText(
                text = date.dayOfMonth,
                modifier = modifierText.align(Alignment.CenterHorizontally),
                fontStyle = if (date.isSelected) FontStyle.Italic else null,
                fontWeight = if (date.isSelected) FontWeight.Bold else null
            )

            dailyHabit?.takeIf { it.timesDone > 0 && date.dayOfMonth.isNotEmpty() }?.let {
                CalendarBottomSheetCanvas(color, it)
            }
        }
    }
}
