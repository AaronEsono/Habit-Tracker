package aeb.proyecto.addhabit

import aeb.proyecto.addhabit.components.common.bottomSheet.AddBottomSheet
import aeb.proyecto.addhabit.components.common.button.PickTypeHabitButton
import aeb.proyecto.addhabit.components.common.card.AddHabitCard
import aeb.proyecto.addhabit.components.common.card.AddHabitCardButton
import aeb.proyecto.addhabit.components.common.dialog.DatePickerDialogHabit
import aeb.proyecto.addhabit.components.common.dialog.PickTypeHabitDialog
import aeb.proyecto.addhabit.components.common.dialog.PickTypeNotificationDialog
import aeb.proyecto.addhabit.components.common.dialog.PickUnitDialog
import aeb.proyecto.addhabit.components.common.dialog.TimePickerDialog
import aeb.proyecto.addhabit.components.common.dialog.UnitCard
import aeb.proyecto.addhabit.components.common.grid.AddHabitGrid
import aeb.proyecto.addhabit.components.common.notifications.ArrowCyclicButton
import aeb.proyecto.addhabit.components.common.notifications.NotificationComponent
import aeb.proyecto.addhabit.components.common.notifications.NotificationDayButton
import aeb.proyecto.addhabit.components.common.typeHabit.MonthlyTypeHabit
import aeb.proyecto.addhabit.components.common.typeHabit.NumberPicker
import aeb.proyecto.addhabit.components.common.typeHabit.RecurringTypeHabit
import aeb.proyecto.addhabit.components.common.typeHabit.WeeklyButton
import aeb.proyecto.addhabit.components.common.typeHabit.WeeklyTypeHabit
import aeb.proyecto.addhabit.components.common.typeHabit.colorText
import aeb.proyecto.addhabit.components.vertical.VerticalAddHabitScreen
import aeb.proyecto.addhabit.constants.GridOption
import aeb.proyecto.addhabit.constants.GridOptionResult
import aeb.proyecto.addhabit.model.AddHabit
import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.addhabit.model.DataAddHabitScreen
import aeb.proyecto.addhabit.model.DataBottomSheet
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.room.model.classes.TypeNotification
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.ui.constants.listColors
import aeb.proyecto.ui.constants.listIcons
import android.Manifest
import androidx.activity.ComponentActivity
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.rule.GrantPermissionRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
@SmallTest
class AddHabitScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun given_vertical_add_habit_screen_when_loading_then_shows_loading_state(){

        composeTestRule.setContent {
            VerticalAddHabitScreen(
                dataAddHabit = DataAddHabitScreen(),
                uiState = AddHabitUIState.Loading,
                navigateToHabit = {},
                onClickCard = {},
                onClickGridOption = {},
                onClickDialog = {},
                onDismissDialog = {},
                onClickTypeHabit = {},
                onClickWeekly = {},
                onMonthNumberSelected = {},
                onDateSelected = {},
                onPickUnit = {},
                onClickTypeNotification = {},
                onTimeSelected = {},
                onClickDeleteNotification = {},
                onCheckedWeeklyChange = {},
                onCheckedMonthlyChange = {},
                onClickTypeNotificationResult = {},
                onClickEditNotification = { _, _ ->},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_loading_overlay").assertIsDisplayed()
    }

    @Test
    fun given_vertical_add_habit_screen_when_to_habit_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            VerticalAddHabitScreen(
                dataAddHabit = DataAddHabitScreen(),
                uiState = AddHabitUIState.ToHabit,
                navigateToHabit = { clicked = true },
                onClickCard = {},
                onClickGridOption = {},
                onClickDialog = {},
                onDismissDialog = {},
                onClickTypeHabit = {},
                onClickWeekly = {},
                onMonthNumberSelected = {},
                onDateSelected = {},
                onPickUnit = {},
                onClickTypeNotification = {},
                onTimeSelected = {},
                onClickDeleteNotification = {},
                onCheckedWeeklyChange = {},
                onCheckedMonthlyChange = {},
                onClickTypeNotificationResult = {},
                onClickEditNotification = { _, _ ->},
                onDismiss = {},
                onAccept = {}
            )
        }

