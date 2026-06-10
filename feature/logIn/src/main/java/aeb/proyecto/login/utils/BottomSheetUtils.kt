package aeb.proyecto.login.utils

import aeb.proyecto.login.model.DataLoginBottomSheet
import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState

/** * Validates if the action button should be enabled based on the current context.
 * * @param dataLoginScreen The current state of the bottom sheet to determine the context.
 * @param emailTextFieldState The current state of the email input field.
 * @return True if the button should be enabled, false otherwise.
 */
fun isButtonEnabled(dataLoginScreen: DataLoginBottomSheet, emailTextFieldState: TextFieldState): Boolean{
    return dataLoginScreen != DataLoginBottomSheet.FORGOT_PASSWORD || Patterns.EMAIL_ADDRESS.matcher(emailTextFieldState.text).matches()
}