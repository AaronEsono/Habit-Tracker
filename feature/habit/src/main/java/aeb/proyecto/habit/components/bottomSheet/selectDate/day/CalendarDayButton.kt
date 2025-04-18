package aeb.proyecto.habit.components.bottomSheet.selectDate.day

import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.background
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
            .wrapContentSize(Alignment.Center)
    ) {
        LabelMediumText(
            date.dateOfMonth.dayOfMonth.toString(),
            textAlign = TextAlign.Center,
            color = isEnabledTextColor(isEnabled = enabled,isDateSelected = isSelectedDate)
        )
    }
}

@Composable
fun Modifier.colorBackgroundDay(isEnabled:Boolean,isDateSelected:Boolean,isToday:Boolean):Modifier =
    if(isEnabled){
        if(isDateSelected){
            this.background(MaterialTheme.colorScheme.onSurface)
        }else if(isToday){
            this.background(MaterialTheme.colorScheme.surfaceContainer)
        }
        else{
            this.background(MaterialTheme.colorScheme.surfaceVariant)
        }
    }
    else
        this.background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.3f))

@Composable
fun isEnabledTextColor(isEnabled:Boolean,isDateSelected: Boolean):Color =
    if(isEnabled){
        if(isDateSelected){
            MaterialTheme.colorScheme.inverseOnSurface
        }else{
            MaterialTheme.colorScheme.onSurface
        }
    }
    else
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)