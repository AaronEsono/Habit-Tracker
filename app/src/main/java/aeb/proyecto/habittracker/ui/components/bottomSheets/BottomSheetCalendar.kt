package aeb.proyecto.habittracker.ui.components.bottomSheets

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.model.calendar.CalendarUiState
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.calendar.CalendarContent
import aeb.proyecto.habittracker.ui.components.calendar.CalendarHeader
import aeb.proyecto.habittracker.ui.components.calendar.CalendarViewModel
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCalendar(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    color: Color,
    habit: HabitWithDailyHabit,
    onClickDate: (LocalDate) -> Unit
) {

    val scope = rememberCoroutineScope()

    val state = calendarViewModel.uiState.collectAsState().value
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    calendarViewModel.setHabit(habit)

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = spacing8, vertical = spacing16)
        ) {

            LabelMediumText(
                stringResource(R.string.calendar_dx_title),
                modifier = Modifier.padding(bottom = spacing8)
            )

            CalendarHeader(state.yearMonth, { calendarViewModel.toPreviousMonth(it) }) {
                calendarViewModel.toNextMonth(it)
            }

            CalendarContent(state.dates, color = color, calendarViewModel = calendarViewModel) { date ->
                calendarViewModel.createDate(date)?.let {
                    onClickDate(it)
                }
            }

            CustomFilledButton(title = R.string.buttons_accept,
                icon = R.drawable.ic_check,
                color = color,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = spacing8, end = spacing8, top = spacing8),
                onClick = {
                    scope.launch {
                        bottomSheetState.hide()
                        onDismiss()
                    }
                })
        }
    }

}