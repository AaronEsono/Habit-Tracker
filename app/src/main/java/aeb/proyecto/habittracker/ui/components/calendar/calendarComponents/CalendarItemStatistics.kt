package aeb.proyecto.habittracker.ui.components.calendar.calendarComponents

import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color

@Composable
fun CalendarItemStatistics(
    date: CalendarUiState.Date,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent
) {

    Box(
        modifier = modifier
            .padding(horizontal = spacing8, vertical = spacing4)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawRoundRect(
                color = backgroundColor,
                cornerRadius = CornerRadius(spacing12.toPx())
            )
        }

        LabelMediumText(
            text = date.dayOfMonth
        )
    }
}