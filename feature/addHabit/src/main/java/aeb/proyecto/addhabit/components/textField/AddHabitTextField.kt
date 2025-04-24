package aeb.proyecto.addhabit.components.textField

import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.textField.CustomTextField
import aeb.proyecto.ui.textField.utils.clearFocusOnKeyboardDismiss
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.OutputTransformation
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
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AddHabitTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState = rememberTextFieldState(),
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceTint,
    contentPadding: PaddingValues = OutlinedTextFieldDefaults.contentPadding(),
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Email,
    trailingIcon:  @Composable() (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next,
    outPutTransformation: OutputTransformation? = null,
    placeholder: @Composable() (() -> Unit)? = null,
    labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Attached(),
    focusManager: FocusManager,
){

    CustomTextField(
        textFieldState = textFieldState,
        modifier = modifier
            .fillMaxWidth()
            .clearFocusOnKeyboardDismiss(),
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(spacing8),
        outputTransformation = outPutTransformation,
        contentPadding = contentPadding,
        labelPosition = labelPosition,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,

            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainer,
            focusedBorderColor = MaterialTheme.colorScheme.surfaceContainer,

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
                ImeAction.Next -> focusManager.moveFocus(FocusDirection.Right)
            }
        }
    )

}

@Composable
fun TrailingIcon(textFieldState: TextFieldState){
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
}