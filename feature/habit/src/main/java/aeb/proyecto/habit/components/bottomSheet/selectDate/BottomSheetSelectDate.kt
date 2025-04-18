package aeb.proyecto.habit.components.bottomSheet.selectDate

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.bottomSheet.selectDate.day.CalendarDayButton
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.calendar.content.CalendarContent
import aeb.proyecto.ui.calendar.content.CalendarDays
import aeb.proyecto.ui.calendar.content.CalendarHeader
import aeb.proyecto.ui.calendar.content.isInYearMonth
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSelectDate(
    viewModel: SelectDateViewModel = hiltViewModel(),
    selectedDate: LocalDate,
    onDismiss: () -> Unit = {},
    onClick: (LocalDate) -> Unit = {}
){

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val yearMonth = viewModel.yearMonth.collectAsStateWithLifecycle().value
    val calendarDates = viewModel.calendarUIState.collectAsStateWithLifecycle().value

    LaunchedEffect (Unit){
        viewModel.initMonth()
    }

    CustomBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState
    ) {
        Column {
            CalendarHeader(
                yearMonth = yearMonth,
                modifier = Modifier.padding(top = spacing6, bottom = spacing12),
                onPreviousMonthButtonClicked = { yearMonth ->
                    viewModel.onMonthButtonClicked(yearMonth)
                },
                onNextMonthButtonClicked = { yearMonth ->
                    viewModel.onMonthButtonClicked(yearMonth)
                }
            )

            CalendarDays(
                modifier = Modifier.padding(horizontal = spacing16, vertical = spacing10),
                horizontalPadding = spacing12
            )

            CalendarContent(
                modifier = Modifier.padding(horizontal = spacing16),
                dates = calendarDates.dates,
                verticalPadding = spacing8,
                horizontalPadding = spacing10,
            ) { date,modifier ->
                date?.let {
                    CalendarDayButton(
                        date = it,
                        modifier = modifier,
                        isSelectedDate = date.dateOfMonth == selectedDate,
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

            Row (
                modifier = Modifier.fillMaxWidth()
                    .padding(start = spacing16, bottom = spacing10, end = spacing16, top = spacing4)
            ){
                CustomRipple {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                onDismiss()
                            }
                        },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                        shape = RoundedCornerShape(spacing12),
                        modifier = Modifier.weight(1f).padding(end = spacing12),
                    ) {
                        LabelLargeText(stringResource(R.string.habit_cancel))
                    }
                }

                CustomRipple {
                    Button(
                        onClick = {
                            onClick(LocalDate.now())
                            scope.launch {
                                sheetState.hide()
                                onDismiss()
                            }
                        },
                        shape = RoundedCornerShape(spacing12),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        LabelLargeText(stringResource(R.string.habit_today),
                            color = MaterialTheme.colorScheme.inverseOnSurface)
                    }
                }
            }
        }
    }
}