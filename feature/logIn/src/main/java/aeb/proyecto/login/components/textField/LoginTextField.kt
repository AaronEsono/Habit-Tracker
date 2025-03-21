package aeb.proyecto.login.components.textField

import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.textField.CustomSecureTextField
import aeb.proyecto.ui.textField.CustomTextField
import aeb.proyecto.ui.textField.utils.clearFocusOnKeyboardDismiss
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState = rememberTextFieldState(),
    isError: Boolean = false,
    errorText : String? = null,
    label: String? = null,
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
                LabelMediumText(
                    label,
                    color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
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

            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            errorCursorColor = MaterialTheme.colorScheme.error,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorTextColor = MaterialTheme.colorScheme.error,

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
