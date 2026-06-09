package aeb.proyecto.habit.components.common.bottomSheet.selectDate

import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import java.time.LocalDate

/**
 * A selectable day button component for the calendar view.
 *
 * Renders an individual day within the calendar, visually indicating its state
 * (enabled/disabled, selected, or current day) and handling click events to
 * trigger date selection.
 *
 * @param modifier Modifier to be applied to the button layout.
 * @param enabled Whether the button is interactable.
 * @param isSelectedDate Whether the current date matches the user's selected date.
 * @param date The [CalendarUIState.DateCalendar] object containing date metadata.
 * @param onClick Callback function invoked when the date is clicked, returning the [LocalDate].
 */
@Composable
fun CalendarDayButton(
    modifier: Modifier = Modifier,
    enabled:Boolean,
    isSelectedDate:Boolean,
    date: CalendarUIState.DateCalendar<Unit>,
    onClick:(LocalDate) -> Unit = {},
){
    TextButton(
        onClick = {onClick(date.dateOfMonth)},
        enabled = enabled,
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(spacing12))
            .colorBackgroundDay(isEnabled = enabled,isSelectedDate,
                isToday = date.dateOfMonth == LocalDate.now())
            .wrapContentSize(Alignment.Center),
            contentPadding = PaddingValues()
    ) {
        LabelLargeText(
            date.dateOfMonth.dayOfMonth.toString(),
            textAlign = TextAlign.Center,
            color = isEnabledTextColor(isEnabled = enabled,isDateSelected = isSelectedDate,
                isToday = date.dateOfMonth == LocalDate.now())
        )
    }
}

/**
 * Modifier extension to apply background colors based on the day's state.
 *
 * @param isEnabled Whether the day is enabled.
 * @param isDateSelected Whether the day is currently selected.
 * @param isToday Whether the day represents the current system date.
 * @return The modified [Modifier] with the appropriate background color.
 */
@Composable
fun Modifier.colorBackgroundDay(isEnabled:Boolean,isDateSelected:Boolean,isToday:Boolean):Modifier =
    if(isEnabled){
        if(isDateSelected){
            this.background(MaterialTheme.colorScheme.onSurface)
        }else if(isToday){
            this.background(MaterialTheme.colorScheme.surfaceContainerLow)
        }
        else{
            this.background(MaterialTheme.colorScheme.surfaceVariant)
        }
    }
    else
        this.background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.3f))

/**
 * Determines the text color for the day button based on its state.
 *
 * @param isEnabled Whether the day is enabled.
 * @param isDateSelected Whether the day is currently selected.
 * @param isToday Whether the day represents the current system date.
 * @return The calculated [Color] for the text.
 */
@Composable
fun isEnabledTextColor(isEnabled:Boolean,isDateSelected: Boolean,isToday: Boolean = false):Color =
    if(isEnabled){
        if(isDateSelected){
            MaterialTheme.colorScheme.inverseOnSurface
        }else if(isToday){
            MaterialTheme.colorScheme.inverseOnSurface
        }
        else{
            MaterialTheme.colorScheme.onSurface
        }
    }
    else
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)