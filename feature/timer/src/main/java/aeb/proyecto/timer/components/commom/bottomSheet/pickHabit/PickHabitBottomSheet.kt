package aeb.proyecto.timer.components.commom.bottomSheet.pickHabit

import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.commom.bottomSheet.pickHabit.components.HabitCard
import aeb.proyecto.timer.components.commom.loading.TimerLoading
import aeb.proyecto.timer.model.HabitLinkedState
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.date.utils.getTextToday
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.TitleMediumText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickHabitBottomSheet(
    pickHabitViewModel: PickHabitViewModel = hiltViewModel(),
    habitLinkedState: HabitLinkedState,
    onDismiss: () -> Unit = {},
    onAccept: (Long,LocalDate) -> Unit
){

    LaunchedEffect (Unit){
        pickHabitViewModel.setData(habitLinkedState)
    }

    val pickHabitUIState = pickHabitViewModel.pickHabitUIState.collectAsStateWithLifecycle().value
    val habitSelected = pickHabitViewModel.habitSelected.collectAsStateWithLifecycle().value
    val dateSelected = pickHabitViewModel.dateSelected.collectAsStateWithLifecycle().value
    val dialogOpen = pickHabitViewModel.dialogOpen.collectAsStateWithLifecycle().value

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    CustomBottomSheet (
        onDismiss = onDismiss,
        sheetState = sheetState
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacing8, horizontal = spacing12),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            when(pickHabitUIState){
                PickHabitUIState.Error -> Unit
                PickHabitUIState.Loading -> {
                    TimerLoading()
                }
                is PickHabitUIState.Success -> {
                    when(pickHabitUIState.habits.size){
                        0 -> {
                            TitleMediumText(
                                stringResource(R.string.timer_linked_habit_no_habit),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = spacing8)
                            )
                        }
                        else -> {

                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .wrapContentHeight()
                            ){
                                TitleMediumText(
                                    text = getTextToday(dateSelected ?: LocalDate.now()),
                                    textAlign = TextAlign.Center,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Center)
                                )

                                CustomRipple {
                                    Icon(
                                        Icons.Filled.DateRange,
                                        contentDescription = "habit icon pick habit",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                            .padding(end = spacing10)
                                            .size(25.dp)
                                            .clickable {
                                                scope.launch {
                                                    pickHabitViewModel.openDialog()
                                                }
                                            }
                                    )
                                }
                            }

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface,
                                thickness = 1.dp,
                                modifier = Modifier.padding(top = spacing16)
                            )

                            TitleMediumText(
                                text = stringResource(R.string.timer_linked_habit_title),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = spacing16, top = spacing12)
                            )


                            Column(
                                modifier = Modifier
                                    .heightIn(max = 300.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                pickHabitUIState.habits.forEach { habit ->
                                    HabitCard(
                                        habit = habit,
                                        selected = habit.id == habitSelected?.id,
                                        onClickHabit = {
                                            pickHabitViewModel.habitSelected(habit)
                                        }
                                    )
                                }
                            }

                            Row (
                                modifier = Modifier.fillMaxWidth()
                                    .padding(bottom = spacing8, top = spacing6)
                            ){

                                OutlinedButton(
                                    onClick = {
                                        scope.launch {
                                            sheetState.hide()
                                            onDismiss()
                                        }
                                    },
                                    shape = RoundedCornerShape(spacing12),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    LabelLargeText(
                                        text = stringResource(R.string.timer_cancel),
                                    )
                                }

                                Spacer(modifier = Modifier.padding(horizontal = spacing8))

                                Button(
                                    onClick = {
                                        scope.launch {
                                            onAccept(
                                                habitSelected?.id ?: -1,
                                                dateSelected ?: LocalDate.now()
                                            )
                                            sheetState.hide()
                                            onDismiss()
                                        }
                                    },
                                    shape = RoundedCornerShape(spacing12),
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    LabelLargeText(
                                        text = stringResource(R.string.timer_accept),
                                        color = MaterialTheme.colorScheme.inverseOnSurface
                                    )
                                }

                            }

                        }
                    }
                }
            }
        }
    }

    if(dialogOpen){
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = pickHabitViewModel::closeDialog,
            modifier = Modifier.verticalScroll(rememberScrollState()),
            confirmButton = {
                TextButton(onClick = {
                    val localDate = datePickerState.selectedDateMillis?.let { millis ->
                        Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    } ?: LocalDate.now()

                    pickHabitViewModel.choseDate(localDate)
                    pickHabitViewModel.closeDialog()
                }) {
                    TitleMediumText(stringResource(R.string.timer_accept))
                }
            },
            dismissButton = {
                TextButton(onClick = {pickHabitViewModel.closeDialog()}) {
                    TitleMediumText(stringResource(R.string.timer_cancel))
                }
            },
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = MaterialTheme.colorScheme.onSurface,
                selectedDayContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = MaterialTheme.colorScheme.onSurface,
                    selectedYearContainerColor = MaterialTheme.colorScheme.onSurface,
                    selectedDayContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    selectedYearContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    todayDateBorderColor = MaterialTheme.colorScheme.onSurface,
                    todayContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }

}