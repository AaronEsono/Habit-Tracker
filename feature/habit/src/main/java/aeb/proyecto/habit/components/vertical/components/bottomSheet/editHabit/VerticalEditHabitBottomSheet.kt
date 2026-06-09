package aeb.proyecto.habit.components.vertical.components.bottomSheet.editHabit

import aeb.proyecto.habit.components.common.bottomSheet.editHabit.buttons.ButtonsRow
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.calendar.CalendarDateEditHabit
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.card.daily.DailyHabitCard
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.card.monthly.MonthlyHabitCard
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.card.recurring.RecurringHabitCard
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.card.weekly.WeeklyHabitCard
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.state.EditHabitState
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.vm.EditHabitVM
import aeb.proyecto.habit.components.common.loading.HabitLoading
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.calendar.content.CalendarContent
import aeb.proyecto.ui.calendar.content.CalendarDays
import aeb.proyecto.ui.calendar.content.CalendarHeader
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * A modal bottom sheet for editing and managing a specific habit,
 * optimized for vertical screen orientations.
 *
 * This screen provides a centralized hub to:
 * 1. View configuration details based on the habit type (Daily, Monthly, etc.).
 * 2. Perform metadata management (Edit, Delete).
 * 3. Interact with a calendar grid to track or modify specific dates.
 *
 * @param verticalEditHabitVM ViewModel managing the habit's state.
 * @param idHabit The unique identifier of the habit.
 * @param startDayOfWeek User-defined start day of the week for the calendar.
 * @param onDismiss Callback to close the bottom sheet.
 * @param onClickEdit Action to navigate to the detailed edit form.
 * @param onClickDelete Action to trigger the deletion sequence.
 * @param onLongClick Secondary calendar date interaction callback.
 * @param onClick Primary calendar date interaction callback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalEditHabitBottomSheet(
    verticalEditHabitVM: EditHabitVM = hiltViewModel(),
    idHabit:Long,
    startDayOfWeek: DayOfWeek? = DayOfWeek.MONDAY,
    onDismiss: (typeBottomSheet: TypeBottomSheet) -> Unit = {},
    onClickEdit: (id:Long) -> Unit = {},
    onClickDelete: (id:Long, color: Int) -> Unit = {_,_ ->},
    onLongClick: (id:Long,date:LocalDate) -> Unit,
    onClick: (id: Long, date: LocalDate) -> Unit,
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val yearMonth = verticalEditHabitVM.yearMonth.collectAsStateWithLifecycle().value
    val day = verticalEditHabitVM.startDayOfWeek.collectAsStateWithLifecycle(null).value
    val calendarDays = verticalEditHabitVM.calendarDays.collectAsStateWithLifecycle().value


    LaunchedEffect(Unit) {
        verticalEditHabitVM.getIdHabit(idHabit)
        verticalEditHabitVM.setDay(startDayOfWeek)
    }

    val bottomSheetState = verticalEditHabitVM.bottomSheetState.collectAsStateWithLifecycle().value

    CustomBottomSheet (
        sheetState = sheetState,
        onDismiss = { onDismiss(TypeBottomSheet.EditHabit()) },
    ){
        // Cabecera
        when(bottomSheetState){
            is EditHabitState.Error -> Unit
            EditHabitState.Loading -> {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = spacing8).height(200.dp)
                ){
                    HabitLoading()
                }
            }
            is EditHabitState.Success -> {
                Column (
                    modifier = Modifier.padding(horizontal = spacing16)
                ){
                    when(bottomSheetState.habit.typeHabit){
                        TypeHabit.Daily -> {
                            if(bottomSheetState.habit.typeHabit is TypeHabit.Daily){
                                DailyHabitCard(
                                    habit = bottomSheetState.habit,
                                    onDismissBottomSheet = {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            onDismiss(TypeBottomSheet.EditHabit())
                                        }
                                    }
                                )
                            }
                        }
                        is TypeHabit.Monthly -> {
                            if(bottomSheetState.habit.typeHabit is TypeHabit.Monthly){
                                MonthlyHabitCard(
                                    habit = bottomSheetState.habit,
                                    onDismissBottomSheet = {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            onDismiss(TypeBottomSheet.EditHabit())
                                        }
                                    }
                                )
                            }
                        }
                        is TypeHabit.Recurring -> {
                            if(bottomSheetState.habit.typeHabit is TypeHabit.Recurring){
                                RecurringHabitCard(
                                    habit = bottomSheetState.habit,
                                    onDismissBottomSheet = {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            onDismiss(TypeBottomSheet.EditHabit())
                                        }
                                    }
                                )
                            }
                        }
                        is TypeHabit.Weekly -> {
                            if (bottomSheetState.habit.typeHabit is TypeHabit.Weekly){
                                WeeklyHabitCard(
                                    habit = bottomSheetState.habit,
                                    onDismissBottomSheet = {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            onDismiss(TypeBottomSheet.EditHabit())
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(vertical = spacing4))

                    //Botones
                    ButtonsRow(
                        onClickEdit = {
                            coroutineScope.launch {
                                onClickEdit(bottomSheetState.habit.id)
                                sheetState.hide()
                                onDismiss(TypeBottomSheet.EditHabit())
                            }
                        },
                        onClickDelete = {
                            coroutineScope.launch {
                                onClickDelete(bottomSheetState.habit.id, bottomSheetState.habit.color)
                            }
                        }
                    )

                    CalendarHeader(
                        yearMonth = yearMonth,
                        modifier = Modifier.padding(top = spacing16, bottom = spacing16),
                        onPreviousMonthButtonClicked = { yearMonth ->
                            verticalEditHabitVM.onMonthButtonClicked(yearMonth)
                        },
                        onNextMonthButtonClicked = { yearMonth ->
                            verticalEditHabitVM.onMonthButtonClicked(yearMonth)
                        }
                    )

                    CalendarDays(
                        modifier = Modifier.padding(horizontal = spacing8),
                        horizontalPadding = spacing12,
                        startDay = day,
                    )

                    Spacer(modifier = Modifier.padding(vertical = spacing4))

                    CalendarContent(
                        dates = calendarDays.dates,
                        horizontalPadding = spacing8,
                        verticalPadding = spacing2
                    ) { item, modifier ->
                        if(item != null){
                            CalendarDateEditHabit(
                                modifier = modifier,
                                day = item.dateOfMonth,
                                monthSelected = yearMonth.atEndOfMonth(),
                                habitWithDay = item.data,
                                onClick = onClick,
                                onLongClick = onLongClick
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(vertical = spacing8))
                }
            }
        }
    }
}