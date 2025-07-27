package aeb.proyecto.addhabit.components.horizontal

import aeb.proyecto.addhabit.AddHabitUIState
import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.components.commom.bottomSheet.AddBottomSheet
import aeb.proyecto.addhabit.components.commom.card.AddHabitCard
import aeb.proyecto.addhabit.components.commom.card.AddHabitCardButton
import aeb.proyecto.addhabit.components.commom.card.CardLeadingIconButton
import aeb.proyecto.addhabit.components.commom.dialog.DatePickerDialogHabit
import aeb.proyecto.addhabit.components.commom.dialog.PickTypeHabitDialog
import aeb.proyecto.addhabit.components.commom.dialog.PickTypeNotificationDialog
import aeb.proyecto.addhabit.components.commom.dialog.PickUnitDialog
import aeb.proyecto.addhabit.components.commom.dialog.TimePickerDialog
import aeb.proyecto.addhabit.components.commom.divider.CustomHorizontalDivider
import aeb.proyecto.addhabit.components.commom.grid.AddHabitGrid
import aeb.proyecto.addhabit.components.commom.loading.AddHabitLoading
import aeb.proyecto.addhabit.components.commom.notifications.NotificationComponent
import aeb.proyecto.addhabit.components.commom.textField.AddHabitTextField
import aeb.proyecto.addhabit.components.commom.textField.TrailingIcon
import aeb.proyecto.addhabit.components.commom.typeHabit.MonthlyTypeHabit
import aeb.proyecto.addhabit.components.commom.typeHabit.RecurringTypeHabit
import aeb.proyecto.addhabit.components.commom.typeHabit.WeeklyTypeHabit
import aeb.proyecto.addhabit.constants.GridOption
import aeb.proyecto.addhabit.constants.GridOptionResult
import aeb.proyecto.addhabit.constants.PICK_DATE
import aeb.proyecto.addhabit.constants.PICK_NOTIFICATION
import aeb.proyecto.addhabit.constants.PICK_TYPE_HABIT
import aeb.proyecto.addhabit.constants.PICK_TYPE_NOTIFICATION
import aeb.proyecto.addhabit.constants.PICK_UNIT
import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.addhabit.constants.TypeNotificationResult
import aeb.proyecto.addhabit.model.DataAddHabitScreen
import aeb.proyecto.addhabit.utils.OnChangePermissions
import aeb.proyecto.addhabit.utils.getTextUnits
import aeb.proyecto.addhabit.utils.goToAppSettings
import aeb.proyecto.room.model.classes.TypeNotification
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.unitsHourMode
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing28
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.regexTextField.IsOnlyDigit
import aeb.proyecto.ui.regexTextField.IsOnlyZeroTo59
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun HorizontalAddHabitScreen(
    dataAddHabit: DataAddHabitScreen,
    uiState: AddHabitUIState,
    navigateToHabit: () -> Unit,
    onClickCard: (GridOption) -> Unit,
    onClickGridOption: (GridOptionResult) -> Unit = {},
    onClickDialog: (Int) -> Unit = {},
    onDismissDialog: () -> Unit = {},
    onClickTypeHabit: (TypeHabit) -> Unit = {},
    onClickWeekly: (Int) -> Unit = {},
    onMonthNumberSelected: (Int) -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
    onPickUnit: (UnitHabit) -> Unit = {},
    onClickTypeNotification: (TypeNotification) -> Unit = {},
    onTimeSelected: (LocalTime) -> Unit = {},
    onClickDeleteNotification: (String) -> Unit = {},
    onCheckedWeeklyChange:() -> Unit,
    onCheckedMonthlyChange:() -> Unit,
    onClickTypeNotificationResult: (TypeNotificationResult) -> Unit = {},
    onClickEditNotification: (String, LocalTime) -> Unit = { _, _ ->},
    onDismiss: () -> Unit = {},
    onAccept: () -> Unit = {}
){

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val density = LocalDensity.current
    val habit = dataAddHabit.habitScreen

    val notificationWeek = habit.notifications.filter { it.type is TypeNotification.Daily }
    val notificationRecurring = habit.notifications.filter { it.type is TypeNotification.Recurring }

    val isPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    OnChangePermissions(isPermissionGranted,context)

    IsOnlyDigit(habit.numberTimesTextField,habit.unit)
    IsOnlyDigit(habit.firstHourTimesTextField)
    IsOnlyZeroTo59(habit.secondHourTimesTextField)

    when(uiState){
        AddHabitUIState.Error, AddHabitUIState.Success -> Unit
        AddHabitUIState.Loading -> {
            AddHabitLoading()
        }
        AddHabitUIState.ToHabit -> {
            LaunchedEffect (Unit){
                navigateToHabit()
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = spacing12, end = spacing12, top = spacing12)
            .padding(
                WindowInsets.navigationBars
                    .only(WindowInsetsSides.Bottom)
                    .asPaddingValues()
            )
            .verticalScroll(rememberScrollState())
    ){

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ){

            Column (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                AnimatedVisibility(
                    visible = habit.unit !in unitsHourMode
                ) {
                    Spacer(modifier = Modifier.padding(vertical = spacing4))
                }

                //TextField Nombre y descripción
                AddHabitTextField(
                    textFieldState = habit.nameTextField,
                    modifier = Modifier.height(60.dp),
                    label = { LabelMediumText(stringResource(R.string.add_habit_name_label)) },
                    labelPosition = TextFieldLabelPosition.Above(),
                    focusManager = focusManager,
                    imeAction = ImeAction.Done,
                    trailingIcon = { TrailingIcon(habit.nameTextField) },
                    contentPadding = PaddingValues(start = spacing12),
                    keyboardType = KeyboardType.Text
                )

                AddHabitTextField(
                    modifier = Modifier
                        .padding(top = spacing10)
                        .height(60.dp),
                    textFieldState = habit.descriptionTextField,
                    label = { LabelMediumText(stringResource(R.string.add_habit_description_label)) },
                    focusManager = focusManager,
                    labelPosition = TextFieldLabelPosition.Above(),
                    imeAction = ImeAction.Done,
                    trailingIcon = { TrailingIcon(habit.descriptionTextField) },
                    contentPadding = PaddingValues(start = spacing12),
                    keyboardType = KeyboardType.Text
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = spacing12))

            Column (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                //Unidades y veces
                LabelMediumText(stringResource(R.string.add_habit_times_and_units),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left)

                Spacer(modifier = Modifier.padding(vertical = spacing2))

                AnimatedContent(
                    targetState = habit.unit in unitsHourMode,
                    label = "UnitContentAnimation"
                ) { isHourMode ->
                    if (!isHourMode) {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            AddHabitTextField(
                                textFieldState = habit.numberTimesTextField,
                                modifier = Modifier
                                    .height(45.dp)
                                    .padding(top = spacing2),
                                focusManager = focusManager,
                                trailingIcon = { TrailingIcon(habit.numberTimesTextField) },
                                imeAction = ImeAction.Done,
                                contentPadding = PaddingValues(start = spacing12),
                                keyboardType = KeyboardType.Number
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column (
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = spacing2)
                            ){
                                AddHabitTextField(
                                    textFieldState = habit.firstHourTimesTextField,
                                    modifier = Modifier
                                        .height(45.dp),
                                    focusManager = focusManager,
                                    imeAction = ImeAction.Next,
                                    contentPadding = PaddingValues(horizontal = spacing12),
                                    keyboardType = KeyboardType.Number
                                )

                                LabelSmallText(
                                    stringResource(habit.unit.titlePlural),
                                    color = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.padding(top = spacing1)
                                )
                            }

                            LabelLargeText(
                                stringResource(R.string.add_habit_dots),
                                fontSize = 40.sp,
                                modifier = Modifier
                                    .padding(horizontal = spacing4)
                                    .offset(y = (-10).dp)
                            )

                            Column (
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = spacing2)
                            ){
                                val label = remember (habit.unit){
                                    if(habit.unit == UnitHabit.HOURS)
                                        UnitHabit.MINUTES.titlePlural
                                    else
                                        UnitHabit.SECONDS.titlePlural
                                }

                                AddHabitTextField(
                                    textFieldState = habit.secondHourTimesTextField,
                                    modifier = Modifier
                                        .height(45.dp),
                                    focusManager = focusManager,
                                    placeholder = {
                                        Text(
                                            stringResource(R.string.add_habit_zero),
                                            color = MaterialTheme.colorScheme.onSurface)
                                    },
                                    imeAction = ImeAction.Done,
                                    contentPadding = PaddingValues(horizontal = spacing12),
                                    keyboardType = KeyboardType.Number
                                )

                                LabelSmallText(
                                    stringResource(label),
                                    color = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.padding(top = spacing1)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = spacing8))

                AddHabitCardButton(
                    title = getTextUnits(habit.numberTimesTextField, habit.firstHourTimesTextField, habit.unit),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onClickDialog(PICK_UNIT) }
                )
            }
        }

        CustomHorizontalDivider(modifier = Modifier.padding(top = spacing28, bottom = spacing16))

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

        CustomHorizontalDivider(modifier = Modifier.padding(top = spacing20, bottom = spacing16))

        // Tipo de hábito
        LabelLargeText(
            stringResource(R.string.add_habit_pick_type_habit_title),
            modifier = Modifier.padding(bottom = spacing8))

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
                        weeklyGoal = habit.weeklyGoal,
                        colorSelected = habit.color,
                        contrastColor = dataAddHabit.contrastColor,
                        onCheckedChange = onCheckedWeeklyChange,
                        onClickWeekly = onClickWeekly)
                }

                TypeHabit.MONTHLY -> {
                    MonthlyTypeHabit(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing10),
                        colorSelected = habit.color,
                        contrastColor = dataAddHabit.contrastColor,
                        numberSelected = habit.numberOfDaysMonth,
                        monthlyGoal = habit.monthlyGoal,
                        onCheckedMonthly = onCheckedMonthlyChange,
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

        //Notificaciones
        if(isPermissionGranted.value){

            CustomHorizontalDivider(modifier = Modifier.padding(top = spacing28, bottom = spacing16))

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
                LabelMediumText(
                    stringResource(R.string.add_habit_notificacionWeek),
                    modifier = Modifier.padding(top = spacing10, bottom = spacing4))

                // Divide notificationWeek en grupos de 2
                val rows = notificationWeek.chunked(2)

                rows.forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { notification ->
                            NotificationComponent(
                                notification = notification,
                                color = habit.color,
                                contrastColor = dataAddHabit.contrastColor,
                                startDayOfWeek = dataAddHabit.dayStartWeek,
                                onClickDelete = onClickDeleteNotification,
                                onClickTypeNotification = onClickTypeNotificationResult,
                                onClickEdit = onClickEditNotification,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }

                        // Si hay solo un elemento en esta fila (es impar), añade un Spacer para completar la fila
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            if(notificationRecurring.isNotEmpty()){
                LabelMediumText(
                    stringResource(R.string.add_habit_notificacionRecurring),
                    modifier = Modifier.padding(top = spacing10, bottom = spacing4))

                // Divide notificationWeek en grupos de 2
                val rows = notificationRecurring.chunked(2)

                rows.forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { notification ->
                            NotificationComponent(
                                notification = notification,
                                color = habit.color,
                                contrastColor = dataAddHabit.contrastColor,
                                startDayOfWeek = dataAddHabit.dayStartWeek,
                                onClickDelete = onClickDeleteNotification,
                                onClickTypeNotification = onClickTypeNotificationResult,
                                onClickEdit = onClickEditNotification,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }

                        // Si hay solo un elemento en esta fila (es impar), añade un Spacer para completar la fila
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }else{

            CustomHorizontalDivider(modifier = Modifier.padding(top = spacing28, bottom = spacing16))

            LabelMediumText(stringResource(R.string.add_habit_no_permissions))

            Button(onClick = {
                goToAppSettings(context)
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing12),
                shape = RoundedCornerShape(spacing8),
                colors = ButtonDefaults.buttonColors(
                    containerColor = habit.color
                )){
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        Icons.Filled.Key,
                        contentDescription = "button settings",
                        tint = dataAddHabit.contrastColor
                    )

                    LabelMediumText(
                        stringResource(R.string.add_habit_no_permissions_button),
                        modifier = Modifier.padding(start = spacing8),
                        color = dataAddHabit.contrastColor
                    )
                }
            }
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
                    notification = dataAddHabit.notificationSelected,
                    contrastColor = dataAddHabit.contrastColor,
                    initialTimeMode = false,
                    onDismissRequest = onDismissDialog,
                    onConfirm = onTimeSelected
                )
            }

            else -> {}
        }
    }

    if(dataAddHabit.bottomSheetState.isVisible){
        AddBottomSheet(
            dataBottomSheet = dataAddHabit.bottomSheetState.dataBottomSheet,
            onDismiss = onDismiss,
            color = dataAddHabit.habitScreen.color,
            contrastColor = dataAddHabit.contrastColor,
            onAccept = onAccept
        )
    }
}