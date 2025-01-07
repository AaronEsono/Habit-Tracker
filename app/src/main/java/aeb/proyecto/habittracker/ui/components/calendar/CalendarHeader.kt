package aeb.proyecto.habittracker.ui.components.calendar

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.BodyLargeText
import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.utils.ColorsTheme
import aeb.proyecto.habittracker.utils.Constans
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import java.time.YearMonth

@Composable
fun CalendarHeader(
    yearMonth: YearMonth,
    onPreviousMonthButtonClicked: (YearMonth) -> Unit,
    onNextMonthButtonClicked: (YearMonth) -> Unit,
    showYear: Boolean = true,
    titleText:String,
    tinIconLeft:Color = MaterialTheme.colorScheme.onSurface,
    tinIconRight:Color = MaterialTheme.colorScheme.onSurface,
    iconLeft:ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    iconRight: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
    ) {

    Row {
        IconButton(onClick = {
            onPreviousMonthButtonClicked.invoke(yearMonth.minusMonths(1))
        }) {
            Icon(
                imageVector = iconLeft,
                contentDescription = stringResource(id = R.string.add_habit_unit_1),
                tint = tinIconLeft
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
                text = titleText,
                textAlign = TextAlign.Center,
            )

            if(showYear){
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
                imageVector = iconRight,
                contentDescription = stringResource(id = R.string.buttons_edit),
                tint = tinIconRight
            )
        }
    }
}