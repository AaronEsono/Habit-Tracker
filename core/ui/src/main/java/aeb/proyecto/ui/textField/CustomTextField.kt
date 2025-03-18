package aeb.proyecto.ui.textField

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    textFieldState:TextFieldState,
    enabled:Boolean = true,
    isError:Boolean = false,
    shape: Shape =  OutlinedTextFieldDefaults.shape,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardActions: KeyboardActionHandler? = null,
    contentPadding: PaddingValues = OutlinedTextFieldDefaults.contentPadding(),
    label: @Composable() (TextFieldLabelScope.() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Above(),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
){

    OutlinedTextField(
        modifier = modifier,
        state = textFieldState,
        label = label,
        enabled = enabled,
        shape = shape,
        isError = isError,
        labelPosition = labelPosition,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardActions,
        contentPadding = contentPadding,
        lineLimits = lineLimits,
        colors = colors,
    )

}