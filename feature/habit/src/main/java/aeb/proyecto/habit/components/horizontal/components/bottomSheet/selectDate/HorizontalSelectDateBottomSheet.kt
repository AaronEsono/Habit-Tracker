package aeb.proyecto.habit.components.horizontal.components.bottomSheet.selectDate

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
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing28
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

/**
 * A modal bottom sheet providing a custom calendar interface to select a specific [LocalDate].
 *
 * This component displays a calendar grid (header, days of week, and day buttons),
 * allowing the user to navigate through months and select a date. It handles the
 * selection lifecycle and interacts with the [SelectDateViewModel] to update the calendar state.
 *
 * @param viewModel ViewModel responsible for managing calendar data (YearMonth, dates).
 * @param selectedDate The currently active date to highlight in the calendar.
 * @param onDismiss Callback invoked to close the bottom sheet.
 * @param onClick Callback triggered when a date is selected, providing the [LocalDate] and a confirmation flag.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorizontalSelectDateBottomSheet(
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
        onDismiss = { onDismiss(TypeBottomSheet.SelectDate(false))},
        sheetState = sheetState
    ) {
        Column (
            modifier = Modifier.verticalScroll(rememberScrollState())
        ){
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
                modifier = Modifier.padding(horizontal = spacing28, vertical = spacing10),
                horizontalPadding = spacing20
            )

            CalendarContent(
                modifier = Modifier.padding(horizontal = spacing28),
                dates = calendarDates.dates,
                verticalPadding = spacing12,
                horizontalPadding = spacing20,
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
                                onDismiss(TypeBottomSheet.SelectDate(false))
                            }
                        }
                    )
                } ?: Box(modifier = modifier.aspectRatio(1f))
            }

            RowButtonSelectDate(
                modifier = Modifier.padding(horizontal = spacing16),
                scope = scope,
                sheetState = sheetState,
                onDismiss = { onDismiss(TypeBottomSheet.SelectDate(false))},
                onClick = onClick
            )
        }
    }

}