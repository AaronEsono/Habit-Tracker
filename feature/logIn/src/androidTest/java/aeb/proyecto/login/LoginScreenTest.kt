package aeb.proyecto.login

import aeb.proyecto.login.components.bottomSheet.LoginBottomSheet
import aeb.proyecto.login.model.DataLoginBottomSheet
import aeb.proyecto.login.model.DataLoginScreen
import android.content.Context
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun loginScreenShowCorrectly(){
        composeTestRule.setContent {
            LoginScreen(
                uiState = LoginUIState.Success,
                dataLoginScreen =  DataLoginScreen(
                    isInLoginMode = true
                ),
                onClickChecked = {},
                onClickLoginMode = {},
                 onClickAccept  = {},
                onClickGoogle = {},
                onClickResetPassword  = {},
                toSaveScreen= {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.login_title)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.login_subtitle)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.login_textField_email_label)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.login_textField_password_label)).assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun registerScreenShowCorrectly(){
        composeTestRule.setContent {
            LoginScreen(
                uiState = LoginUIState.Success,
                dataLoginScreen = DataLoginScreen(
                    isInLoginMode = false
                ),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = {},
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {}
            )
        }
            composeTestRule.onNodeWithText(context.getString(R.string.login_title_register)).assertExists()
                .assertIsDisplayed()

            composeTestRule.onNodeWithText(context.getString(R.string.login_subtitle_register)).assertExists()
                .assertIsDisplayed()

            composeTestRule.onNodeWithText(context.getString(R.string.login_textField_email_label)).assertExists()
                .assertIsDisplayed()

            composeTestRule.onNodeWithText(context.getString(R.string.login_textField_password_label)).assertExists()
                .assertIsDisplayed()

            composeTestRule.onNodeWithText(context.getString(R.string.login_textField_remember_password_label)).assertExists()
                .assertIsDisplayed()

    }

    @Test
    fun bottomWorksCorrectly() = runBlocking{
        val dataLoginScreen: MutableStateFlow<DataLoginScreen> = MutableStateFlow(
            DataLoginScreen(
                isInLoginMode = true,
                isChecked = false
            )
        )
        var onClickAccept = false
        var onClickGoogle = false
        var onClickResetPassword = false

        composeTestRule.setContent {
            LoginScreen(
                uiState = LoginUIState.Success,
                dataLoginScreen = dataLoginScreen.value,
                onClickChecked = { dataLoginScreen.update { it.copy(isChecked = !it.isChecked) } },
                onClickLoginMode = { dataLoginScreen.update { it.copy(isInLoginMode = !it.isInLoginMode) } },
                onClickAccept = { onClickAccept = true },
                onClickGoogle = { onClickGoogle = true },
                onClickResetPassword = { onClickResetPassword = true },
                toSaveScreen = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.login_title)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.login_subtitle)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("Checkbox row").performClick()
        composeTestRule.onNodeWithTag("Google button").performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.login_forgot_password)).performClick()

        assertTrue(onClickGoogle)
        assertTrue(onClickResetPassword)
        assertTrue(dataLoginScreen.value.isChecked)
    }

    @Test
    fun bottomSheetWorksCorrectly() {
        composeTestRule.setContent {
            LoginBottomSheet(
                dataBottomSheet = DataLoginBottomSheet.FORGOT_PASSWORD,
                emailTextFieldState = rememberTextFieldState(),
                onDismiss = {},
                onAccept = {}
                )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.login_forgot_password_title)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.login_forgot_password_subtitle)).assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun buttonEnabledCorrectly(){
        var isChecked = false

        composeTestRule.setContent {
            LoginScreen(
                uiState = LoginUIState.Success,
                dataLoginScreen = DataLoginScreen(
                    isInLoginMode = true,
                    emailTextFieldState = TextFieldState("prueba@gmail.com"),
                    passwordTextFieldState = TextFieldState("123456"),
                ),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = { isChecked = true },
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {}
            )
        }

        composeTestRule.onNodeWithTag("login button").performClick()

        assertTrue(isChecked)
    }

    @Test
    fun buttonDisabledCorrectly(){
            var isChecked = false

            composeTestRule.setContent {
                LoginScreen(
                    uiState = LoginUIState.Success,
                    dataLoginScreen = DataLoginScreen(
                        isInLoginMode = true,
                    ),
                    onClickChecked = {},
                    onClickLoginMode = {},
                    onClickAccept = { isChecked = true },
                    onClickGoogle = {},
                    onClickResetPassword = {},
                    toSaveScreen = {}
                )
            }

            composeTestRule.onNodeWithTag("login button").performClick()

            assertFalse(isChecked)
    }

    @Test
    fun textTextFieldWorksCorrectly(){

        composeTestRule.setContent {
            LoginScreen(
                uiState = LoginUIState.Success,
                dataLoginScreen = DataLoginScreen(
                    isInLoginMode = true,
                    emailTextFieldState = TextFieldState("prueba@gmail.com"),
                    passwordTextFieldState = TextFieldState("123456"),
                ),
                onClickChecked = {},
                onClickLoginMode = {},
                onClickAccept = { },
                onClickGoogle = {},
                onClickResetPassword = {},
                toSaveScreen = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.login_textField_email_label))
            .assertTextContains("prueba@gmail.com")

        composeTestRule.onNodeWithText(context.getString(R.string.login_textField_password_label))
            .assertTextContains("123456")
    }

}