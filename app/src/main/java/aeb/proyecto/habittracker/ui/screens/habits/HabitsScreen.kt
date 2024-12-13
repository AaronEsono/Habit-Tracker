package aeb.proyecto.habittracker.ui.screens.habits

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetCalendar
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetGeneral
import aeb.proyecto.habittracker.ui.components.calendar.CalendarContent
import aeb.proyecto.habittracker.ui.components.calendar.CalendarHeader
import aeb.proyecto.habittracker.ui.components.calendar.CalendarViewModel
import aeb.proyecto.habittracker.ui.components.card.CardDailyHabit
import aeb.proyecto.habittracker.ui.components.dialog.DialogHabit
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Constans.PICK
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HabitsScreen(
    habitsViewModel: HabitsViewModel = hiltViewModel(),
    onEditClick: (Long) -> Unit = {}
) {

    val habits = habitsViewModel.habits.collectAsState().value
    // TODO - Comprobar cuando cambie el dia, añadir un nuevo dia

    val showDialog = remember { mutableStateOf(false) }
    val habitSelected = remember { mutableLongStateOf(0) }

    val showGeneralDx = remember { mutableStateOf(false) }
    val showCalendar = remember { mutableStateOf(false) }
    val showPickUnit = remember { mutableStateOf(false) }

    val color = remember {
        mutableStateOf(
            Color.Red
        )
    }

    if (habits.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = spacing8),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LabelMediumText(
                text = stringResource(R.string.habits_screen_no_habit)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .blur(if (showDialog.value) 10.dp else 0.dp)
                .padding(horizontal = spacing8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(habits.size, key = { habits[it].habit.id }) { index ->
                key(habits[index].habit.id) {
                    CardDailyHabit(
                        habitWithDailyHabit = habits[index],
                        onClick = { id ->
                            val unit = Constans.Units.entries.find { item ->
                                item.id == (habits.find { it.habit.id == id }?.habit?.unit ?: 1)
                            } ?: Constans.Units.TIMES

                            if (unit.action == PICK) {
                                habitsViewModel.plusOneHabit(id)
                            } else {
                                showPickUnit.value = true
                            }
                        },
                        onClickCard = { habit ->

                            habitSelected.longValue = habit
                            color.value = Color(habits.find { it.habit.id == habit }?.habit?.color ?: 1)
                            showDialog.value = true
                        }
                    )
                }
            }
        }

        if (showDialog.value) {
            DialogHabit(
                habits.find { it.habit.id == habitSelected.longValue } ?: HabitWithDailyHabit(),
                onDismissRequest = { showDialog.value = false },
                onUnitClick = {
                    habitsViewModel.plusOneHabit(it)
                },
                onDeleteClick = { showGeneralDx.value = true },
                onEditClick = {
                    showDialog.value = false
                    onEditClick(habitSelected.longValue)
                },
                onCalendarClick = { showCalendar.value = true })
        }

        if (showGeneralDx.value) {

            BottomSheetGeneral(
                showBottomSheet = showGeneralDx,
                color = color,
                title = R.string.general_dx_attention,
                subtitle = R.string.general_dx_subtitle_delete,
                showCancel = true,
                onCancel = { showGeneralDx.value = false },
                onAccept = {
                    habitsViewModel.deleteHabit(habitSelected.longValue)
                    showGeneralDx.value = false
                    showDialog.value = false
                }
            )
        }

        if (showCalendar.value) {
            BottomSheetCalendar(
                showBottomSheet = showCalendar,
                color = color,
                habit = habits.find { it.habit.id == habitSelected.longValue } ?: HabitWithDailyHabit()){ date, year ->
                    val date = LocalDate.of(year.year, year.month, date.dayOfMonth.toInt())

                    habitsViewModel.plusOneHabit(habitSelected.longValue, date)
            }
        }

    }
}