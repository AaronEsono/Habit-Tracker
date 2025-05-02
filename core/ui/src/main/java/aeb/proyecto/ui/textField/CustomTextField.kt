package aeb.proyecto.ui.textField

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    textFieldState:TextFieldState,
    enabled:Boolean = true,
    isError:Boolean = false,
    placeholder: @Composable (() -> Unit)? = null,
    shape: Shape =  OutlinedTextFieldDefaults.shape,
    textStyle: TextStyle = LocalTextStyle.current,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardActions: KeyboardActionHandler? = null,
    contentPadding: PaddingValues = OutlinedTextFieldDefaults.contentPadding(),
    label: @Composable() (TextFieldLabelScope.() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Above(),
    outputTransformation: OutputTransformation? = null,
    inputTransformation: InputTransformation? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
){

    OutlinedTextField(
        modifier = modifier,
        state = textFieldState,
        label = label,
        placeholder = placeholder,
        enabled = enabled,
        shape = shape,
        textStyle = textStyle,
        isError = isError,
        labelPosition = labelPosition,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardActions,
        outputTransformation = outputTransformation,
        inputTransformation = inputTransformation,
        contentPadding = contentPadding,
        lineLimits = lineLimits,
        colors = colors,
    )

}