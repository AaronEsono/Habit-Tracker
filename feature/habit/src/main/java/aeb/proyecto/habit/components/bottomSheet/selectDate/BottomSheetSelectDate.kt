package aeb.proyecto.habit.components.bottomSheet.selectDate

import aeb.proyecto.habit.components.bottomSheet.selectDate.day.CalendarDayButton
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.calendar.content.CalendarContent
import aeb.proyecto.ui.calendar.content.CalendarHeader
import aeb.proyecto.ui.calendar.content.isInYearMonth
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSelectDate(
    viewModel: SelectDateViewModel = hiltViewModel(),
    onDismiss: () -> Unit = {},
    onClick: (LocalDate) -> Unit = {}
){

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    val yearMonth = viewModel.yearMonth.collectAsStateWithLifecycle().value
    val calendarDates = viewModel.calendarUIState.collectAsStateWithLifecycle().value

    CustomBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState
    ) {
        Column {
            CalendarHeader(
                yearMonth = yearMonth,
                modifier = Modifier.padding(top = spacing6, bottom = spacing20),
                onPreviousMonthButtonClicked = { yearMonth ->
                    viewModel.onMonthButtonClicked(yearMonth)
                },
                onNextMonthButtonClicked = { yearMonth ->
                    viewModel.onMonthButtonClicked(yearMonth)
                }
            )

            CalendarContent(
                modifier = Modifier.padding(horizontal = spacing16),
                dates = calendarDates.dates,
                verticalPadding = spacing10,
                horizontalPadding = spacing12,
            ) { date,modifier ->
                date?.let {
                    CalendarDayButton(
                        date = it,
                        modifier = modifier,
                        enabled = it.dateOfMonth.isInYearMonth(yearMonth),
                        onClick = { date ->
                            onClick(date)
                            scope.launch {
                                sheetState.hide()
                                onDismiss()
                            }
                        }
                    )
                } ?: Box(modifier = modifier.aspectRatio(1f))
            }
        }
    }
}