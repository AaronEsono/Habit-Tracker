package aeb.proyecto.ui.calendar.content

import aeb.proyecto.ui.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.clickable
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
    Row (
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            Icons.Filled.ArrowBackIosNew,
            contentDescription = "",
            modifier = Modifier.clickable {onPreviousMonthButtonClicked(yearMonth.minusMonths(1))}
                .padding(start = spacing16)
        )

        LabelLargeText(
            stringResource(R.string.text_calendar_header,
                yearMonth.month.toString(),
                yearMonth.year.toString()),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        Icon(
            Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "",
            modifier = Modifier.clickable {onNextMonthButtonClicked(yearMonth.plusMonths(1))}
                .padding(end = spacing16)
        )
    }
}