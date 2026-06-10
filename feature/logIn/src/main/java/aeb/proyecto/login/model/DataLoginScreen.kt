package aeb.proyecto.login.model

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents the complete UI state for the Login/Registration screen.
 * * This class holds the configuration and user input states required to render
 * the login form, including form modes, text field contents, and validation states.
 *
 * @property isChecked Whether the "Remember Me" or terms acceptance checkbox is active.
 * @property isInLoginMode Defines if the screen is currently in Login mode (true) or Registration mode (false).
 * @property emailTextFieldState Holds the input state for the email field.
 * @property passwordTextFieldState Holds the input state for the password field.
 * @property rememberTextFieldState Holds the state for any additional input required for verification or recovery.
 * @property emailError A reactive stream representing the validity state of the email field.
 */
data class DataLoginScreen(
    var isChecked:Boolean = false,
    val isInLoginMode:Boolean = true,
    var emailTextFieldState:TextFieldState = TextFieldState(),
    var passwordTextFieldState:TextFieldState = TextFieldState(),
    var rememberTextFieldState:TextFieldState = TextFieldState(),
    var emailError: StateFlow<Boolean> = MutableStateFlow(false)
)