        assertTrue(clicked)
    }

    @Test
    fun given_vertical_add_habit_screen_when_success_then_shows_the_screen(){

        composeTestRule.setContent {
            VerticalAddHabitScreen(
                dataAddHabit = DataAddHabitScreen(),
                uiState = AddHabitUIState.Success,
                navigateToHabit = {},
                onClickCard = {},
                onClickGridOption = {},
                onClickDialog = {},
                onDismissDialog = {},
                onClickTypeHabit = {},
                onClickWeekly = {},
                onMonthNumberSelected = {},
                onDateSelected = {},
                onPickUnit = {},
                onClickTypeNotification = {},
                onTimeSelected = {},
                onClickDeleteNotification = {},
                onCheckedWeeklyChange = {},
                onCheckedMonthlyChange = {},
                onClickTypeNotificationResult = {},
                onClickEditNotification = { _, _ ->},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_screen_vertical").assertIsDisplayed()
    }

    @Test
    fun given_add_habit_card_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            AddHabitCard(
                title = "title",
                color = Color.Blue,
                icon = Icons.Filled.Add,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("add_habit_card", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_add_habit_grid_when_colors_then_shows_the_grid(){

        composeTestRule.setContent {
            AddHabitGrid(
                gridOption = GridOption.COLORS,
                colorSelected = Color.Blue,
                iconSelected = Icons.Filled.Add,
                onClickGridOption = {},
            )
        }

        composeTestRule.onNodeWithTag("add_habit_grid_color_${listColors[0].hashCode()}", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("add_habit_grid_icon_${listIcons[1].hashCode()}", useUnmergedTree = true).assertIsNotDisplayed()
    }

    @Test
    fun given_add_habit_grid_when_icons_then_shows_the_grid(){

        composeTestRule.setContent {
            AddHabitGrid(
                gridOption = GridOption.ICONS,
                colorSelected = Color.Blue,
                iconSelected = Icons.Filled.Add,
                onClickGridOption = {},
            )
        }

        composeTestRule.onNodeWithTag("add_habit_grid_color_${listColors[0].hashCode()}", useUnmergedTree = true).assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("add_habit_grid_icon_${listIcons[1].hashCode()}", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun given_add_habit_grid_when_colors_then_performs_click(){
        var gridOptionResult: GridOptionResult = GridOptionResult.colorResult(Color.Blue)

        composeTestRule.setContent {
            AddHabitGrid(
                gridOption = GridOption.COLORS,
                colorSelected = Color.Blue,
                iconSelected = Icons.Filled.Add,
                onClickGridOption = { gridOption -> gridOptionResult = gridOption },
            )
        }

        composeTestRule.onNodeWithTag("add_habit_grid_color_${listColors[0].hashCode()}", useUnmergedTree = true).performClick()
        assertEquals(GridOptionResult.colorResult(listColors[0]), gridOptionResult)
    }

    @Test
    fun given_add_habit_grid_when_icons_then_then_performs_click(){
        var gridOptionResult: GridOptionResult = GridOptionResult.colorResult(Color.Blue)

        composeTestRule.setContent {
            AddHabitGrid(
                gridOption = GridOption.ICONS,
                colorSelected = Color.Blue,
                iconSelected = Icons.Filled.Add,
                onClickGridOption = { gridOption -> gridOptionResult = gridOption },
            )
        }

        composeTestRule.onNodeWithTag("add_habit_grid_icon_${listIcons[1].hashCode()}", useUnmergedTree = true).performClick()
        assertEquals(GridOptionResult.iconResult(listIcons[1]), gridOptionResult)
    }

    @Test
    fun given_add_habit_card_button_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            AddHabitCardButton(
                title = "title",
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("add_habit_card_button", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_weekly_type_habit_when_clicked_switch_then_perform_click(){
        var clicked = false

        composeTestRule.setContent {
            WeeklyTypeHabit(
                weeklyGoal = false,
                numberSelected = 1,
                colorSelected = Color.Blue,
                contrastColor = Color.Red,
                onClickWeekly = {},
                onCheckedChange = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("add_habit_weekly_switch", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_weekly_type_habit_when_weekly_goal_on_false_then_hide_screen(){

        composeTestRule.setContent {
            WeeklyTypeHabit(
                weeklyGoal = true,
                numberSelected = 1,
                colorSelected = Color.Blue,
                contrastColor = Color.Red,
                onClickWeekly = {},
                onCheckedChange = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_weekly_row", useUnmergedTree = true).assertIsNotDisplayed()
    }

    @Test
    fun given_weekly_type_habit_when_weekly_goal_on_true_then_show_screen(){

        composeTestRule.setContent {
            WeeklyTypeHabit(
                weeklyGoal = false,
                numberSelected = 1,
                colorSelected = Color.Blue,
                contrastColor = Color.Red,
                onClickWeekly = {},
                onCheckedChange = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_weekly_row", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun given_weekly_button_when_clicked_switch_then_perform_click(){
        var clicked = false

        composeTestRule.setContent {
            WeeklyButton(
                number = 1,
                selected = false,
                colorSelected = Color.Blue,
                contrastColor = Color.Red,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("weekly_button_1", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_monthly_type_habit_when_clicked_switch_then_perform_click(){
        var clicked = false

        composeTestRule.setContent {
            MonthlyTypeHabit(
                monthlyGoal = false,
                numberSelected = 1,
                colorSelected = Color.Blue,
                contrastColor = Color.Red,
                onCheckedMonthly = { clicked = true  },
                onNumberSelected = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_monthly_switch", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_monthly_type_habit_when_weekly_goal_on_false_then_hide_screen(){

        composeTestRule.setContent {
            MonthlyTypeHabit(
                monthlyGoal = true,
                numberSelected = 1,
                colorSelected = Color.Blue,
                contrastColor = Color.Red,
                onCheckedMonthly = {},
                onNumberSelected = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_monthly_title", useUnmergedTree = true).assertIsNotDisplayed()
    }

    @Test
    fun given_monthly_type_habit_when_weekly_goal_on_true_then_show_screen(){

        composeTestRule.setContent {
            MonthlyTypeHabit(
                monthlyGoal = false,
                numberSelected = 1,
                colorSelected = Color.Blue,
                contrastColor = Color.Red,
                onCheckedMonthly = {},
                onNumberSelected = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_monthly_title", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun numberPicker_initialState_displaysSelectedNumber() {
        // --- GIVEN ---
        val initialSelectedNumber = 5

        // --- WHEN ---
        composeTestRule.setContent {
            NumberPicker(
                numberSelected = initialSelectedNumber,
                onNumberSelected = {}
            )
        }

        // --- THEN ---
        composeTestRule
            .onNodeWithText("$initialSelectedNumber")
            .assertIsDisplayed()
    }

    @Test
    fun numberPicker_whenItemClicked_invokesOnNumberSelected() {
        // --- GIVEN ---
        val initialNumber = 1
        val targetNumber = 3
        var capturedSelectedNumber = initialNumber

        composeTestRule.setContent {
            NumberPicker(
                numberSelected = initialNumber,
                onNumberSelected = { selected ->
                    capturedSelectedNumber = selected
                }
            )
        }

        // --- WHEN ---
        composeTestRule
            .onNodeWithText("$targetNumber")
            .performClick()

        composeTestRule.waitForIdle()

        // --- THEN ---
        assertEquals(targetNumber, capturedSelectedNumber)
    }

    @Test
    fun colorText_whenPageMatchesSelected_returnsContrastColor() {
        // --- GIVEN ---
        val page = 5
        val numberSelected = 5
        val contrastColor = Color.Red

        // --- WHEN ---
        var resultColor: Color = Color.Unspecified
        composeTestRule.setContent {
            resultColor = colorText(
                page = page,
                numberSelected = numberSelected,
                contrastColor = contrastColor
            )
        }

        // --- THEN ---
        assertEquals(contrastColor, resultColor)
    }

    @Test
    fun colorText_whenPageDoesNotMatchSelected_returnsOnSurfaceColor() {
        // --- GIVEN ---
        val page = 3
        val numberSelected = 5
        val contrastColor = Color.Red

        // --- WHEN ---
        var resultColor: Color = Color.Unspecified
        composeTestRule.setContent {
            resultColor = colorText(
                page = page,
                numberSelected = numberSelected,
                contrastColor = contrastColor
            )
        }

        // --- THEN ---
        assert(resultColor != contrastColor)
    }

    @Test
    fun given_recurring_type_habit_when_clicked_switch_then_perform_click(){
        var clicked = false

        composeTestRule.setContent {
            val focusManager = LocalFocusManager.current

            RecurringTypeHabit(
                focusManager = focusManager,
                intervalTextFieldState = TextFieldState(),
                color = Color.Red,
                date = LocalDate.now(),
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("add_habit_recurring_date", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_vertical_add_unit_habit_is_in_hour_mode_then_show_the_correct_screen(){

        composeTestRule.setContent {
            VerticalAddHabitScreen(
                dataAddHabit = DataAddHabitScreen(
                    habitScreen = AddHabit(
                        unit = UnitHabit.HOURS
                    )
                ),
                uiState = AddHabitUIState.Success,
                navigateToHabit = {},
                onClickCard = {},
                onClickGridOption = {},
                onClickDialog = {},
                onDismissDialog = {},
                onClickTypeHabit = {},
                onClickWeekly = {},
                onMonthNumberSelected = {},
                onDateSelected = {},
                onPickUnit = {},
                onClickTypeNotification = {},
                onTimeSelected = {},
                onClickDeleteNotification = {},
                onCheckedWeeklyChange = {},
                onCheckedMonthlyChange = {},
                onClickTypeNotificationResult = {},
                onClickEditNotification = { _, _ ->},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_chrono_input").assertIsDisplayed()
        composeTestRule.onNodeWithTag("add_habit_number_input").assertIsNotDisplayed()
    }

    @Test
    fun given_vertical_add_unit_habit_is_in_counter_mode_then_show_the_correct_screen(){

        composeTestRule.setContent {
            VerticalAddHabitScreen(
                dataAddHabit = DataAddHabitScreen(
                    habitScreen = AddHabit(
                        unit = UnitHabit.ATTEMPTS
                    )
                ),
                uiState = AddHabitUIState.Success,
                navigateToHabit = {},
                onClickCard = {},
                onClickGridOption = {},
                onClickDialog = {},
                onDismissDialog = {},
                onClickTypeHabit = {},
                onClickWeekly = {},
                onMonthNumberSelected = {},
                onDateSelected = {},
                onPickUnit = {},
                onClickTypeNotification = {},
                onTimeSelected = {},
                onClickDeleteNotification = {},
                onCheckedWeeklyChange = {},
                onCheckedMonthlyChange = {},
                onClickTypeNotificationResult = {},
                onClickEditNotification = { _, _ ->},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_chrono_input").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("add_habit_number_input").assertIsDisplayed()
    }

    @Test
    fun given_vertical_add_habit_when_has_permissions_then_show_the_correct_screen(){
        composeTestRule.setContent {

            VerticalAddHabitScreen(
                dataAddHabit = DataAddHabitScreen(),
                uiState = AddHabitUIState.Success,
                navigateToHabit = {},
                onClickCard = {},
                onClickGridOption = {},
                onClickDialog = {},
                onDismissDialog = {},
                onClickTypeHabit = {},
                onClickWeekly = {},
                onMonthNumberSelected = {},
                onDateSelected = {},
                onPickUnit = {},
                onClickTypeNotification = {},
                onTimeSelected = {},
                onClickDeleteNotification = {},
                onCheckedWeeklyChange = {},
                onCheckedMonthlyChange = {},
                onClickTypeNotificationResult = {},
                onClickEditNotification = { _, _ ->},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_notifications_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("add_habit_no_permissions").assertIsNotDisplayed()
    }

    @Test
    fun given_arrow_button_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            ArrowCyclicButton(
                icon = Icons.Filled.Add,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("add_habit_arrow_button", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_notification_button_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            NotificationDayButton(
                title = R.string.add_habit_icon_label,
                selected = true,
                colorSelected = Color.Red,
                contrastColor = Color.Red,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("add_habit_notification_day_button", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_notification_component_when_recurring_then_shows_the_correct_screen(){
        composeTestRule.setContent {
            NotificationComponent(
                notification = AddHabitNotification(
                    type = TypeNotification.Recurring(),
                    id = "1",
                    time = LocalTime.now()
                ),
                startDayOfWeek = DayOfWeek.MONDAY,
                color = Color.Red,
                contrastColor = Color.Red,
                onClickDelete = {  },
                onClickEdit = {_,_ ->},
                onClickTypeNotification = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_recurring_date_box", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("add_habit_notification_day_row").assertDoesNotExist()
    }

    @Test
    fun given_notification_component_when_daily_then_shows_the_correct_screen(){
        composeTestRule.setContent {
            NotificationComponent(
                notification = AddHabitNotification(
                    type = TypeNotification.Daily(),
                    id = "1",
                    time = LocalTime.now()
                ),
                startDayOfWeek = DayOfWeek.MONDAY,
                color = Color.Red,
                contrastColor = Color.Red,
                onClickDelete = {  },
                onClickEdit = {_,_ ->},
                onClickTypeNotification = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_recurring_date_box").assertDoesNotExist()
        composeTestRule.onNodeWithTag("add_habit_notification_day_row", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun given_notification_component_when_clicked_delete_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            NotificationComponent(
                notification = AddHabitNotification(
                    type = TypeNotification.Recurring(),
                    id = "1",
                    time = LocalTime.now()
                ),
                startDayOfWeek = DayOfWeek.MONDAY,
                color = Color.Red,
                contrastColor = Color.Red,
                onClickDelete = { clicked = true  },
                onClickEdit = {_,_ ->},
                onClickTypeNotification = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_notification_delete_button", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_pick_type_habit_dialog_when_dismiss_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            PickTypeHabitDialog(
                onDismissRequest = { clicked = true },
                onClickButton = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_dialog_close_button", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_pick_type_habit_dialog_when_type_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            PickTypeHabitDialog(
                onDismissRequest = { },
                onClickButton = {clicked = true}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_dialog_button_${aeb.proyecto.addhabit.constants.TypeHabit.DAILY.name}", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_date_picker_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            DatePickerDialogHabit(
                onDismissRequest = { clicked = true },
                colorSelected = Color.Red,
                contrastColor = Color.Red,
                onClickDate = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_date_picker_accept_button", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_unit_card_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            UnitCard(
                unit = UnitHabit.HOURS,
                colorSelected = Color.Red,
                contrastColor = Color.Red,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("add_habit_unit_card", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_pick_unit_dialog_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            PickUnitDialog(
                unitSeleted = UnitHabit.HOURS,
                colorSelected = Color.Red,
                contrastColor = Color.Red,
                onDismissRequest = { clicked = true },
                onClickButton = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_dialog_close_button", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_pick_type_habit_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            PickTypeHabitButton(
                title = "title",
                subtitle = "subtitle",
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("add_habit_dialog_button_pick_type_habit", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_pick_type_notification_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            PickTypeNotificationDialog(
                onDismissRequest = { clicked = true },
                onClickButton = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_dialog_close_button", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_time_picker_dialog_when_initial_mode_true_then_shows_the_correct_data(){

        composeTestRule.setContent {
            TimePickerDialog(
                notification = AddHabitNotification(
                    type = TypeNotification.Daily(),
                    id = "1",
                    time = LocalTime.now()
                ),
                color = Color.Red,
                contrastColor = Color.Red,
                onDismissRequest = {},
                onConfirm = {},
                initialTimeMode = true
            )
        }

        composeTestRule.onNodeWithTag("add_habit_time_picker_time_picker", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("add_habit_time_picker_time_input").assertDoesNotExist()
    }

    @Test
    fun given_time_picker_dialog_when_initial_mode_false_then_shows_the_correct_data(){

        composeTestRule.setContent {
            TimePickerDialog(
                notification = AddHabitNotification(
                    type = TypeNotification.Daily(),
                    id = "1",
                    time = LocalTime.now()
                ),
                color = Color.Red,
                contrastColor = Color.Red,
                onDismissRequest = {},
                onConfirm = {},
                initialTimeMode = false
            )
        }

        composeTestRule.onNodeWithTag("add_habit_time_picker_time_picker", useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag("add_habit_time_picker_time_input").assertIsDisplayed()
    }

    @Test
    fun given_time_picker_dialog_when_clicked_time_mode_then_performs_click(){

        composeTestRule.setContent {
            TimePickerDialog(
                notification = AddHabitNotification(
                    type = TypeNotification.Daily(),
                    id = "1",
                    time = LocalTime.now()
                ),
                color = Color.Red,
                contrastColor = Color.Red,
                onDismissRequest = {},
                onConfirm = {},
                initialTimeMode = true
            )
        }

        composeTestRule.onNodeWithTag("add_habit_time_picker_time_input").assertDoesNotExist()
        composeTestRule.onNodeWithTag("add_habit_time_picker_icon_time_mode", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag("add_habit_time_picker_time_input").assertIsDisplayed()
    }

    @Test
    fun given_time_picker_dialog_when_clicked_dismiss_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            TimePickerDialog(
                notification = AddHabitNotification(
                    type = TypeNotification.Daily(),
                    id = "1",
                    time = LocalTime.now()
                ),
                color = Color.Red,
                contrastColor = Color.Red,
                onDismissRequest = { clicked = true },
                onConfirm = {},
                initialTimeMode = true
            )
        }

        composeTestRule.onNodeWithTag("add_habit_time_picker_cancel_button", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_time_picker_dialog_when_clicked_confirm_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            TimePickerDialog(
                notification = AddHabitNotification(
                    type = TypeNotification.Daily(),
                    id = "1",
                    time = LocalTime.now()
                ),
                color = Color.Red,
                contrastColor = Color.Red,
                onDismissRequest = {},
                onConfirm = {clicked = true },
                initialTimeMode = true
            )
        }

        composeTestRule.onNodeWithTag("add_habit_time_picker_confirm_button", useUnmergedTree = true).performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_add_bottom_sheet_when_clicked_cancel_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            AddBottomSheet(
                dataBottomSheet = DataBottomSheet.DELETE_NOTIFICATION,
                color = Color.Red,
                contrastColor = Color.Red,
                onDismiss = { clicked = true },
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("add_habit_delete_notification", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()

        assertTrue(clicked)
    }

    @Test
    fun given_add_bottom_sheet_when_clicked_confirm_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            AddBottomSheet(
                dataBottomSheet = DataBottomSheet.DELETE_NOTIFICATION,
                color = Color.Red,
                contrastColor = Color.Red,
                onDismiss = {},
                onAccept = {clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("add_habit_accept_bottom_sheet", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()

        assertTrue(clicked)
    }


}