package aeb.proyecto.login.components.common.textField

import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.textField.CustomTextField
import aeb.proyecto.ui.textField.utils.clearFocusOnKeyboardDismiss
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

/**
 * Standard input field for non-sensitive data entry within the authentication flow.
 * Includes a context-aware clear button and automated focus management logic.
 *
 * @param modifier Structural Modifier parameters.
 * @param textFieldState Reactive state container for the text input.
 * @param isError Flag to trigger the error visual state.
 * @param errorText Supporting message to display when [isError] is true.
 * @param label Placeholder label for the input field.
 * @param containerColor Background color variant for the field container.
 * @param leadingIcon Optional leading icon for visual context (default: Email).
 * @param keyboardType Type of keyboard to display (default: Email).
 * @param imeAction Keyboard action behavior (Next/Done).
 * @param focusManager Controller for handling keyboard focus transitions.
 */
@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState = rememberTextFieldState(),
    isError: Boolean = false,
    errorText : String? = null,
    label: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
    leadingIcon:ImageVector? = Icons.Filled.Email,
    keyboardType: KeyboardType = KeyboardType.Email,
    imeAction: ImeAction = ImeAction.Next,
    focusManager: FocusManager,
){

    CustomTextField(
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
            when (textFieldState.text.toString()) {
                "" -> {}
                else -> {
                    CustomRipple {
                        IconButton(onClick = {textFieldState.edit { replace(0,length,"") }}){
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Clear icon TextField",
                            )
                        }
                    }
                }
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

            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            errorContainerColor = containerColor,

            selectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.onSurface,
                backgroundColor = MaterialTheme.colorScheme.onSurface
            )
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = keyboardType
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
    )

}
