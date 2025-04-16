package aeb.proyecto.habit.components.timeRange

import aeb.proyecto.habit.R
import aeb.proyecto.ui.date.getDay
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.month.getAvrMonth
import aeb.proyecto.ui.month.getMonth
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun WeeklyTimeRange(
    startOfWeek:LocalDate,
    endOfWeek:LocalDate,
    onClick:(LocalDate) -> Unit,
){

    val interactionSource = remember { MutableInteractionSource() }

    Row (
        modifier = Modifier.fillMaxWidth().padding(vertical = spacing12),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = "weekly fordward button",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(start = spacing20, end = spacing6)
                .size(25.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { onClick(startOfWeek.minusDays(7)) }
        )

        Column (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AnimatedContent(
                targetState = Pair(startOfWeek, endOfWeek)
            ) { (startOfWeek,endOfWeek) ->
                // Dia semana, Dia, Mes
                LabelLargeText(stringResource(R.string.habit_day_weekly,
                    stringResource(getDay(startOfWeek.dayOfWeek.name)),
                    startOfWeek.dayOfMonth.toString(),
                    stringResource(getAvrMonth(startOfWeek.monthValue)),

                    stringResource(getDay(endOfWeek.dayOfWeek.name)),
                    endOfWeek.dayOfMonth.toString(),
                    stringResource(getAvrMonth(endOfWeek.monthValue))
                ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            }
        }

        Icon(
            Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "weekly fordward button",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(end = spacing20, start = spacing6)
                .size(25.dp)
                .clickable (
                    interactionSource = interactionSource,
                    indication = null
                ){ onClick(startOfWeek.plusDays(7)) }
        )
    }

}