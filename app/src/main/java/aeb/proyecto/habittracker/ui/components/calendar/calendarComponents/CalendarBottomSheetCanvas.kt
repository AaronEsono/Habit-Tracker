package aeb.proyecto.habittracker.ui.components.calendar.calendarComponents

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.ui.components.text.CalendarText
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing3
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing6
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill

@Composable
fun CalendarBottomSheetCanvas(
    colorCircleCanvas: Color,
    dailyHabit: DailyHabit,
    modifierCard: Modifier = Modifier,
    modifierCanvas: Modifier = Modifier,
    modifierText: Modifier = Modifier,
    modifierRow: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.tertiaryContainer
) {
    Spacer(modifier = Modifier.padding(vertical = spacing2))

    Card(
        modifier = modifierCard
            .wrapContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = modifierRow
                .wrapContentSize()
                .padding(horizontal = spacing4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Canvas(modifier = modifierCanvas.size(spacing6)) {
                drawCircle(
                    color = colorCircleCanvas,
                    radius = size.minDimension / 2,
                    style = Fill
                )
            }

            CalendarText(
                "${dailyHabit.timesDone}",
                modifier = modifierText.padding(start = spacing3)
            )
        }
    }
}