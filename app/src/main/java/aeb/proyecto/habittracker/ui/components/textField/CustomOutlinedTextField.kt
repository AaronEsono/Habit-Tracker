package aeb.proyecto.habittracker.ui.components.textField

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedTextField(
    rememberTextFieldState: TextFieldState = rememberTextFieldState(),
    modifier: Modifier = Modifier,
    modifierError: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = spacing8),
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Above(),
    shape: Shape = RoundedCornerShape(spacing8),
    keyBoardType: KeyboardType = KeyboardType.Text,
    showLabel: Boolean = true,
    isError:Boolean = false,
    clearFocus: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    textAlignError: TextAlign = TextAlign.Left,
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
    @StringRes label: Int = R.string.buttons_accept,
    @StringRes labelError: Int = R.string.add_habit_screen_no_name,
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        state = rememberTextFieldState,
        labelPosition = labelPosition,
        label = { if (showLabel) LabelSmallText(stringResource(label)) },
        lineLimits = lineLimits,
        isError = isError,
        modifier = modifier,
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
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyBoardType, imeAction = imeAction),
        onKeyboardAction = {
            if (imeAction == ImeAction.Done && clearFocus) {
                focusManager.clearFocus()
            }
            if(imeAction == ImeAction.Next){
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

@Preview
@Composable
fun CustomOutlinedTextFieldPreview() {
    CustomOutlinedTextField(
        label = R.string.topbar_habit,
        labelError = R.string.add_habit_screen_no_name
    )
}