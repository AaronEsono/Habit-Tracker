package aeb.proyecto.ui.calendar.content

import aeb.proyecto.language.provider.getFirstDayOfWeekByLocale
import aeb.proyecto.ui.date.utils.getOrderedDays
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek

/**
 * Structural week header row displaying localized day name tokens (e.g., Mon, Tue, Wed).
 * Dynamically shifts column ordering schedules based on localized region parameters to support
 * internationalization seamlessly, utilizing weight constraints to bind exactly 7 proportional column vertices.
 *
 * @param modifier The structural composition modifier layout adjustment token.
 * @param horizontalPadding Internal element boundary layout separation spacing.
 * @param startDay Explicit override token to anchor week rows; if null, defaults directly to system locale criteria.
 */
@Composable
fun CalendarDays(
    modifier: Modifier = Modifier,
    horizontalPadding:Dp = 0.dp,
    startDay: DayOfWeek? = null
) {

    val context = LocalContext.current

    // Cache the resolved starting day boundary parameter to prevent locale scanning cycles during redraws
    val firstDay by remember { mutableStateOf(startDay ?: getFirstDayOfWeekByLocale(context)) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
    ) {
        // Enforce rigid physical scaling fractions across 7 proportional sub-slots
        getOrderedDays(firstDay).forEach {
            LabelLargeText(
                stringResource(it.string),
                modifier = Modifier.weight(1f), // Enforces perfect geometric alignment with structural dates below
                textAlign = TextAlign.Center
            )
        }
    }

}