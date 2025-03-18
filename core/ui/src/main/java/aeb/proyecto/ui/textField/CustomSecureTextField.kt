package aeb.proyecto.ui.textField

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

@Composable
fun CustomSecureTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    enabled:Boolean = true,
    shape: Shape =  OutlinedTextFieldDefaults.shape,
    contentPadding: PaddingValues = OutlinedTextFieldDefaults.contentPadding(),
    label: @Composable() (TextFieldLabelScope.() -> Unit)? = null,
    isError:Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardActions: KeyboardActionHandler? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    textObfuscationMode: TextObfuscationMode = TextObfuscationMode.RevealLastTyped,
    labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Above(),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
){

    OutlinedSecureTextField(
        modifier = modifier,
        state = textFieldState,
        label = label,
        enabled = enabled,
        shape = shape,
        labelPosition = labelPosition,
        contentPadding = contentPadding,
        colors = colors,
        isError = isError,
        leadingIcon = leadingIcon,
        textObfuscationMode = textObfuscationMode,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardActions,
    )

}