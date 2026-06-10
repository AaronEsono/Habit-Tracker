package aeb.proyecto.login.utils

import android.content.Context
import android.content.ContextWrapper
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.compose.foundation.text.input.TextFieldState

/**
 * Checks if the email format is invalid. Returns false if empty to avoid showing error before user types.
 * @param textFieldState The input state containing the email text.
 * @return True if the email is malformed and not empty.
 */
fun isEmailInvalid(textFieldState: TextFieldState): Boolean {
    return !Patterns.EMAIL_ADDRESS.matcher(textFieldState.text).matches() && textFieldState.text.isNotEmpty()
}

/**
 * Checks if the password meets the minimum length requirement (only in registration mode).
 * @param textFieldState The input state containing the password.
 * @param isLoginMode Boolean flag indicating if the screen is in Login mode.
 * @return True if the password is too short in registration mode.
 */
fun isPasswordInvalid(textFieldState: TextFieldState, isLoginMode: Boolean): Boolean {
    return textFieldState.text.length < 6 && textFieldState.text.isNotEmpty() && !isLoginMode
}

/**
 * Validates that the confirm password/remember field matches the original password.
 * @param textFieldState The state of the confirmation password field.
 * @param passwordTextFieldState The state of the primary password field.
 * @return True if the confirmation does not match the password.
 */
fun isRememberInvalid(textFieldState: TextFieldState, passwordTextFieldState: TextFieldState): Boolean {
    return textFieldState.text.isNotEmpty() && textFieldState.text != passwordTextFieldState.text
}

/**
 * Comprehensive validation for the main authentication submission button.
 * @param emailState The email field state.
 * @param passwordState The password field state.
 * @param rememberState The confirmation/remember password state.
 * @param isLoginMode Whether the form is currently in Login mode.
 * @return True if all required fields are valid based on the active mode.
 */
fun isButtonEnabled(
    emailState: TextFieldState,
    passwordState: TextFieldState,
    rememberState: TextFieldState,
    isLoginMode: Boolean
): Boolean {
    val isEmailValid = emailState.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailState.text).matches()
    val isPasswordValid = passwordState.text.isNotEmpty()

    return if (isLoginMode) {
        isEmailValid && isPasswordValid
    } else {
        isEmailValid && isPasswordValid && rememberState.text == passwordState.text
    }
}

/**
 * Extension to unwrap a [Context] to find the underlying [ComponentActivity].
 * @return The found [ComponentActivity] or null if not found.
 */
fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}