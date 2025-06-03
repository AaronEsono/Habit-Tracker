package aeb.proyecto.login.utils

import aeb.proyecto.login.model.DataLoginBottomSheet
import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState

fun isButtonEnabled(dataLoginScreen: DataLoginBottomSheet, emailTextFieldState: TextFieldState): Boolean{
    return dataLoginScreen != DataLoginBottomSheet.FORGOT_PASSWORD || Patterns.EMAIL_ADDRESS.matcher(emailTextFieldState.text).matches()
}