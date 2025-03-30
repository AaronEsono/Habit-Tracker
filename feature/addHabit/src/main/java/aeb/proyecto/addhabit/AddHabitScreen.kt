package aeb.proyecto.addhabit

import aeb.proyecto.addhabit.components.bottomSheet.AddBottomSheet
import aeb.proyecto.addhabit.components.card.AddHabitCard
import aeb.proyecto.addhabit.components.card.AddHabitCardButton
import aeb.proyecto.addhabit.components.card.CardLeadingIconButton
import aeb.proyecto.addhabit.components.dialog.DatePickerDialogHabit
import aeb.proyecto.addhabit.components.dialog.PickTypeHabitDialog
import aeb.proyecto.addhabit.components.dialog.PickTypeNotificationDialog
import aeb.proyecto.addhabit.components.dialog.PickUnitDialog
import aeb.proyecto.addhabit.components.dialog.TimePickerDialog
import aeb.proyecto.addhabit.components.divider.CustomHorizontalDivider
import aeb.proyecto.addhabit.components.grid.AddHabitGrid
import aeb.proyecto.addhabit.components.notifications.NotificationComponent
import aeb.proyecto.addhabit.components.textField.AddHabitTextField
import aeb.proyecto.addhabit.components.typeHabit.MonthlyTypeHabit
import aeb.proyecto.addhabit.components.typeHabit.RecurringTypeHabit
import aeb.proyecto.addhabit.components.typeHabit.WeeklyTypeHabit
import aeb.proyecto.addhabit.components.typeHabit.getDate
import aeb.proyecto.addhabit.constants.GridOption
import aeb.proyecto.addhabit.constants.GridOptionResult
import aeb.proyecto.addhabit.constants.PICK_DATE
import aeb.proyecto.addhabit.constants.PICK_NOTIFICATION
import aeb.proyecto.addhabit.constants.PICK_TYPE_HABIT
import aeb.proyecto.addhabit.constants.PICK_TYPE_NOTIFICATION
import aeb.proyecto.addhabit.constants.PICK_UNIT
import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.addhabit.constants.Units
import aeb.proyecto.addhabit.model.DataAddHabit
import aeb.proyecto.addhabit.model.TypeNotification
import aeb.proyecto.addhabit.utils.IsOnlyDigit
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing18
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing5
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Space
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.LocalTime

//Habits entre dias personalizables?
// Decimales

@Composable
fun AddHabitScreen(
    habitIt:Long?,
    navigateToHabit: () -> Unit,
    viewModel: AddHabitViewModel = hiltViewModel()
){

    val dataAddHabit = viewModel.dataAddHabit.collectAsStateWithLifecycle().value

    AddHabitScreen(
        dataAddHabit = dataAddHabit,
        onClickCard = viewModel::onClickCard,
        onClickGridOption = viewModel::onClickGridOption,
        onClickDialog = viewModel::setDialog,
        onDismissDialog = viewModel::closeDialog,
        onClickTypeHabit = viewModel::onClickTypeHabit,
        onClickWeekly = viewModel::onClickWeekly,
        onMonthNumberSelected = viewModel::monthNumberSelected,
        onDateSelected = viewModel::onClickDate,
        onPickUnit = viewModel::onPickUnit,
        onClickTypeNotification = viewModel::onClickTypeNotification,
        onTimeSelected = viewModel::onTimeSelected,
        onClickDeleteNotification = viewModel::onClickDeleteNotification
    )

    if(dataAddHabit.bottomSheetState.isVisible){
        AddBottomSheet(
            dataBottomSheet = dataAddHabit.bottomSheetState.dataBottomSheet,
            onDismiss = viewModel::closeBottomSheet,
            color = dataAddHabit.habitScreen.color,
            contrastColor = dataAddHabit.contrastColor,
            onAccept = viewModel::onAcceptBottomSheet
        )
    }

}

