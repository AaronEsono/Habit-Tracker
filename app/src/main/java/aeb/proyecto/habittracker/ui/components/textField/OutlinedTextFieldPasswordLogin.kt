package aeb.proyecto.habittracker.ui.components.textField

import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedTextFieldPasswordLogin(
    rememberTextFieldState: TextFieldState = rememberTextFieldState(),
    @StringRes label:Int,
    @StringRes labelError:Int,
    modifier: Modifier = Modifier,
    modifierError: Modifier = Modifier,
    isError:Boolean = false,
    showLabel: Boolean = true,
    shape: Shape = RoundedCornerShape(spacing8),
    imeActionPassword: ImeAction = ImeAction.Done,
    textAlignError: TextAlign = TextAlign.Left,
    contentPadding: PaddingValues = PaddingValues(horizontal = spacing8),
    clearFocus: Boolean = true,
    labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Attached(),
    errorLabelColor: Color = MaterialTheme.colorScheme.error,
    errorTextColor: Color = MaterialTheme.colorScheme.error,
    errorBorderColor: Color = MaterialTheme.colorScheme.error,
    errorCursorColor: Color = MaterialTheme.colorScheme.error,
    errorTrailingIconColor: Color = MaterialTheme.colorScheme.error,
    errorLeadingIconColor: Color = MaterialTheme.colorScheme.error,
    errorColorText: Color = MaterialTheme.colorScheme.error,
    focusedContainerColor: Color = MaterialTheme.colorScheme.surfaceTint,
    unfocusedContainerColor: Color = MaterialTheme.colorScheme.surfaceTint,
    errorContainerColor: Color = MaterialTheme.colorScheme.surfaceTint,
    focusedBorderColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    unfocusedTextColor: Color = MaterialTheme.colorScheme.onSurface,
    focusedTextColor: Color = MaterialTheme.colorScheme.onSurface,
    cursorColor: Color = MaterialTheme.colorScheme.onSurface,
){
    val focusManager = LocalFocusManager.current
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    OutlinedSecureTextField(
        modifier = modifier,
        state = rememberTextFieldState,
        labelPosition = labelPosition,
        label = { if(showLabel) LabelMediumText(stringResource(label)) },
        textObfuscationMode = if (passwordHidden) TextObfuscationMode.RevealLastTyped else TextObfuscationMode.Visible,
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                val visibilityIcon = if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                val description = if (passwordHidden) "Show password" else "Hide password"
                Icon(imageVector = visibilityIcon, contentDescription = description
                , tint = if (isError) errorColorText else focusedTextColor)
            }
        },
        isError = isError,
        contentPadding = contentPadding,
        colors = OutlinedTextFieldDefaults.colors(
            errorLabelColor = errorLabelColor,
            focusedContainerColor = focusedContainerColor,
            unfocusedContainerColor = unfocusedContainerColor,
            errorContainerColor = errorContainerColor,
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            unfocusedTextColor = unfocusedTextColor,
            focusedTextColor = focusedTextColor,
            cursorColor = cursorColor,
            errorTextColor = errorTextColor,
            errorBorderColor = errorBorderColor,
            errorCursorColor = errorCursorColor,
            errorTrailingIconColor = errorTrailingIconColor,
            errorLeadingIconColor = errorLeadingIconColor,
        ),
        shape = shape,
        keyboardOptions = KeyboardOptions(imeAction = imeActionPassword),
        onKeyboardAction = {
            if (imeActionPassword == ImeAction.Done && clearFocus) {
                focusManager.clearFocus()
            }
            if(imeActionPassword == ImeAction.Next){
                focusManager.moveFocus(FocusDirection.Down)
            }
        }
    )

    // Espacio para el supportingText
    if (isError) {
        Spacer(modifier = Modifier.height(4.dp))

        LabelSmallText(
            stringResource(labelError),
            modifier = modifierError,
            textAlign = textAlignError,
            color = errorColorText
        )
    }

}