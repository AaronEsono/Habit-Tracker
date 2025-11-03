package aeb.proyecto.habit.components.vertical.components.bottomSheet.selectDate

import aeb.proyecto.habit.components.common.bottomSheet.selectDate.CalendarDayButton
import aeb.proyecto.habit.components.common.bottomSheet.selectDate.RowButtonSelectDate
import aeb.proyecto.habit.components.common.bottomSheet.selectDate.SelectDateViewModel
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.calendar.content.CalendarContent
import aeb.proyecto.ui.calendar.content.CalendarDays
import aeb.proyecto.ui.calendar.content.CalendarHeader
import aeb.proyecto.ui.calendar.content.isInYearMonth
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalSelectDateBottomSheet(
    viewModel: SelectDateViewModel = hiltViewModel(),
    selectedDate: LocalDate,
    onDismiss: (typeBottomSheet: TypeBottomSheet) -> Unit = {},
    onClick: (LocalDate, Boolean) -> Unit = {_,_ ->}
){

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val yearMonth = viewModel.yearMonth.collectAsStateWithLifecycle().value
    val calendarDates = viewModel.calendarUIState.collectAsStateWithLifecycle().value

    LaunchedEffect (Unit){
        viewModel.initMonth()
    }

    CustomBottomSheet(
        onDismiss = { onDismiss(TypeBottomSheet.SelectDate(enabled = false))},
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
                            onClick(date,true)
                            scope.launch {
                                sheetState.hide()
                                onDismiss(TypeBottomSheet.SelectDate(enabled = false))
                            }
                        }
                    )
                } ?: Box(modifier = modifier.aspectRatio(1f))
            }

            RowButtonSelectDate(
                modifier = Modifier.padding(horizontal = spacing16),
                scope = scope,
                sheetState = sheetState,
                onDismiss = {onDismiss(TypeBottomSheet.SelectDate(enabled = false))},
                onClick = onClick
            )
        }
    }

}