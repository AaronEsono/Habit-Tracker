package aeb.proyecto.addhabit.components.vertical

import aeb.proyecto.addhabit.AddHabitUIState
import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.components.common.bottomSheet.AddBottomSheet
import aeb.proyecto.addhabit.components.common.card.AddHabitCard
import aeb.proyecto.addhabit.components.common.card.AddHabitCardButton
import aeb.proyecto.addhabit.components.common.card.CardLeadingIconButton
import aeb.proyecto.addhabit.components.common.dialog.DatePickerDialogHabit
import aeb.proyecto.addhabit.components.common.dialog.PickTypeHabitDialog
import aeb.proyecto.addhabit.components.common.dialog.PickTypeNotificationDialog
import aeb.proyecto.addhabit.components.common.dialog.PickUnitDialog
import aeb.proyecto.addhabit.components.common.dialog.TimePickerDialog
import aeb.proyecto.addhabit.components.common.divider.CustomHorizontalDivider
import aeb.proyecto.addhabit.components.common.grid.AddHabitGrid
import aeb.proyecto.addhabit.components.common.loading.AddHabitLoading
import aeb.proyecto.addhabit.components.common.notifications.NotificationComponent
import aeb.proyecto.addhabit.components.common.textField.AddHabitTextField
import aeb.proyecto.addhabit.components.common.textField.TrailingIcon
import aeb.proyecto.addhabit.components.common.typeHabit.MonthlyTypeHabit
import aeb.proyecto.addhabit.components.common.typeHabit.RecurringTypeHabit
import aeb.proyecto.addhabit.components.common.typeHabit.WeeklyTypeHabit
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalTime

/**
 * Portrait implementation of the Habit Configuration Screen.
 * Assembles highly granular component atoms into a fluid, multi-column workspace. Coordinates responsive
 * state tracking, localized text filters, and security permission routines through unidirectional event propagation.
 *
 * @param dataAddHabit Central semantic state data wrapper containing the structural screen states and buffers.
 * @param uiState Current global asynchronous execution lifecycle checkpoint mapping transition events.
 * @param navigateToHabit Navigation router trigger dispatched to clear the stack and pop back onto dashboards.
 * @param onClickCard Component event firing localized asset selectors (Palettes or Icon inventories).
 * @param onClickGridOption Dispatches structural element updates chosen from layout catalog grids.
 * @param onClickDialog Injects visual state adjustments over overlay tracking indexes.
 * @param onDismissDialog Contextual layout command closing functional active dialog slots.
 * @param onClickTypeHabit Selects core behavior strategies (Daily, Weekly, Monthly, or Custom Recurring loops).
 * @param onClickWeekly Mutation lambda tracking day integer updates across weekly metrics.
 * @param onMonthNumberSelected Mutation lambda tracking day integer updates across monthly timelines.
 * @param onDateSelected Commits localized date updates picked from overlay calendars.
 * @param onPickUnit Locks core habit measurement units downstream.
 * @param onClickTypeNotification Append new dynamic notification templates matching user actions.
 * @param onTimeSelected Commits finalized structural local hours/minutes components upstream.
 * @param onClickDeleteNotification Dispatches removal commands targeted over specific reminder IDs.
 * @param onCheckedWeeklyChange Swaps focus strategies across cumulative or granular weekly metrics rules.
 * @param onCheckedMonthlyChange Swaps focus strategies across cumulative or granular monthly metrics rules.
 * @param onClickTypeNotificationResult Distributes incremental interval context variations downstream.
 * @param onClickEditNotification Invokes deep time tuning modifications over existing alerts trackers.
 * @param onDismiss Navigation back anchor clearing uncommitted modifications out of memory fields.
 * @param onAccept Direct confirmation layout switch commanding state data insertion processes.
 */
