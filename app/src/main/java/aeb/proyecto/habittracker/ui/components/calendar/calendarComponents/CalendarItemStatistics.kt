package aeb.proyecto.habittracker.ui.components.calendar.calendarComponents

import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun CalendarItemStatistics(
    date: CalendarUiState.Date,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(spacing12),
    backgroundColor: Color = Color.Transparent,
    modifierText: Modifier = Modifier,
    modifierColumn: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .padding(horizontal = spacing8, vertical = spacing4)
            .border(2.dp,backgroundColor, shape)
            .background(color = backgroundColor, shape)
            .aspectRatio(1f)
    ) {
        Column(
            modifier = modifierColumn.align(Alignment.Center)
        ) {
            BodyMediumText(
                text = date.dayOfMonth,
                modifier = modifierText.align(Alignment.CenterHorizontally)
            )
        }
    }
}