package aeb.proyecto.ui.calendar.content

import aeb.proyecto.ui.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.month.getAvrMonth
import aeb.proyecto.ui.month.getMonth
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import java.time.YearMonth

@Composable
fun CalendarHeader(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth,
    onPreviousMonthButtonClicked: (YearMonth) -> Unit,
    onNextMonthButtonClicked: (YearMonth) -> Unit,
){

    val interactionSource = remember { MutableInteractionSource() }

    Row (
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            Icons.Filled.ArrowBackIosNew,
            contentDescription = "",
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) {onPreviousMonthButtonClicked(yearMonth.minusMonths(1))}
                .padding(start = spacing16)
        )


        Column (
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            LabelLargeText(stringResource(getMonth(yearMonth.monthValue)))
            LabelMediumText(yearMonth.year.toString())
        }

        Icon(
            Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "",
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) {onNextMonthButtonClicked(yearMonth.plusMonths(1))}
                .padding(end = spacing16)
        )
    }
}