package aeb.proyecto.ui.calendar.content

import aeb.proyecto.language.model.getFirstDayOfWeekByLocale
import aeb.proyecto.ui.date.getOrderedDays
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun CalendarDays(
    modifier: Modifier = Modifier,
    horizontalPadding:Dp = 0.dp
) {

    val context = LocalContext.current
    val firstDay by remember { mutableStateOf(getFirstDayOfWeekByLocale(context)) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
    ) {
        getOrderedDays(firstDay).forEach {
            LabelLargeText(
                stringResource(it.string),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }

}