package aeb.proyecto.login.model

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class DataLoginScreen(
    var isChecked:Boolean = false,
    val isInLoginMode:Boolean = true,
    var emailTextFieldState:TextFieldState = TextFieldState(),
    var passwordTextFieldState:TextFieldState = TextFieldState(),
    var rememberTextFieldState:TextFieldState = TextFieldState(),
    var emailError: StateFlow<Boolean> = MutableStateFlow(false)
)