@Composable
fun VerticalAddHabitScreen(
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
    val habit = dataAddHabit.habitScreen

    // Pre-filter notification arrays by strategy to ease structural matrix distribution later
    val notificationWeek = habit.notifications.filter { it.type is TypeNotification.Daily }
    val notificationRecurring = habit.notifications.filter { it.type is TypeNotification.Recurring }

    // Evaluate hardware interaction tracking states defensively
    val isPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Trigger permission side effects monitors smoothly
    OnChangePermissions(isPermissionGranted,context)

    // Apply strict text filter sanitization properties onto input data state tracks
    IsOnlyDigit(habit.numberTimesTextField,habit.unit)
    IsOnlyDigit(habit.firstHourTimesTextField)
    IsOnlyZeroTo59(habit.secondHourTimesTextField)

    // Process asynchronous runtime interface flow directives
    when(uiState){
        AddHabitUIState.Error, AddHabitUIState.Success -> Unit
        AddHabitUIState.Loading -> {
            AddHabitLoading()
        }
        AddHabitUIState.ToHabit -> {
            // Unbind screen lifecycle safely inside a coroutine lateral side-effect layer
            LaunchedEffect (Unit){
                navigateToHabit()
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = spacing12, end = spacing12, top = spacing12)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .testTag("add_habit_screen_vertical")
    ){

        // ============================================================================
        // SECTION 1: NAME AND DESCRIPTION
        // ============================================================================

        // HABIT TITLE IDENTIFICATION TRAILING ROW
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

        // HABIT SUMMARY DESCRIPTION DETAIL TRACK
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

        CustomHorizontalDivider(modifier = Modifier.padding(top = spacing28, bottom = spacing16))


        // ============================================================================
        // SECTION 2: AESTHETICS & BRANDING DEPLOYMENT METRICS (COLORS & ICONS)
        // ============================================================================
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            // ACTIVE THEME CHROMATIC PALETTE PICKER TRIGGER CARD
            AddHabitCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.add_habit_color_label),
                icon = Icons.Filled.ColorLens,
                color = habit.color,
                onClick = { onClickCard(GridOption.COLORS) }
            )

            // VISUAL SYMBOL GLYPH IDENTIFIER PICKER TRIGGER CARD
            AddHabitCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.add_habit_icon_label),
                icon = habit.icon,
                color = habit.color,
                onClick = { onClickCard(GridOption.ICONS) }
            )
        }

        // INLINE EXPANSION: CHROMATIC PALETTE SELECTION MATRIX CATALOGUE
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

        // INLINE EXPANSION: IDENTIFIER SYMBOL GLYPH SELECTION MATRIX CATALOGUE
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

        // ============================================================================
        // SECTION 3: CORE TEMPORAL FREQUENCY ROUTINE STRATEGY PIPELINE
        // ============================================================================

        LabelLargeText(
            stringResource(R.string.add_habit_pick_type_habit_title),
            modifier = Modifier.padding(bottom = spacing8))

        // DIALOG TRIGGER HUB COMMANDING THE STRATEGY OVERLAY SELECTION DIALOG
        AddHabitCardButton(
            title = stringResource(habit.typeHabit.title),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClickDialog(PICK_TYPE_HABIT) }
        )

        // FLUID OVERLAY TRANSITION MUTATING ACCORDING TO COMMITTED RE-ACTIVE CALENDAR SCHEDULERS
        AnimatedContent(
            targetState = habit.typeHabit
        ) { typeHabit ->
            when (typeHabit) {
                // Daily strategy requires no additional target coordinates metrics
                TypeHabit.DAILY -> Unit

                // Injects specific granular layout tracks mapping designated weekly target loops
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

                // Injects structural configuration metrics targeted across localized monthly timelines
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

                // Injects dynamic specialized rolling interval components tracking custom cyclic loops
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

        CustomHorizontalDivider(modifier = Modifier.padding(top = spacing28, bottom = spacing16))

        // ============================================================================
        // SECTION 4: REPETITIONS ANS UNITS
        // ============================================================================
        LabelLargeText(stringResource(R.string.add_habit_times_and_units))

        // RE-EVALUATE WORKSPACE LAYOUT DIRECTIVES REFLECTING RE-ACTIVE CHOSEN MEASURE METRICS
        AnimatedContent(
            targetState = habit.unit in unitsHourMode,
            label = "UnitContentAnimation"
        ) { isHourMode ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing12),
            ) {
                if (!isHourMode) {
                    // MODE A: PROGRAMMATIC SINGLE SCALAR COUNTER INPUT NODE (E.G. REPETITIONS)
                    AddHabitTextField(
                        textFieldState = habit.numberTimesTextField,
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp)
                            .padding(top = spacing2)
                            .testTag("add_habit_number_input"),
                        focusManager = focusManager,
                        trailingIcon = { TrailingIcon(habit.numberTimesTextField) },
                        imeAction = ImeAction.Done,
                        contentPadding = PaddingValues(start = spacing12),
                        keyboardType = KeyboardType.Number
                    )
                } else {
                    // MODE B: CHRONO TWIN FOCUS METRICS TRAILING SPLIT ROW (E.G. HOURS : MINUTES)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("add_habit_chrono_input")
                    ) {
                        // COARSE HIGHER ORDER STEP CHRONO INPUT (HOURS / MINUTES)
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

                        // DECORATIVE TIME BOUNDARY SPLIT OPERATOR LABEL (: COLON MARKER)
                        LabelLargeText(
                            stringResource(R.string.add_habit_dots),
                            fontSize = 40.sp,
                            modifier = Modifier
                                .padding(horizontal = spacing4)
                                .offset(y = (-10).dp)
                        )

                        // REFINED SUB-STEP TIME TRACE INPUT (MINUTES / SECONDS TRACKER)
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

                Spacer(modifier = Modifier.padding(horizontal = spacing8))

                // CRADLE UNIT DIALOG CONTEXT PICKER TRIGGER BUTTON SHEET
                AddHabitCardButton(
                    title = getTextUnits(habit.numberTimesTextField, habit.firstHourTimesTextField, habit.unit),
                    modifier = Modifier.weight(1f),
                    onClick = { onClickDialog(PICK_UNIT) }
                )
            }
        }

        // ============================================================================
        //SECTION 5: SYSTEM PERMISSIONS BOUNDARY & LOCAL NOTIFICATION REGISTRY TRACK
        // ============================================================================
        if(isPermissionGranted.value){

            CustomHorizontalDivider(modifier = Modifier.padding(top = spacing28, bottom = spacing16))

            LabelLargeText(
                stringResource(R.string.add_habit_notifications_title),
                modifier = Modifier.testTag("add_habit_notifications_title")
            )

            // TRIGGER BUTTON SHEET TO APPEND NEW ALARM CONFIGURATION TEMPLATES
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

            // GRID FLOW SUB-SECTION A: GRANULAR WEEKLY/DAILY ALERTS MATRIX TREE
            if(notificationWeek.isNotEmpty()){
                LabelMediumText(
                    stringResource(R.string.add_habit_notificacionWeek),
                    modifier = Modifier.padding(top = spacing10, bottom = spacing4))

                notificationWeek.forEach {
                    NotificationComponent(
                        notification = it, color = habit.color,
                        contrastColor = dataAddHabit.contrastColor,
                        startDayOfWeek = dataAddHabit.dayStartWeek,
                        onClickDelete = onClickDeleteNotification,
                        onClickTypeNotification = onClickTypeNotificationResult,
                        onClickEdit = onClickEditNotification,
                        modifier = Modifier.padding(bottom = spacing8)
                    )
                }
            }

            // GRID FLOW SUB-SECTION B: GRANULAR ROLLING CYCLIC ALERT MATRIX TREE
            if(notificationRecurring.isNotEmpty()){
                LabelMediumText(
                    stringResource(R.string.add_habit_notificacionRecurring),
                    modifier = Modifier.padding(top = spacing10, bottom = spacing4))

                notificationRecurring.forEach {
                    NotificationComponent(
                        notification = it, color = habit.color,
                        contrastColor = dataAddHabit.contrastColor,
                        onClickDelete = onClickDeleteNotification,
                        onClickTypeNotification = onClickTypeNotificationResult,
                        onClickEdit = onClickEditNotification,
                        modifier = Modifier.padding(bottom = spacing8)
                    )
                }
            }

        }else{

            // ============================================================================
            // CONTINGENCY FALLBACK VIEW: POST_NOTIFICATIONS HARDWARE DEFENSE BLOCKER
            // ============================================================================

            CustomHorizontalDivider(modifier = Modifier.padding(top = spacing28, bottom = spacing16))

            LabelMediumText(
                stringResource(R.string.add_habit_no_permissions),
                modifier = Modifier.testTag("add_habit_no_permissions")
            )

            // ACTION BUTTON FORWARDING USER HARDWARE INSTRUCTIONS STRAIGHT TO OPERATING SYSTEM OPTIONS
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

    // ============================================================================
    // SECTION 6: MODAL CONTEXT ROUTER (STATE-DRIVEN DIALOG ORCHESTRATION PIPELINE)
    // ============================================================================
    if (dataAddHabit.showDialog) {

        // Contextual branching dispatcher evaluating designated programmatic tracking indexes
        when (dataAddHabit.typeDialog) {

            // ROUTE A: INFLATE STRATEGY TYPE SELECTION OVERLAY (DAILY, WEEKLY, MONTHLY, CYCLIC)
            PICK_TYPE_HABIT -> {
                PickTypeHabitDialog(
                    onDismissRequest = onDismissDialog,
                    onClickButton = onClickTypeHabit
                )
            }

            // ROUTE B: INFLATE CALENDAR SELECTOR SHEET FOR CYCLIC INITIALIZATION BASES
            PICK_DATE ->{
                DatePickerDialogHabit(
                    onDismissRequest = onDismissDialog,
                    colorSelected = habit.color,
                    contrastColor = dataAddHabit.contrastColor,
                    onClickDate = onDateSelected)
            }

            // ROUTE C: INFLATE SCALAR UNIT SPECS OVERLAY MATRIX (TIMES, HOURS, MINUTES, SECONDS)
            PICK_UNIT -> {
                PickUnitDialog(
                    onDismissRequest = onDismissDialog,
                    unitSeleted = habit.unit,
                    colorSelected = habit.color,
                    contrastColor = dataAddHabit.contrastColor,
                    onClickButton = onPickUnit
                )
            }

            // ROUTE D: INFLATE REMINDER DISPATCH STRATEGY OVERLAY WINDOW
            PICK_TYPE_NOTIFICATION ->{
                PickTypeNotificationDialog(
                    onDismissRequest = onDismissDialog,
                    onClickButton = onClickTypeNotification
                )
            }

            // ROUTE E: INFLATE DYNAMIC CHRONO ALARM TIME PICKER WORKSPACE SHEET
            PICK_NOTIFICATION -> {
                TimePickerDialog(
                    color = habit.color,
                    notification = dataAddHabit.notificationSelected,
                    contrastColor = dataAddHabit.contrastColor,
                    onDismissRequest = onDismissDialog,
                    onConfirm = onTimeSelected
                )
            }

            else -> {}
        }
    }

    // ============================================================================
    // SECTION 7: CONTEXTUAL PERSISTENT TRAY CONTAINER (MODAL BOTTOM SHEET WORKFLOW)
    // ============================================================================
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