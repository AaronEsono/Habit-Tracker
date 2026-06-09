package aeb.proyecto.habit.components.common.timeRange.components

import aeb.proyecto.habit.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.month.getMonth
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.time.LocalDate

/**
 * A navigation component for monthly time ranges.
 *
 * Displays the currently selected month and year, providing navigation controls
 * to shift the calendar view forward or backward by one month.
 *
 * @param startOfMonth The [LocalDate] representing the first day of the current month.
 * @param endOfMonth The [LocalDate] representing the last day of the current month.
 * @param onClick Callback triggered when a navigation arrow is clicked,
 * providing the target date and a boolean (typically for navigation direction).
 */
@Composable
fun MonthlyTimeRange(
    startOfMonth: LocalDate,
    endOfMonth: LocalDate,
    onClick: (LocalDate, Boolean) -> Unit
){

    val interactionSource = remember { MutableInteractionSource() }

    Row (
        modifier = Modifier.fillMaxWidth().padding(vertical = spacing12),
        verticalAlignment = Alignment.CenterVertically
    ){
        // Navigation: Previous Month
        Icon(
            Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = "weekly fordward button",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(start = spacing20, end = spacing8)
                .size(25.dp)
                .clickable (
                    interactionSource = interactionSource,
                    indication = null
                ){ onClick(startOfMonth.minusMonths(1), false)}
        )

        // Month and Year Display
        Column (
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            AnimatedContent(
                targetState = startOfMonth
            ) { startOfMonth ->
                // Mes, y año
                LabelLargeText(
                    stringResource(R.string.habit_day_monthly,
                        stringResource(getMonth(startOfMonth.monthValue)),
                        startOfMonth.year.toString(),
                    ))
            }
        }

        // Navigation: Next Month
        Icon(
            Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "weekly fordward button",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(end = spacing20, start = spacing8)
                .size(25.dp)
                .clickable (
                    interactionSource = interactionSource,
                    indication = null
                ){ onClick(endOfMonth.plusMonths(1),false)}
        )
    }

}