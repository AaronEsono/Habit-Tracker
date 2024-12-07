package aeb.proyecto.habittracker.ui.screens.addHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.time.TimeNotification
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetGeneral
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetPickUnit
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.card.CardInfoAddHabit
import aeb.proyecto.habittracker.ui.components.card.CardPickColorAddHabit
import aeb.proyecto.habittracker.ui.components.items.ColorItem
import aeb.proyecto.habittracker.ui.components.items.IconItem
import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.ui.components.textField.CustomOutlinedTextField
import aeb.proyecto.habittracker.ui.components.timePicker.TimePickerHabit
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Constans.InPlural
import aeb.proyecto.habittracker.utils.Constans.ListColors
import aeb.proyecto.habittracker.utils.Constans.ListIcons
import aeb.proyecto.habittracker.utils.Constans.onlyDigits
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing72
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@SuppressLint("UnrememberedMutableState")
@Composable
fun AddHabitScreen() {
    val nameHabit = rememberTextFieldState("")
    val descriptionHabit = rememberTextFieldState("")
    val timesHabit = rememberTextFieldState("")

    val color = remember { mutableStateOf(ListColors[0]) }
    val icon = remember { mutableStateOf(ListIcons[0]) }

    val colorSelected = remember { mutableStateOf(false) }
    val iconSelected = remember { mutableStateOf(false) }

    val showBottomSheet = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }
    val showGeneralDx = remember { mutableStateOf(false) }

    val notifications: MutableState<List<TimeNotification>> = remember { mutableStateOf(listOf()) }

    val unitPicked = remember { mutableStateOf(Constans.Units.TIMES) }

    val notificationSelected: MutableState<TimeNotification?> = remember { mutableStateOf(null) }

    val attentionText = remember { mutableStateOf(R.string.general_dx_attention_subtitile_time_picker) }

    LaunchedEffect(timesHabit.text) {
        if (!timesHabit.text.toString().matches(onlyDigits) && timesHabit.text.toString()
                .isNotEmpty()
        ) {
            timesHabit.edit {
                delete(timesHabit.text.length - 1, timesHabit.text.length)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = spacing16, start = spacing16, end = spacing16, bottom = spacing72)
                .verticalScroll(rememberScrollState())
        ) {

            CustomOutlinedTextField(
                rememberTextFieldState = nameHabit,
                label = R.string.add_habit_screen_name,
                isNeeded = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            CustomOutlinedTextField(
                rememberTextFieldState = descriptionHabit,
                label = R.string.add_habit_screen_description,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(vertical = spacing12))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CardPickColorAddHabit(
                    text = R.string.add_habit_screen_color,
                    color = color.value,
                    icon = Icons.Filled.ColorLens,
                    modifier = Modifier.weight(1f)
                ) {
                    colorSelected.value = !colorSelected.value
                    iconSelected.value = false
                }


                CardPickColorAddHabit(
                    text = R.string.add_habit_screen_icon,
                    color = color.value,
                    icon = icon.value,
                    modifier = Modifier.weight(1f)
                ) {
                    iconSelected.value = !iconSelected.value
                    colorSelected.value = false
                }
            }

            Spacer(modifier = Modifier.padding(vertical = spacing12))

            AnimatedVisibility(
                visible = colorSelected.value,
            ) {
                GridOptions(color, colorSelected, true, icon, iconSelected)
            }

            AnimatedVisibility(
                visible = iconSelected.value,
            ) {
                GridOptions(color, colorSelected, false, icon, iconSelected)
            }

            Spacer(modifier = Modifier.padding(vertical = spacing2))

            BodySmallText(stringResource(unitPicked.value.question))

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    CustomOutlinedTextField(
                        rememberTextFieldState = timesHabit,
                        label = R.string.add_habit_screen_name,
                        labelError = R.string.add_habit_screen_no_units,
                        isNeeded = true,
                        showLabel = false,
                        isNumeric = true,
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = spacing8))

                CardInfoAddHabit(
                    title = if (InPlural.contains(timesHabit.text.toString())) stringResource(
                        unitPicked.value.title
                    ) else stringResource(unitPicked.value.pluralTitle),
                    finalIcon = Icons.Filled.KeyboardArrowDown,
                    color = color,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing8),
                    onClick = { showBottomSheet.value = true },
                    onDelete = { showBottomSheet.value = true }
                )
            }

            Spacer(modifier = Modifier.padding(vertical = spacing12))

            BodySmallText(stringResource(R.string.add_habit_pick_date))

            CardInfoAddHabit(
                title = stringResource(R.string.add_habit_add_hour),
                icon = Icons.Filled.Add,
                finalIcon = Icons.Filled.KeyboardArrowRight,
                color = color,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing8),
                onClick = {
                    notificationSelected.value = null
                    showTimePicker.value = true
                },
                onDelete = {
                    notificationSelected.value = null
                    showTimePicker.value = true
                }
            )

            notifications.value.forEach { it ->
                CardInfoAddHabit(
                    title = stringResource(
                        R.string.add_habit_pick_time,
                        it.hour,
                        if (it.minute < 10) "0${it.minute}" else it.minute
                    ),
                    icon = Icons.Filled.AddAlert,
                    finalIcon = Icons.Filled.Delete,
                    color = color,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing8),
                    onClick = {
                        notificationSelected.value = it
                        showTimePicker.value = true
                    },
                    onDelete = { notifications.value = notifications.value.minus(it) },
                    colorInFinalIcon = true
                )
            }

        }

        CustomFilledButton(
            title = R.string.buttons_save,
            icon = R.drawable.ic_check,
            color = color.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing16)
                .align(Alignment.BottomCenter) // Fija el botón en la parte inferior
                .height(48.dp),
            onClick = {
                if (nameHabit.text.isEmpty() && timesHabit.text.isEmpty()) {
                    attentionText.value = R.string.general_dx_attention_fill_data
                    showGeneralDx.value = true
                }
            }

        )

    }


    if (showGeneralDx.value) {
        BottomSheetGeneral(
            showGeneralDx,
            color = color,
            titleAccept = R.string.buttons_accept,
            iconAccept = R.drawable.ic_check,
            title = R.string.general_dx_attention,
            subtitle = attentionText.value
        )
    }

    if (showBottomSheet.value) {
        BottomSheetPickUnit(showBottomSheet, color = color, unitPicked)
    }

    if (showTimePicker.value) {
        TimePickerHabit(
            color = color,
            onDismiss = { showTimePicker.value = false },
            onConfirm = { it ->
                //Pasar al viewModel cuando se implemente room
                if (notificationSelected.value == null) {

                    if (notifications.value.find { item -> item == it } == null) {
                        notifications.value = notifications.value.plus(it)
                    } else {
                        attentionText.value = R.string.general_dx_attention_subtitile_time_picker
                        showGeneralDx.value = true
                    }

                } else {
                    val temporalList = notifications.value.toMutableList()

                    temporalList.find { item -> item == notificationSelected.value }?.let { item2 ->
                        item2.minute = it.minute
                        item2.hour = it.hour
                    }

                    notifications.value = temporalList
                }
            },
            notification = notificationSelected.value
        )
    }


}

@Composable
fun GridOptions(
    color: MutableState<Color>,
    colorSelected: MutableState<Boolean>,
    isColors: Boolean,
    imageVector: MutableState<ImageVector>,
    iconSelected: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 120.dp)
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = spacing12), elevation = CardDefaults.cardElevation(
                defaultElevation = spacing8
            )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(30.dp),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = spacing4),
                contentPadding = PaddingValues(8.dp), // Espaciado externo
                horizontalArrangement = Arrangement.spacedBy(8.dp), // Espaciado horizontal entre ítems
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espaciado vertical entre filas
            ) {
                if (isColors) {
                    items(ListColors.size) { item ->
                        ColorItem(
                            color = ListColors[item],
                            selected = color.value == ListColors[item]
                        ) {
                            color.value = it
                            colorSelected.value = false
                        }
                    }
                } else {
                    items(ListIcons.size) { item ->
                        IconItem(
                            imageVector = ListIcons[item],
                            selected = imageVector.value == ListIcons[item],
                            color = color.value
                        ) {
                            imageVector.value = it
                            iconSelected.value = false
                        }
                    }
                }
            }
        }
    }
}