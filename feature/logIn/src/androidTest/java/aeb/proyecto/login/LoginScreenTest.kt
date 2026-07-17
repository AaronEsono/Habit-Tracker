package aeb.proyecto.login

import aeb.proyecto.login.components.vertical.VerticalLoginScreen
import aeb.proyecto.login.model.BottomSheetState
import aeb.proyecto.login.model.DataLoginBottomSheet
import aeb.proyecto.login.model.DataLoginScreen
import android.content.Context
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun givenLoginState_WhenLoading_ThenShowLoading(){

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Loading,
                dataLoginScreen = DataLoginScreen(),
                bottomSheetState = BottomSheetState(),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("Login overlay").assertIsDisplayed()
    }

    @Test
    fun givenLoginState_WhenLogin_ThenFunctionWorks(){
        var loginClicked = false

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(),
                bottomSheetState = BottomSheetState(),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = { loginClicked = true },
                onDismiss = {},
                onAccept = {}
            )
        }

        assertEquals(true,loginClicked)
    }

    @Test
    fun givenLoginState_WhenSuccessAndLoginMode_ThenShowsCorrectTextFields(){

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(
                    isInLoginMode = true
                ),
                bottomSheetState = BottomSheetState(),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("login_email_textField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_password_textField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_repeat_password_textField").assertDoesNotExist()
    }

    @Test
    fun givenLoginState_WhenSuccessAndRegisterMode_ThenShowsCorrectTextFields(){

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(
                    isInLoginMode = false
                ),
                bottomSheetState = BottomSheetState(),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("login_email_textField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_password_textField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_repeat_password_textField").assertIsDisplayed()
    }

    @Test
    fun givenLoginState_WhenSuccessAndClickChangeWork_ThenUpdates(){
        var modeClicked = false

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(
                    isInLoginMode = false
                ),
                bottomSheetState = BottomSheetState(),
                onClickChecked = {},
                onClickLoginMode = { modeClicked = true },
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("Login_change_mode_button").performClick()

        assertEquals(true,modeClicked)
    }

    @Test
    fun givenLoginState_WhenSuccessAndClickAcceptButton_ThenDoNothing(){
        var acceptClicked = false

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(
                    isInLoginMode = false
                ),
                bottomSheetState = BottomSheetState(),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = { acceptClicked = true },
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("login_button_accept").performClick()

        assertEquals(false,acceptClicked)
    }

    @Test
    fun givenLoginState_WhenSuccessAndClickAcceptButtonAndCorrectTextFields_ThenCallsButton(){
        var acceptClicked = false

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(
                    isInLoginMode = true,
                    emailTextFieldState = TextFieldState(initialText = "aaron@altostrat.com"),
                    passwordTextFieldState = TextFieldState(initialText = "12345678"),
                ),
                bottomSheetState = BottomSheetState(),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = { acceptClicked = true },
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("login_button_accept").performClick()

        assertEquals(true,acceptClicked)
    }

    @Test
    fun givenLoginState_WhenSuccessAndRegisterAndClickAcceptButtonAndCorrectTextFields_ThenCallsButton(){
        var acceptClicked = false

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(
                    isInLoginMode = false,
                    emailTextFieldState = TextFieldState(initialText = "aaron@altostrat.com"),
                    passwordTextFieldState = TextFieldState(initialText = "12345678"),
                    rememberTextFieldState = TextFieldState(initialText = "12345678")
                ),
                bottomSheetState = BottomSheetState(),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = { acceptClicked = true },
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("login_button_accept").performClick()

        assertEquals(true,acceptClicked)
    }

    @Test
    fun givenLoginState_WhenSuccessAnForgotPassword_ThenPerformAction(){
        var forgotClicked = false

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(),
                bottomSheetState = BottomSheetState(),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = { forgotClicked = true  },
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("Login_forgot_password").performClick()

        assertEquals(true,forgotClicked)
    }

    @Test
    fun givenLoginState_WhenSuccessAnClickGoogleButton_ThenPerformAction(){
        var googleClicked = false

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(),
                bottomSheetState = BottomSheetState(),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = { googleClicked = true  },
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("Login_google_button").performClick()

        assertEquals(true,googleClicked)
    }

    @Test
    fun givenLoginState_WhenSuccessAnClickRememberButton_ThenPerformAction(){
        var remmeberClicked = false

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(),
                bottomSheetState = BottomSheetState(),
                onClickChecked = { remmeberClicked = true },
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("Login_checkbox_row").performClick()

        assertEquals(true,remmeberClicked)
    }

    @Test
    fun givenLoginState_WhenDialogIsInForget_ThenShowTheCorrectData(){

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(),
                bottomSheetState = BottomSheetState(
                    showBottomSheet = true,
                    dataBottomSheet = DataLoginBottomSheet.FORGOT_PASSWORD
                ),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("login_textField_dialog_forgot").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_button_cancel_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_button_accept_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_button_accept_dialog_general").assertDoesNotExist()
    }

    @Test
    fun givenLoginState_WhenDialogIsInUnverified_ThenShowTheCorrectData(){

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(),
                bottomSheetState = BottomSheetState(
                    showBottomSheet = true,
                    dataBottomSheet = DataLoginBottomSheet.UNVERIFIED_EMAIL
                ),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("login_textField_dialog_forgot").assertDoesNotExist()
        composeTestRule.onNodeWithTag("login_button_cancel_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_button_accept_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_button_accept_dialog_general").assertDoesNotExist()
    }

    @Test
    fun givenLoginState_WhenDialogIsInUnverified_ThenPerformClick(){
        var cancelClicked = false
        var acceptClicked = false

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(),
                bottomSheetState = BottomSheetState(
                    showBottomSheet = true,
                    dataBottomSheet = DataLoginBottomSheet.UNVERIFIED_EMAIL
                ),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = { cancelClicked = true },
                onAccept = { acceptClicked = true }
            )
        }

        composeTestRule.onNodeWithTag("login_button_cancel_dialog").performClick()
        composeTestRule.onNodeWithTag("login_button_accept_dialog").performClick()

        assertEquals(true,acceptClicked)
        assertEquals(true,cancelClicked)
    }

    @Test
    fun givenLoginState_WhenDialogIsAccountCreated_ThenShowCorrectData(){

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(),
                bottomSheetState = BottomSheetState(
                    showBottomSheet = true,
                    dataBottomSheet = DataLoginBottomSheet.ACCOUNT_CREATED
                ),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = {}
            )
        }

        composeTestRule.onNodeWithTag("login_button_accept_dialog_general").assertIsDisplayed()
    }

    @Test
    fun givenLoginState_WhenDialogIsAccountCreated_ThenPerformClick(){
        var acceptClicked = false

        composeTestRule.setContent {
            VerticalLoginScreen(
                uiState = LoginUIState.Login,
                dataLoginScreen = DataLoginScreen(),
                bottomSheetState = BottomSheetState(
                    showBottomSheet = true,
                    dataBottomSheet = DataLoginBottomSheet.ACCOUNT_CREATED
                ),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {},
                onDismiss = {},
                onAccept = { acceptClicked = true}
            )
        }

        composeTestRule.onNodeWithTag("login_button_accept_dialog_general").performClick()

        composeTestRule.waitForIdle()

        assertTrue(acceptClicked)
    }
}