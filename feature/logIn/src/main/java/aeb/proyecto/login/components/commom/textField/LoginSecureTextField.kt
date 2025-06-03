package aeb.proyecto.login.components.commom.textField

import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.textField.CustomSecureTextField
import aeb.proyecto.ui.textField.utils.clearFocusOnKeyboardDismiss
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction

@Composable
fun LoginSecureTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState = rememberTextFieldState(),
    isError: Boolean = false,
    errorText : String? = null,
    label: String? = null,
    leadingIcon: ImageVector? = Icons.Filled.Lock,
    imeAction: ImeAction = ImeAction.Next,
    focusManager: FocusManager,
){
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    CustomSecureTextField(
        textFieldState = textFieldState,
        modifier = modifier
            .fillMaxWidth()
            .clearFocusOnKeyboardDismiss(),
        label = {
            label?.let {
                LabelMediumText(label)
            }
        },
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    leadingIcon,
                    contentDescription = "Leading icon TextField"
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                val visibilityIcon =
                    if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                val description = if (passwordHidden) "Show password" else "Hide password"
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        },
        isError = isError,
        shape = RoundedCornerShape(spacing8),
        labelPosition = TextFieldLabelPosition.Attached(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.onSurface,
            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,

            errorLeadingIconColor = MaterialTheme.colorScheme.onSurface,
            errorTextColor = MaterialTheme.colorScheme.onSurface,
            errorTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            errorCursorColor = MaterialTheme.colorScheme.onSurface,
            errorBorderColor = MaterialTheme.colorScheme.onSurface,

            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            errorContainerColor = MaterialTheme.colorScheme.background,

            selectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.onSurface,
                backgroundColor = MaterialTheme.colorScheme.onSurface
            )
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
        ),
        onKeyboardActions = {
            when(imeAction){
                ImeAction.Done -> focusManager.clearFocus()
                ImeAction.Next -> focusManager.moveFocus(FocusDirection.Down)
            }
        },
        supportingText = {
            if(isError){
                errorText?.let {
                    LabelMediumText(
                        errorText,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = spacing8)
                    )
                }
            }
        },
        textObfuscationMode =
        if (passwordHidden) TextObfuscationMode.RevealLastTyped
        else TextObfuscationMode.Visible
    )

}