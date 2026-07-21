package aeb.proyecto.timer

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import aeb.proyecto.timer.components.common.button.AcceptButton
import aeb.proyecto.timer.components.common.button.InternalSegmentedButton
import aeb.proyecto.timer.components.common.habitLinked.HabitLinkedButton
import aeb.proyecto.timer.components.common.habitLinked.states.LinkedHabit
import aeb.proyecto.timer.components.common.infinitePicker.AlertDialogPicker
import aeb.proyecto.timer.components.common.infinitePicker.InfinitePicker
import aeb.proyecto.timer.components.common.infinitePicker.getCenteredIndex
import aeb.proyecto.timer.components.common.segmentedRow.SegmentedRow
import aeb.proyecto.timer.components.common.timeEntry.TimeEntry
import aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.IntervalSegmentedScreen
import aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.SetDialog
import aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.model.TypePickState
import aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.vertical.VerticalTimePicker
import aeb.proyecto.timer.components.vertical.VerticalTimerScreen
import aeb.proyecto.timer.components.vertical.components.screens.VerticalActiveTimerScreen
import aeb.proyecto.timer.components.vertical.components.screens.VerticalChoseTimerScreen
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.timer.constants.minutes
import aeb.proyecto.timer.model.HabitLinkedState
import aeb.proyecto.timer.model.HourSelectedState
import aeb.proyecto.timer.model.SegmentedButtonOptions
import aeb.proyecto.timer.model.TimeEntryState
import aeb.proyecto.timer.model.TimerDataUIState
import aeb.proyecto.timer.model.TimerServiceUIState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class TimerScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenAStateWhenLoadingThenShowsLoading(){

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalTimerScreen(
                timerUiState = TimerUiState.Loading,
                timerStopWatchUIState = TimerServiceUIState.NoTimer,
                listTimeEntryState = TimeEntryState.EmptyList,
                triggerSegmentedTimer = MutableSharedFlow(),
                bottomSheetState = false,
                onHourChange = {},
                onMinuteChange = {},
                onSecondChange = {},
                onTypeChange = {},
                onStartService = {},
                onSetChange = {},
                onIntervalHourChange = {_,_ ->},
                onButtonIntervalWorkChange = {},
                onButtonIntervalRestChange = {},
                onFinishButton = {},
                onResumeButton = {},
                onStopService = {},
                onCancelButton = {},
                onClickHabitButton = {},
                onDismissHabitBottomSheet = {},
                onAcceptBottomSheet = {_,_ ->},
                onClickCross = {},
                onClickTimeEntry = {},
                onClickFavorite = {_,_ ->},
                onClickDelete = {_ -> },
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("timer_loading").assertIsDisplayed()
    }

    @Test
    fun givenTimerStateWhenNoTimerThenShowsTheCorrectView(){

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalTimerScreen(
                timerUiState = TimerUiState.Success(
                    TimerDataUIState(
                        typeTimer = SegmentedButtonOptions.StopWatch,
                    )
                ),
                timerStopWatchUIState = TimerServiceUIState.NoTimer,
                listTimeEntryState = TimeEntryState.EmptyList,
                triggerSegmentedTimer = MutableSharedFlow(),
                bottomSheetState = false,
                onHourChange = {},
                onMinuteChange = {},
                onSecondChange = {},
                onTypeChange = {},
                onStartService = {},
                onSetChange = {},
                onIntervalHourChange = {_,_ ->},
                onButtonIntervalWorkChange = {},
                onButtonIntervalRestChange = {},
                onFinishButton = {},
                onResumeButton = {},
                onStopService = {},
                onCancelButton = {},
                onClickHabitButton = {},
                onDismissHabitBottomSheet = {},
                onAcceptBottomSheet = {_,_ ->},
                onClickCross = {},
                onClickTimeEntry = {},
                onClickFavorite = {_,_ ->},
                onClickDelete = {_ -> },
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("vertical_chose_timer_screen").assertIsDisplayed()
    }

    @Test
    fun givenTimerStateWhenNoTimerStopWatchThenShowsTheCorrectView(){

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalTimerScreen(
                timerUiState = TimerUiState.Success(
                    TimerDataUIState(
                        typeTimer = SegmentedButtonOptions.StopWatch,
                    )
                ),
                timerStopWatchUIState = TimerServiceUIState.NoTimer,
                listTimeEntryState = TimeEntryState.EmptyList,
                triggerSegmentedTimer = MutableSharedFlow(),
                bottomSheetState = false,
                onHourChange = {},
                onMinuteChange = {},
                onSecondChange = {},
                onTypeChange = {},
                onStartService = {},
                onSetChange = {},
                onIntervalHourChange = {_,_ ->},
                onButtonIntervalWorkChange = {},
                onButtonIntervalRestChange = {},
                onFinishButton = {},
                onResumeButton = {},
                onStopService = {},
                onCancelButton = {},
                onClickHabitButton = {},
                onDismissHabitBottomSheet = {},
                onAcceptBottomSheet = {_,_ ->},
                onClickCross = {},
                onClickTimeEntry = {},
                onClickFavorite = {_,_ ->},
                onClickDelete = {_ -> },
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("timer_stopwatch_label_no_timer").assertIsDisplayed()
    }

    @Test
    fun givenTimerStateWhenNoTimerTimerThenShowsTheCorrectView(){

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalTimerScreen(
                timerUiState = TimerUiState.Success(
                    TimerDataUIState(
                        typeTimer = SegmentedButtonOptions.Timer,
                    )
                ),
                timerStopWatchUIState = TimerServiceUIState.NoTimer,
                listTimeEntryState = TimeEntryState.EmptyList,
                triggerSegmentedTimer = MutableSharedFlow(),
                bottomSheetState = false,
                onHourChange = {},
                onMinuteChange = {},
                onSecondChange = {},
                onTypeChange = {},
                onStartService = {},
                onSetChange = {},
                onIntervalHourChange = {_,_ ->},
                onButtonIntervalWorkChange = {},
                onButtonIntervalRestChange = {},
                onFinishButton = {},
                onResumeButton = {},
                onStopService = {},
                onCancelButton = {},
                onClickHabitButton = {},
                onDismissHabitBottomSheet = {},
                onAcceptBottomSheet = {_,_ ->},
                onClickCross = {},
                onClickTimeEntry = {},
                onClickFavorite = {_,_ ->},
                onClickDelete = {_ -> },
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("time_picker_chose_screen").assertIsDisplayed()
    }


    @Test
    fun verify_currentItemSelected_is_triggered_on_initialization() {
        var selectedHour: String? = null

        composeTestRule.setContent {
            val initialIndex = getCenteredIndex(minutes.size, 2)
            val state = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

            InfinitePicker(
                items = minutes,
                listState = state,
                currentItemSelected = { selectedHour = it }
            )
        }

        // --- THEN ---
        assertNotNull(selectedHour)
        assertEquals("02", selectedHour)
    }

    @Test
    fun verify_onClickCenter_is_triggered_when_clicking_the_active_item() {
        var clickedCenterValue: String? = null
        var initialIndex = 0

        composeTestRule.setContent {
            initialIndex = getCenteredIndex(minutes.size, 0)
            val state = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

            InfinitePicker(
                items = minutes,
                listState = state,
                onClickCenter = { clickedCenterValue = it }
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("picker_item_index_$initialIndex").performClick()

        // --- THEN ---
        assertEquals("00", clickedCenterValue)
    }

    @Test
    fun verify_scrollToItem_is_triggered_when_clicking_a_non_active_item() {
        var scrollTargetIndex: Int? = null
        var initialIndex = 0

        composeTestRule.setContent {
            initialIndex = getCenteredIndex(minutes.size, 1)
            val state = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

            InfinitePicker(
                items = minutes,
                listState = state,
                scrollToItem = { scrollTargetIndex = it }
            )
        }

        // --- WHEN ---
        val neighborIndex = initialIndex + 1
        composeTestRule.onNodeWithTag("picker_item_index_$neighborIndex").performClick()

        // --- THEN ---
        assertNotNull(scrollTargetIndex)

        assertEquals(neighborIndex, scrollTargetIndex)
    }

    @Test
    fun givenADialogWhenClickedAcceptThenPerformsTheClick(){
        var acceptClicked = false

        composeTestRule.setContent {
            AlertDialogPicker(
                typeList = TypeUnitDate.Minutes,
                initialText = "00",
                onDismissRequest = {},
                onAccept = { acceptClicked = true }
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_alert_dialog_accept_button").performClick()

        //Then
        assertTrue(acceptClicked)
    }

    @Test
    fun givenADialogWhenClickedCancelThenPerformsTheClick(){
        var cancelClicked = false

        composeTestRule.setContent {
            AlertDialogPicker(
                typeList = TypeUnitDate.Minutes,
                initialText = "00",
                onDismissRequest = {cancelClicked = true},
                onAccept = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_alert_dialog_cancel_button").performClick()

        //Then
        assertTrue(cancelClicked)
    }

    @Test
    fun givenTimerStateWhenNoTimerIntervalThenShowsTheCorrectView(){

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalTimerScreen(
                timerUiState = TimerUiState.Success(
                    TimerDataUIState(
                        typeTimer = SegmentedButtonOptions.Interval,
                    )
                ),
                timerStopWatchUIState = TimerServiceUIState.NoTimer,
                listTimeEntryState = TimeEntryState.EmptyList,
                triggerSegmentedTimer = MutableSharedFlow(),
                bottomSheetState = false,
                onHourChange = {},
                onMinuteChange = {},
                onSecondChange = {},
                onTypeChange = {},
                onStartService = {},
                onSetChange = {},
                onIntervalHourChange = {_,_ ->},
                onButtonIntervalWorkChange = {},
                onButtonIntervalRestChange = {},
                onFinishButton = {},
                onResumeButton = {},
                onStopService = {},
                onCancelButton = {},
                onClickHabitButton = {},
                onDismissHabitBottomSheet = {},
                onAcceptBottomSheet = {_,_ ->},
                onClickCross = {},
                onClickTimeEntry = {},
                onClickFavorite = {_,_ ->},
                onClickDelete = {_ -> },
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("interval_segmented_screen").assertIsDisplayed()
    }

    @Test
    fun givenASegmentedButtonWhenClickedThenPerformsClick(){
        // Given
        var acceptClicked = false

        // --- WHEN ---
        composeTestRule.setContent {
            InternalSegmentedButton(
                size = 44.dp,
                onClickListener = { acceptClicked = true }
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("internal_segmented_button").performClick()
        assertTrue(acceptClicked)
    }

    @Test
    fun verify_clicking_work_time_not_opens_the_corresponding_dialog() {
        // --- GIVEN ---
        composeTestRule.setContent {
            IntervalSegmentedScreen(
                hourSelectedState = HourSelectedState.Data(Triple(0, 5, 0)),
                restSelectedState = HourSelectedState.NoData,
                setInterval = 3,
                onIntervalHourChange = { _, _ -> },
                onClickButtonWorkTime = {},
                onClickButtonRestTime = {},
                onSetIntervalChange = {}
            )
        }

        composeTestRule.onNodeWithTag("pick_hour_dialog").assertDoesNotExist()

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_interval_work_time").performClick()

        // --- THEN ---
        composeTestRule.onNodeWithTag("pick_hour_dialog").assertIsNotDisplayed()
    }

    @Test
    fun verify_clicking_rest_time_not_opens_the_corresponding_dialog() {
        // --- GIVEN ---
        composeTestRule.setContent {
            IntervalSegmentedScreen(
                hourSelectedState = HourSelectedState.Data(Triple(0, 5, 0)),
                restSelectedState = HourSelectedState.NoData,
                setInterval = 3,
                onIntervalHourChange = { _, _ -> },
                onClickButtonWorkTime = {},
                onClickButtonRestTime = {},
                onSetIntervalChange = {}
            )
        }

        composeTestRule.onNodeWithTag("pick_hour_dialog").assertDoesNotExist()

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_interval_rest_time").performClick()

        // --- THEN ---
        composeTestRule.onNodeWithTag("pick_hour_dialog").assertIsNotDisplayed()
    }

    @Test
    fun verify_clicking_interval_time_opens_the_corresponding_dialog() {
        // --- GIVEN ---
        composeTestRule.setContent {
            IntervalSegmentedScreen(
                hourSelectedState = HourSelectedState.Data(Triple(0, 5, 0)),
                restSelectedState = HourSelectedState.NoData,
                setInterval = 3,
                onIntervalHourChange = { _, _ -> },
                onClickButtonWorkTime = {},
                onClickButtonRestTime = {},
                onSetIntervalChange = {}
            )
        }

        composeTestRule.onNodeWithTag("pick_hour_dialog").assertDoesNotExist()

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_interval_interval_time").performClick()

        // --- THEN ---
        composeTestRule.onNodeWithTag("timer_alert_dialog").assertIsDisplayed()
    }

    @Test
    fun given_set_dialog_when_clicked_accept_then_performs_click(){
        var acceptClicked = false

        // --- GIVEN ---
        composeTestRule.setContent {
            SetDialog(
                initialText = "00",
                onDismissRequest = {},
                onAccept = { acceptClicked = true  }
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_alert_dialog_accept").performClick()

        // --- THEN ---
        assertTrue(acceptClicked)
    }

    @Test
    fun given_set_dialog_when_clicked_cancel_then_performs_click(){
        var cancelClicked = false

        // --- GIVEN ---
        composeTestRule.setContent {
            SetDialog(
                initialText = "00",
                onDismissRequest = {cancelClicked = true },
                onAccept = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_alert_dialog_cancel").performClick()

        // --- THEN ---
        assertTrue(cancelClicked)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun verify_clicking_cancel_button_triggers_onDismissRequest() {
        var onDismissCalled = false

        composeTestRule.setContent {
            val testSheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )

            VerticalTimePicker(
                hourSelectedState = HourSelectedState.Data(Triple(0, 0, 0)),
                label = "Test Label",
                typePickState = TypePickState.WORK_TIME,
                onIntervalHourChange = { _, _ -> },
                onDismissRequest = { onDismissCalled = true }
            )
        }

        composeTestRule.onNodeWithTag("vertical_picker_cancel_button").performClick()

        composeTestRule.waitForIdle()

        assertTrue(onDismissCalled)
    }

    @Test
    fun verify_clicking_accept_button_triggers_onIntervalHourChange_and_dismiss() {
        var onIntervalHourChangeCalled = false
        var onDismissCalled = false
        var capturedTriple: Triple<String, String, String>? = null

        // --- GIVEN ---
        composeTestRule.setContent {
            VerticalTimePicker(
                hourSelectedState = HourSelectedState.Data(Triple(0, 0, 0)),
                label = "Test Label",
                typePickState = TypePickState.WORK_TIME,
                onIntervalHourChange = { triple, _ ->
                    onIntervalHourChangeCalled = true
                    capturedTriple = triple
                },
                onDismissRequest = { onDismissCalled = true }
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("vertical_picker_accept_button").performClick()

        // --- THEN ---
        assertTrue(onIntervalHourChangeCalled)
        assertNotNull(capturedTriple)
    }


    @Test
    fun given_segmented_row_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            SegmentedRow(
                segmentedList = SegmentedButtonOptions.entries,
                onClickOption = { clicked = true },
                typeTimer = SegmentedButtonOptions.Timer
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_segmented_row_button_1").performClick()

        // --- THEN ---
        assertTrue(clicked)
    }

    @Test
    fun given_segmented_row_when_isSelected_then_has_the_correct_color(){

        composeTestRule.setContent {
            SegmentedRow(
                segmentedList = SegmentedButtonOptions.entries,
                onClickOption = {},
                typeTimer = SegmentedButtonOptions.Timer
            )
        }

        // --- THEN ---
        val timerIndex = SegmentedButtonOptions.entries.indexOf(SegmentedButtonOptions.Timer)
        composeTestRule
            .onNodeWithTag("timer_segmented_row_button_$timerIndex")
            .assertIsSelected()

        val otherIndex = SegmentedButtonOptions.entries.indexOf(SegmentedButtonOptions.StopWatch)
        composeTestRule
            .onNodeWithTag("timer_segmented_row_button_$otherIndex")
            .assertIsNotSelected()
    }

    @Test
    fun given_linked_button_when_clicked_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            HabitLinkedButton(
                linkedState = HabitLinkedState.Data(data = HabitWithDay()),
                onClickHabitLinkedButton = { clicked = true },
                onClickCross = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_habit_linked_button").performClick()

        // --- THEN ---
        assertTrue(clicked)
    }

    @Test
    fun given_linked_button_when_linkedData_then_shows_the_screen(){

        composeTestRule.setContent {
            HabitLinkedButton(
                linkedState = HabitLinkedState.Data(data = HabitWithDay()),
                onClickHabitLinkedButton = { },
                onClickCross = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_linked_habit_box", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun given_linked_button_when_no_data_then_shows_the_screen(){

        composeTestRule.setContent {
            HabitLinkedButton(
                linkedState = HabitLinkedState.NoData,
                onClickHabitLinkedButton = { },
                onClickCross = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_linked_habit_no_data", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun given_linked_button_when_linkedData_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            LinkedHabit(
                linkedState = HabitLinkedState.Data(data = HabitWithDay()),
    	            onClickCross = { clicked = true }
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_linked_habit_cross").performClick()

        //Then
        assertTrue(clicked)
    }

    @Test
    fun given_linked_button_when_acceptButton_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            AcceptButton(
                timerUIState = TimerUiState.Success(
                    TimerDataUIState(
                        habitLinked = HabitLinkedState.Data(data = HabitWithDay()),
                        buttonEnabled = true
                    )
                ),
                onStartService = { clicked = true }
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_accept_button_start_service").performClick()

        //Then
        assertTrue(clicked)
    }

    @Test
    fun given_time_entries_when_vertical_timer_then_show_header(){

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalChoseTimerScreen(
                timerUIState = TimerUiState.Success(
                    TimerDataUIState(
                        typeTimer = SegmentedButtonOptions.StopWatch,
                    )
                ),
                listTimeEntryState = TimeEntryState.TimeEntries(
                    timeEntries = listOf(TimeEntryWithHabit())
                ),
                triggerSegmentedTimer = MutableSharedFlow(),
                bottomSheetState = false,
                onHourChange = {},
                onMinuteChange = {},
                onSecondChange = {},
                onTypeChange = {},
                onStartService = {},
                onSetChange = {},
                onIntervalHourChange = {_,_ ->},
                onButtonIntervalWorkChange = {},
                onButtonIntervalRestChange = {},
                onClickHabitButton = {},
                onDismissHabitBottomSheet = {},
                onAcceptBottomSheet = {_,_ ->},
                onClickCross = {},
                onClickTimeEntry = {},
                onClickFavorite = {_,_ ->},
                onClickDelete = {_ -> },
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("timer_time_entry_header",  useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun given_no_entries_when_vertical_timer_then_show_header(){

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalChoseTimerScreen(
                timerUIState = TimerUiState.Success(
                    TimerDataUIState(
                        typeTimer = SegmentedButtonOptions.StopWatch,
                    )
                ),
                listTimeEntryState = TimeEntryState.EmptyList,
                triggerSegmentedTimer = MutableSharedFlow(),
                bottomSheetState = false,
                onHourChange = {},
                onMinuteChange = {},
                onSecondChange = {},
                onTypeChange = {},
                onStartService = {},
                onSetChange = {},
                onIntervalHourChange = {_,_ ->},
                onButtonIntervalWorkChange = {},
                onButtonIntervalRestChange = {},
                onClickHabitButton = {},
                onDismissHabitBottomSheet = {},
                onAcceptBottomSheet = {_,_ ->},
                onClickCross = {},
                onClickTimeEntry = {},
                onClickFavorite = {_,_ ->},
                onClickDelete = {_ -> },
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("timer_time_entry_header").assertIsNotDisplayed()
    }

    @Test
    fun given_time_entry_when_clicked_then_performs_button(){
        var clicked = false

        composeTestRule.setContent {
            TimeEntry(
                modifier = Modifier,
                timeEntry = TimeEntryWithHabit(),
                lastOne = false,
                onClickTimeEntry = { clicked = true },
                onClickFavorite = {_,_ ->},
                onClickDelete = {},
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_time_entry_principal_button").performClick()

        //Then
        assertTrue(clicked)
    }

    @Test
    fun given_time_entry_when_favorite_then_performs_button(){
        var clicked = false

        composeTestRule.setContent {
            TimeEntry(
                modifier = Modifier,
                timeEntry = TimeEntryWithHabit(),
                lastOne = false,
                onClickTimeEntry = {},
                onClickFavorite = {_,_ -> clicked = true },
                onClickDelete = {},
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_time_entry_favorite_no_clicked_button").performClick()

        //Then
        assertTrue(clicked)
    }

    @Test
    fun given_time_entry_when_delete_then_performs_button(){
        var clicked = false

        composeTestRule.setContent {
            TimeEntry(
                modifier = Modifier,
                timeEntry = TimeEntryWithHabit(),
                lastOne = false,
                onClickTimeEntry = { },
                onClickFavorite = {_,_ ->},
                onClickDelete = {clicked = true  },
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("timer_time_entry_delete_button").performClick()

        //Then
        assertTrue(clicked)
    }

    @Test
    fun given_active_timer_screen_when_clicked_resume_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            VerticalActiveTimerScreen(
                timerStopWatchUIState = TimerServiceUIState.TimerRunning(
                    currentState = StopwatchState.Stopped
                ),
                onCancelButton = {},
                onResumeButton = { clicked = true },
                onStopService = {},
                onFinishButton = {},
            )
        }

        // When
        composeTestRule.onNodeWithTag("timer_resume_button").performClick()

        // Then
        assertTrue(clicked)
    }

    @Test
    fun given_active_timer_screen_when_clicked_cancel_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            VerticalActiveTimerScreen(
                timerStopWatchUIState = TimerServiceUIState.TimerRunning(
                    currentState = StopwatchState.InProgress
                ),
                onCancelButton = {clicked = true },
                onResumeButton = {},
                onStopService = {},
                onFinishButton = {},
            )
        }

        // When
        composeTestRule.onNodeWithTag("timer_cancel_button").performClick()

        // Then
        assertTrue(clicked)
    }

    @Test
    fun given_active_timer_screen_when_clicked_stopeed_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            VerticalActiveTimerScreen(
                timerStopWatchUIState = TimerServiceUIState.TimerRunning(
                    currentState = StopwatchState.InProgress
                ),
                onCancelButton = {},
                onResumeButton = { },
                onStopService = { clicked = true },
                onFinishButton = {},
            )
        }

        // When
        composeTestRule.onNodeWithTag("timer_stop_button").performClick()

        // Then
        assertTrue(clicked)
    }

    @Test
    fun given_active_timer_screen_when_clicked_finish_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            VerticalActiveTimerScreen(
                timerStopWatchUIState = TimerServiceUIState.TimerRunning(
                    currentState = StopwatchState.Finished
                ),
                onCancelButton = {},
                onResumeButton = {},
                onStopService = {},
                onFinishButton = {clicked = true },
            )
        }

        // When
        composeTestRule.onNodeWithTag("timer_finish_button").performClick()

        // Then
        assertTrue(clicked)
    }

    @Test
    fun given_active_timer_screen_when_habit_linked_then_shows_the_habit(){

        composeTestRule.setContent {
            VerticalActiveTimerScreen(
                timerStopWatchUIState = TimerServiceUIState.TimerRunning(
                    currentState = StopwatchState.Finished,
                    habitLinked = HabitWithDay()
                ),
                onCancelButton = {},
                onResumeButton = {},
                onStopService = {},
                onFinishButton = {},
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("timer_habit_linked_screen").assertIsDisplayed()
    }

    @Test
    fun given_active_timer_screen_when_is_interval_then_shows_the_habit(){

        composeTestRule.setContent {
            VerticalActiveTimerScreen(
                timerStopWatchUIState = TimerServiceUIState.TimerRunning(
                    currentState = StopwatchState.Finished,
                    habitLinked = HabitWithDay(),
                    typeTimer = TypeTimer.INTERVAL(
                        time = 1,
                        rest = 1,
                        state = IntervalState.Work,
                        interval = 1
                    )
                ),
                onCancelButton = {},
                onResumeButton = {},
                onStopService = {},
                onFinishButton = {},
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("timer_interval_screen_active").assertIsDisplayed()
    }

    @Test
    fun given_active_timer_screen_when_is_stopwatch_then_shows_the_habit(){

        composeTestRule.setContent {
            VerticalActiveTimerScreen(
                timerStopWatchUIState = TimerServiceUIState.TimerRunning(
                    currentState = StopwatchState.Finished,
                    habitLinked = HabitWithDay(),
                    typeTimer = TypeTimer.STOPWATCH
                ),
                onCancelButton = {},
                onResumeButton = {},
                onStopService = {},
                onFinishButton = {},
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("timer_stopwatch_label").assertIsDisplayed()
    }

    @Test
    fun given_active_timer_screen_when_is_timer_then_shows_the_habit(){

        composeTestRule.setContent {
            VerticalActiveTimerScreen(
                timerStopWatchUIState = TimerServiceUIState.TimerRunning(
                    currentState = StopwatchState.Finished,
                    habitLinked = HabitWithDay(),
                    typeTimer = TypeTimer.TIMER(
                        time = 1,
                    )
                ),
                onCancelButton = {},
                onResumeButton = {},
                onStopService = {},
                onFinishButton = {},
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("timer_stopwatch_label").assertIsDisplayed()
    }

}