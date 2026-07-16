package aeb.proyecto.save

import aeb.proyecto.save.components.vertical.VerticalSaveScreen
import aeb.proyecto.save.model.BottomSheetState
import aeb.proyecto.save.model.DataBottomSheet
import aeb.proyecto.save.model.DataSaveScreen
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@SmallTest
class SaveScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun givenLoadingState_whenScreenRendered_thenShowLoadingLayout() {
        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Loading,
                dataSaveScreen = DataSaveScreen(),
                bottomSheetState = BottomSheetState(),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("Save overlay").assertIsDisplayed()
    }

    @Test
    fun givenLogOutState_whenScreenRendered_thenFunchImportScreenWorks() {
        //Given
        var importClicked = false

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.LogOut,
                dataSaveScreen = DataSaveScreen(),
                bottomSheetState = BottomSheetState(),
                onImportScreen = { importClicked = true },
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        assertTrue(importClicked)
    }

    @Test
    fun givenACorrectName_WhenSaveScreenIsShow_ThenShowTheNameCorrectly() {
        //Given
        var name = "aaron@prueba.com"

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(name = name),
                bottomSheetState = BottomSheetState(),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("save_title_user").assertExists()
    }

    @Test
    fun givenAnInvalidName_WhenSaveScreenIsShow_ThenShowTheNameCorrectly() {
        //Given
        var name = ""

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(name = name),
                bottomSheetState = BottomSheetState(),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("save_title_user").assertDoesNotExist()
    }

    @Test
    fun givenACorrectDate_WhenSaveScreenIsShow_ThenShowTheRestoreButtonCorrectly() {
        //Given
        var date = LocalDateTime.now()

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(localDateTime = date),
                bottomSheetState = BottomSheetState(),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("save_button_restore").assertExists()
    }

    @Test
    fun givenAnInvalidtDate_WhenSaveScreenIsShow_ThenShowTheRestoreButtonCorrectly() {
        //Given
        var date = null

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(localDateTime = date),
                bottomSheetState = BottomSheetState(),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("save_button_restore").assertDoesNotExist()
    }

    @Test
    fun givenACorrectDate_WhenSaveScreenIsShow_ThenRestoreButtonPerformsClick() {
        //Given
        val date = LocalDateTime.now()
        var restoreClicked = false

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(localDateTime = date),
                bottomSheetState = BottomSheetState(),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = { restoreClicked = true },
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("save_button_restore").performClick()
        assertTrue(restoreClicked)
    }

    @Test
    fun givenACorrectDate_WhenSaveScreenIsShow_ThenShowTheDeleteButtonCorrectly() {
        //Given
        var date = LocalDateTime.now()

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(localDateTime = date),
                bottomSheetState = BottomSheetState(),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("save_button_delete").assertExists()
    }

    @Test
    fun givenAnInvalidtDate_WhenSaveScreenIsShow_ThenShowTheDeleteButtonCorrectly() {
        //Given
        var date = null

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(localDateTime = date),
                bottomSheetState = BottomSheetState(),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("save_button_delete").assertDoesNotExist()
    }

    @Test
    fun givenACorrectDate_WhenSaveScreenIsShow_ThenButtonPerformsClick() {
        //Given
        val date = LocalDateTime.now()
        var deleteClicked = false

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(localDateTime = date),
                bottomSheetState = BottomSheetState(),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {deleteClicked = true  },
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("save_button_delete").performClick()
        assertTrue(deleteClicked)
    }

    @Test
    fun whenSaveScreenIsShow_ThenLogOutPerformsClick() {
        //Given
        val date = LocalDateTime.now()
        var logOutClicked = false

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(localDateTime = date),
                bottomSheetState = BottomSheetState(),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = { logOutClicked = true },
                onDismiss = {},
                onAccept = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("save_button_logOut").performClick()
        assertTrue(logOutClicked)
    }

    @Test
    fun whenBottomSheetStateIsActive_thenShowTheBottomSheet(){
        //When
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(),
                bottomSheetState = BottomSheetState(showBottomSheet = true),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        //Then
        composeTestRule.onNodeWithTag("vertical_settings_dialog").assertIsDisplayed()
    }

    @Test
    fun givenAValueWhenBottomSheetStateIsActive_thenAcceptButtonWorks(){
        //Given
        var acceptClicked = false

        //When
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(),
                bottomSheetState = BottomSheetState(showBottomSheet = true),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {},
                onAccept = { acceptClicked = true }
            )
        }

        //Then
        composeTestRule.onNodeWithTag("vertical_settings_dialog_accept").performClick()
        assertTrue(acceptClicked)
    }

    @Test
    fun givenAValueWhenBottomSheetStateIsActiveAndValidState_thenAcceptButtonWorks(){
        //Given
        var cancelClicked = false

        //When
        composeTestRule.setContent {
            VerticalSaveScreen(
                saveUIState = SaveUIState.Success,
                dataSaveScreen = DataSaveScreen(),
                bottomSheetState = BottomSheetState(showBottomSheet = true, dataBottomSheet = DataBottomSheet.DELETE_HABIT),
                onImportScreen = {},
                onSaveClick = {},
                onRestoreClick = {},
                onDeleteClick = {},
                onLogOutClick = {},
                onDismiss = {cancelClicked = true},
                onAccept = {}
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("vertical_settings_dialog_cancel")
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()

        // Then
        assertTrue(cancelClicked)
    }
}