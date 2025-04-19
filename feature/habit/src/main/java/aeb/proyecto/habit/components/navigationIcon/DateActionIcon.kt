package aeb.proyecto.habit.components.navigationIcon

import aeb.proyecto.habit.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.month.getAvrMonth
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DateActionIcon(
    selectedDate:LocalDate
){
    Box(
        modifier = Modifier
            .offset(x = (-4).dp)
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            .background(Color.White)
    ) {
        AnimatedContent(
            targetState = selectedDate
        ) { selectedDate ->
            LabelLargeText(
                getTextActionIcon(selectedDate),
                color = Color.Black,
                modifier = Modifier.padding(
                    top = spacing4,
                    end = spacing20,
                    bottom = spacing4,
                    start = spacing8
                )
            )
        }
    }
}

@Composable
fun getTextActionIcon(date:LocalDate):String{
    return when(date){
        LocalDate.now() -> stringResource(R.string.habit_today)
        LocalDate.now().plusDays(1) ->  stringResource(R.string.habit_tomorrow)
        LocalDate.now().minusDays(1) -> stringResource(R.string.habit_yesterday)
        else -> {
            stringResource(
                R.string.habit_action_icon,
                date.dayOfMonth.toString(),
                stringResource(getAvrMonth(date.month.value)),
                date.year.toString()
            )
        }
    }
}