@Composable
internal fun AddHabitScreen(
    dataAddHabit: DataAddHabit,
    onClickCard: (GridOption) -> Unit,
    onClickGridOption: (GridOptionResult) -> Unit = {},
    onClickDialog: (Int) -> Unit = {},
    onDismissDialog: () -> Unit = {},
    onClickTypeHabit: (TypeHabit) -> Unit = {},
    onClickWeekly: (Int) -> Unit = {},
    onMonthNumberSelected: (Int) -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
    onPickUnit: (Units) -> Unit = {},
    onClickTypeNotification: (TypeNotification) -> Unit = {},
    onTimeSelected: (LocalTime) -> Unit = {},
    onClickDeleteNotification: (LocalTime) -> Unit = {}
){

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val habit = dataAddHabit.habitScreen

    val notificationWeek = habit.notifications.filter { it.type is TypeNotification.Daily }
    val notificationRecurring = habit.notifications.filter { it.type is TypeNotification.Recurring }

    IsOnlyDigit(habit.numberTimesTextField)

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = spacing12, end = spacing12, top = spacing12)
    ){
        //TextField Nombre y descripción
        AddHabitTextField(
            textFieldState = habit.nameTextField,
            label = {LabelLargeText(stringResource(R.string.add_habit_name_label))},
            leadingIcon = {
                Icon(
                    Icons.AutoMirrored.Filled.EventNote,
                    contentDescription = "Leading icon TextField"
                )
            },
            focusManager = focusManager,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )

        AddHabitTextField(
            modifier = Modifier.padding(vertical = spacing4),
            textFieldState = habit.descriptionTextField,
            label = {LabelLargeText(stringResource(R.string.add_habit_description_label))},
            leadingIcon = {
                Icon(
                    Icons.Filled.Description,
                    contentDescription = "Leading icon TextField"
                )
            },
            focusManager = focusManager,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )

        CustomHorizontalDivider(modifier = Modifier.padding(top = spacing18, bottom = spacing8))

        // Tipo de hábito
        LabelLargeText(stringResource(R.string.add_habit_pick_type_habit_title),
            modifier = Modifier.padding(bottom = spacing12))

        AddHabitCardButton(
            title = stringResource(habit.typeHabit.title),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClickDialog(PICK_TYPE_HABIT) }
        )

        AnimatedContent(
            targetState = habit.typeHabit
        ) { typeHabit ->
            when (typeHabit) {
                TypeHabit.DAILY -> Unit
                TypeHabit.WEEKLY -> {
                    WeeklyTypeHabit(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing10),
                        numberSelected = habit.numberOfDaysWeek,
                        colorSelected = habit.color,
                        contrastColor = dataAddHabit.contrastColor,
                        onClickWeekly = onClickWeekly)
                }

                TypeHabit.MONTHLY -> {
                    MonthlyTypeHabit(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing10),
                        colorSelected = habit.color,
                        contrastColor = dataAddHabit.contrastColor,
                        numberSelected = habit.numberOfDaysMonth,
                        onNumberSelected = onMonthNumberSelected)
                }
                TypeHabit.CYCLIC -> {
                    RecurringTypeHabit(
                        intervalTextFieldState = habit.intervalTextFieldState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = spacing10),
                        focusManager = focusManager,
                        color = habit.color,
                        date = habit.dateRecurringStartDate,
                        onClick = {onClickDialog(PICK_DATE)}
                    )
                }
            }
        }

        CustomHorizontalDivider(modifier = Modifier.padding(top = spacing18, bottom = spacing16))

        //Colores e iconos
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            AddHabitCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.add_habit_color_label),
                icon = Icons.Filled.ColorLens,
                color = habit.color,
                onClick = { onClickCard(GridOption.COLORS) }
            )

            AddHabitCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.add_habit_icon_label),
                icon = habit.icon,
                color = habit.color,
                onClick = { onClickCard(GridOption.ICONS) }
            )
        }

        AnimatedVisibility(
            visible = dataAddHabit.isColorSelected
        ) {
            AddHabitGrid(gridOption = GridOption.COLORS,
                colorSelected = habit.color,
                iconSelected = habit.icon,
                onClickGridOption = onClickGridOption,
                modifier = Modifier.padding(top = spacing12)
            )
        }

        AnimatedVisibility(
            visible = dataAddHabit.isIconSelected
        ) {
            AddHabitGrid(gridOption = GridOption.ICONS,
                colorSelected = habit.color,
                iconSelected = habit.icon,
                onClickGridOption = onClickGridOption,
                modifier = Modifier.padding(top = spacing12)
            )
        }

        CustomHorizontalDivider(modifier = Modifier.padding(top = spacing16, bottom = spacing8))

        //Unidades y veces
        LabelLargeText(
            stringResource(R.string.add_habit_times_and_units)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacing12),
        ){
            AddHabitTextField(
                textFieldState = habit.numberTimesTextField,
                modifier = Modifier.weight(1f).height(45.dp).padding(top = spacing2),
                focusManager = focusManager,
                imeAction = ImeAction.Done,
                contentPadding = PaddingValues(start = spacing12),
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.padding(horizontal = spacing8))

            AddHabitCardButton(
                title = getTextUnits(habit.numberTimesTextField, habit.unit),
                modifier = Modifier.weight(1f),
                onClick = { onClickDialog(PICK_UNIT) }
            )
        }

        //Notificaciones
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED){

            CustomHorizontalDivider(modifier = Modifier.padding(top = spacing16, bottom = spacing8))

            LabelLargeText(stringResource(R.string.add_habit_notifications_title))

            CardLeadingIconButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing8),
                leadingIcon = Icons.Filled.NotificationAdd,
                color = habit.color,
                title = stringResource(R.string.add_habit_create_notification),
                onClick = { onClickDialog(PICK_TYPE_NOTIFICATION) }
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            if(notificationWeek.isNotEmpty()){
                LabelLargeText(stringResource(R.string.add_habit_notificacionWeek),
                    modifier = Modifier.padding(top = spacing10, bottom = spacing4))

                notificationWeek.forEach {
                    NotificationComponent(
                        notification = it, color = habit.color,
                        contrastColor = dataAddHabit.contrastColor,
                        onClickDelete = onClickDeleteNotification,
                        modifier = Modifier.padding(bottom = spacing8)
                    )
                }
            }

            if(notificationRecurring.isNotEmpty()){
                LabelLargeText(stringResource(R.string.add_habit_notificacionRecurring),
                    modifier = Modifier.padding(top = spacing10, bottom = spacing4))

                notificationRecurring.forEach {
                    NotificationComponent(
                        notification = it, color = habit.color,
                        contrastColor = dataAddHabit.contrastColor,
                        onClickDelete = onClickDeleteNotification,
                        modifier = Modifier.padding(bottom = spacing8)
                    )
                }
            }

        }else{

        }


    }


    //Dialog
    if (dataAddHabit.showDialog) {
        when (dataAddHabit.typeDialog) {
            PICK_TYPE_HABIT -> {
                PickTypeHabitDialog(
                    onDismissRequest = onDismissDialog,
                    onClickButton = onClickTypeHabit
                )
            }

            PICK_DATE ->{
                DatePickerDialogHabit(
                    onDismissRequest = onDismissDialog,
                    colorSelected = habit.color,
                    contrastColor = dataAddHabit.contrastColor,
                    onClickDate = onDateSelected)
            }

            PICK_UNIT -> {
                PickUnitDialog(
                    onDismissRequest = onDismissDialog,
                    unitSeleted = habit.unit,
                    colorSelected = habit.color,
                    contrastColor = dataAddHabit.contrastColor,
                    onClickButton = onPickUnit
                )
            }

            PICK_TYPE_NOTIFICATION ->{
                PickTypeNotificationDialog(
                    onDismissRequest = onDismissDialog,
                    onClickButton = onClickTypeNotification
                )
            }

            PICK_NOTIFICATION -> {
                TimePickerDialog(
                    color = habit.color,
                    contrastColor = dataAddHabit.contrastColor,
                    onDismissRequest = onDismissDialog,
                    onConfirm = onTimeSelected
                )
            }

            else -> {}
        }
    }

}

@Composable
fun getTextUnits(timeTextField:TextFieldState, typeUnit: Units):String{
    return if(timeTextField.text.toString() == "1") stringResource(typeUnit.title)
    else stringResource(typeUnit.titlePlural)
}