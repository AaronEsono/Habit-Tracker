package aeb.proyecto.habittracker.ui.components.calendar

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.BodyLargeText
import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.utils.Constans
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import java.time.YearMonth

@Composable
fun CalendarHeader(
    yearMonth: YearMonth,
    isInStatistics: Boolean = false,
    onPreviousMonthButtonClicked: (YearMonth) -> Unit,
    onNextMonthButtonClicked: (YearMonth) -> Unit,
) {
    val month = Constans.Months.entries.find { it.id == yearMonth.month.value }
    val text = if (isInStatistics) stringResource(
        month?.month ?: R.string.month_1
    ) else "${yearMonth.month.value} / ${yearMonth.year}"

    Row {
        IconButton(onClick = {
            onPreviousMonthButtonClicked.invoke(yearMonth.minusMonths(1))
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(id = R.string.add_habit_unit_1)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BodyLargeText(
                text = text,
                textAlign = TextAlign.Center,
            )

            if(isInStatistics){
                BodyMediumText(
                    text = yearMonth.year.toString(),
                    textAlign = TextAlign.Center,
                )
            }
        }

        IconButton(onClick = {
            onNextMonthButtonClicked.invoke(yearMonth.plusMonths(1))
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.buttons_edit)
            )
        }
    }
}