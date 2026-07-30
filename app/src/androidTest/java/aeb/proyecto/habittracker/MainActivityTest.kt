package aeb.proyecto.habittracker

import aeb.proyecto.domain.usecase.main.ShowDialogState
import aeb.proyecto.habittracker.components.dialog.ManageDialogScreen
import aeb.proyecto.room.entities.relations.HabitWithDay
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MainActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun given_manage_dialog_when_unit_then_show_the_correct_screen(){

        composeTestRule.setContent {
            ManageDialogScreen(
                state = ShowDialogState.NoShowDialog,
                onDismissRequest = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithTag("main_manage_dialog_screen").assertDoesNotExist()
    }

    @Test
    fun given_manage_dialog_when_show_dialog_then_show_the_correct_screen(){

        composeTestRule.setContent {
            ManageDialogScreen(
                state = ShowDialogState.ShowDialog(
                    habit = HabitWithDay(),
                    time = 100000
                ),
                onDismissRequest = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithTag("main_manage_dialog_screen").assertIsDisplayed()
    }

    @Test
    fun given_manage_dialog_when_press_cancel_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            ManageDialogScreen(
                state = ShowDialogState.ShowDialog(
                    habit = HabitWithDay(),
                    time = 100000
                ),
                onDismissRequest = { clicked = true },
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithTag("main_cancel_button_dialog").performClick()
        assertTrue(clicked)
    }

    @Test
    fun given_manage_dialog_when_press_accept_then_performs_click(){
        var clicked = false

        composeTestRule.setContent {
            ManageDialogScreen(
                state = ShowDialogState.ShowDialog(
                    habit = HabitWithDay(),
                    time = 100000
                ),
                onDismissRequest = {},
                onConfirm = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("main_accept_button_dialog").performClick()
        assertTrue(clicked)
    }
}