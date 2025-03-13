package aeb.proyecto.save

import aeb.proyecto.save.components.bottomSheet.SaveBottomSheet
import aeb.proyecto.save.model.DataBottomSheet
import aeb.proyecto.save.model.DataSaveScreen
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@SmallTest
class SaveScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun saveScreenShowCorrectly(){
        composeTestRule.setContent {

            SaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(name = "Aarón"),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.save_email, "Aarón"))
            .assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.save_save_habit))
            .assertExists().assertIsDisplayed()
    }

    @Test
    fun buttonWorksPerfectly(){
        var clickedSave = false
        var clickedRestore = false
        var clickedDelete = false
        var clickedLogOut = false

        composeTestRule.setContent {
            SaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(name = "Aarón",localDateTime = LocalDateTime.now()),
                onImportScreen = {},
                onSaveClick = { clickedSave = true },
                onRestoreClick = { clickedRestore = true },
                onDeleteClick = { clickedDelete = true },
                onLogOutClick = { clickedLogOut = true }
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.save_save_habit)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.save_restore_habit)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.save_delete_habit)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.save_log_out)).performClick()

        assert(clickedSave)
        assert(clickedRestore)
        assert(clickedDelete)
        assert(clickedLogOut)
    }

    @Test
    fun logOutStateWorks(){
        var goOnImport = false

        composeTestRule.setContent {
            SaveScreen(
                saveUIState = SaveUIState.LogOut,
                dataSaveScreen = DataSaveScreen(name = "Aarón",localDateTime = LocalDateTime.now()),
                onImportScreen = { goOnImport = true },
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {}
            )
        }

        assert(goOnImport)
    }

    @Test
    fun loadingStateWorks(){
        composeTestRule.setContent {
            SaveScreen(
                saveUIState = SaveUIState.Loading,
                dataSaveScreen = DataSaveScreen(name = "Aarón",localDateTime = LocalDateTime.now()),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {}
            )
        }

        composeTestRule.onNodeWithTag("Loading overlay")
            .assertExists().assertIsDisplayed()

    }

    @Test
    fun withoutNameNoDisplay(){
        composeTestRule.setContent {
            SaveScreen(
                saveUIState = SaveUIState.Loading,
                dataSaveScreen = DataSaveScreen(name = null,localDateTime = LocalDateTime.now()),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.save_email, "Aarón"))
            .assertIsNotDisplayed()

    }

    @Test
    fun withoutDateNoDisplay(){
        composeTestRule.setContent {
            SaveScreen(
                saveUIState = SaveUIState.Loading,
                dataSaveScreen = DataSaveScreen(name = null,localDateTime = null),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.save_restore_habit))
            .assertIsNotDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.save_delete_habit))
            .assertIsNotDisplayed()
    }

    @Test
    fun withDateNoDisplay(){
        composeTestRule.setContent {
            SaveScreen(
                saveUIState = SaveUIState.Loading,
                dataSaveScreen = DataSaveScreen(name = null,localDateTime = LocalDateTime.now()),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.save_restore_habit))
            .assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.save_delete_habit))
            .assertExists().assertIsDisplayed()
    }

    @Test
    fun stateBottomSheetCorrect(){
        composeTestRule.setContent {
            SaveBottomSheet(
                dataBottomSheet = DataBottomSheet.DELETE_HABIT,
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.data_bottomSheet_delete_label))
            .assertExists().assertIsDisplayed()
    }
}