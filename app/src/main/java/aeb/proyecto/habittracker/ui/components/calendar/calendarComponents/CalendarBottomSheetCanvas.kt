package aeb.proyecto.habittracker.ui.components.calendar.calendarComponents

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.ui.components.text.CalendarText
import aeb.proyecto.habittracker.ui.theme.primaryColorApp
import aeb.proyecto.habittracker.ui.theme.secondaryColorApp
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill

@Composable
fun CalendarBottomSheetCanvas(
    color: Color,
    dailyHabit: DailyHabit
) {
    Spacer(modifier = Modifier.padding(vertical = spacing2))

    Card(
        modifier = Modifier
            .wrapContentSize(),
        colors = CardDefaults.cardColors(
            contentColor = primaryColorApp,
            containerColor = primaryColorApp
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