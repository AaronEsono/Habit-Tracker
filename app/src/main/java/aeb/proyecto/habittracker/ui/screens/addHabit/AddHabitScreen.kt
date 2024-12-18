package aeb.proyecto.habittracker.ui.screens.addHabit

import aeb.proyecto.habittracker.MainActivity
import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.notification.AlarmItem
import aeb.proyecto.habittracker.data.model.notification.NotificationWithName
import aeb.proyecto.habittracker.data.model.state.AddHabitScreenState
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetGeneral
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetPickUnit
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.card.CardInfoAddHabit
import aeb.proyecto.habittracker.ui.components.card.CardPickColorAddHabit
import aeb.proyecto.habittracker.ui.components.items.ColorItem
import aeb.proyecto.habittracker.ui.components.items.IconItem
import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.textField.CustomOutlinedTextField
import aeb.proyecto.habittracker.ui.components.timePicker.TimePickerHabit
import aeb.proyecto.habittracker.utils.Constans.InPlural
import aeb.proyecto.habittracker.utils.Constans.ListColors
import aeb.proyecto.habittracker.utils.Constans.ListIcons
import aeb.proyecto.habittracker.utils.Constans.onlyDigits
import aeb.proyecto.habittracker.utils.Constans.permissions
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing72
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import aeb.proyecto.habittracker.utils.cancelAlarm
import aeb.proyecto.habittracker.utils.setUpAlarm
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

@SuppressLint("UnrememberedMutableState", "NewApi")
@Composable
fun AddHabitScreen(
    addHabitViewModel: AddHabitViewModel = hiltViewModel(),
    navigateToHabit: () -> Unit,
    edit: Boolean = false,
    id: Long? = null
) {
    val habit = addHabitViewModel.habit.collectAsState().value
    val uiState = addHabitViewModel.uiState.collectAsState().value
    val notifications = addHabitViewModel.notifications.collectAsState().value

    val nameHabit = rememberTextFieldState()
    val descriptionHabit = rememberTextFieldState("")
    val timesHabit = rememberTextFieldState("")

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (edit && id != null) {
            addHabitViewModel.getHabit(id)
        }
    }

    LaunchedEffect(timesHabit.text) {
        if (!timesHabit.text.toString().matches(onlyDigits) && timesHabit.text.toString()
                .isNotEmpty()
        ) {
            timesHabit.edit {
                delete(timesHabit.text.length - 1, timesHabit.text.length)
            }
        }
    }

    LaunchedEffect(habit) {
        if (habit.habit.name.isNotEmpty()) {
            nameHabit.edit {
                replace(0, length, habit.habit.name)
            }
            descriptionHabit.edit {
                replace(0, length, habit.habit.description ?: "")
            }
            timesHabit.edit {
                replace(0, length, habit.habit.times.toString())
            }
            addHabitViewModel.setData(habit.habit.color, habit.habit.icon, habit.habit.unit)
        }
    }

    val isPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycleState.value = event
            if (event == Lifecycle.Event.ON_RESUME) {
                isPermissionGranted.value = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
                    color = uiState.color,
                    icon = Icons.Filled.ColorLens,
                    modifier = Modifier.weight(1f)
                ) {
                    addHabitViewModel.openColor()
                }


                CardPickColorAddHabit(
                    text = R.string.add_habit_screen_icon,
                    color = uiState.color,
                    icon = uiState.icon,
                    modifier = Modifier.weight(1f)
                ) {
                    addHabitViewModel.openIcon()
                }
            }

            Spacer(modifier = Modifier.padding(vertical = spacing12))

            AnimatedVisibility(
                visible = uiState.colorSelected,
            ) {
                GridOptions(uiState.color, true, addHabitViewModel, uiState.icon)
            }

            AnimatedVisibility(
                visible = uiState.iconSelected,
            ) {
                GridOptions(uiState.color, false, addHabitViewModel, uiState.icon)
            }

            Spacer(modifier = Modifier.padding(vertical = spacing2))

            BodySmallText(stringResource(uiState.unitPicked.question))

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
                        uiState.unitPicked.title
                    ) else stringResource(uiState.unitPicked.pluralTitle),
                    finalIcon = Icons.Filled.KeyboardArrowDown,
                    color = uiState.color,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing8),
                    onClick = { addHabitViewModel.openBottomSheet() },
                    onDelete = { addHabitViewModel.openBottomSheet() }
                )
            }

            Spacer(modifier = Modifier.padding(vertical = spacing12))

            if (isPermissionGranted.value) {
                AddHabitNotifications(addHabitViewModel, uiState, notifications)
            } else {
                Column (modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)){
                    LabelMediumText(text = stringResource(R.string.add_habit_permissions), textAlign = TextAlign.Left)
                    CustomFilledButton(
                        modifier = Modifier.padding(top = spacing8).fillMaxWidth(),
                        title = R.string.buttons_go_settings,
                        icon = R.drawable.ic_settingsbtn,
                        color = uiState.color,
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }

        CustomFilledButton(
            title = if (edit) R.string.buttons_edit else R.string.buttons_save,
            icon = R.drawable.ic_check,
            color = uiState.color,
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing16)
                .align(Alignment.BottomCenter) // Fija el botón en la parte inferior
                .height(48.dp),
            onClick = {
                if (nameHabit.text.isEmpty() || timesHabit.text.isEmpty()) {
                    addHabitViewModel.setText()
                } else {
                    addHabitViewModel.procesateHabit(
                        nameHabit.text.toString(),
                        descriptionHabit.text.toString(),
                        timesHabit.text.toString(),
                        edit
                    ) { notifications, cancel ->
                        if (edit) {
                            cancel.forEach {
                                cancelAlarm(context, it)
                            }
                        }

                        notifications.forEach {
                            setUpAlarm(
                                context,
                                NotificationWithName(it, nameHabit.text.toString(), uiState.color)
                            )
                        }
                        navigateToHabit()
                    }
                }
            }

        )

        AddHabitScreenState(addHabitViewModel, uiState)
    }
}