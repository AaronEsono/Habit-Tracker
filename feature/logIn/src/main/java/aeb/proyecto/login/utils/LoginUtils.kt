package aeb.proyecto.login.utils

import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState

fun isEmailInvalid(textFieldState: TextFieldState): Boolean {
    return !Patterns.EMAIL_ADDRESS.matcher(textFieldState.text).matches() && textFieldState.text.isNotEmpty()
}

fun isPasswordInvalid(textFieldState: TextFieldState, isLoginMode: Boolean): Boolean {
    return textFieldState.text.length < 6 && textFieldState.text.isNotEmpty() && !isLoginMode
}

fun isRememberInvalid(textFieldState: TextFieldState, passwordTextFieldState: TextFieldState): Boolean {
    return textFieldState.text.isNotEmpty() && textFieldState.text != passwordTextFieldState.text
}

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