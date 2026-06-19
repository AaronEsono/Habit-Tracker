package aeb.proyecto.timer.components.common.textField

import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.textField.CustomTextField
import aeb.proyecto.ui.textField.utils.clearFocusOnKeyboardDismiss
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

/**
 * A highly customizable text field designed for the Timer module.
 * Optimized for numeric input using [TextFieldState] and providing controls
 * for keyboard and focus management.
 *
 * @param modifier Applied to the [BasicTextField].
 * @param textFieldState The state holder for the text content.
 * @param containerColor Background color of the input field.
 * @param contentPadding Padding applied within the field.
 * @param keyboardType Type of keyboard to display (default [KeyboardType.Number]).
 * @param imeAction Action key for the IME (default [ImeAction.Done]).
 * @param outPutTransformation Optional visual transformation for the input.
 */
@Composable
fun TimerTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState = rememberTextFieldState(),
    containerColor: Color = MaterialTheme.colorScheme.surfaceTint,
    contentPadding: PaddingValues = OutlinedTextFieldDefaults.contentPadding(),
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Done,
    outPutTransformation: OutputTransformation? = null,
){

    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current


    val heightItem = remember {
        with(density) { 40.sp.toDp() }
    }

    CustomTextField(
        textFieldState = textFieldState,
        modifier = modifier
            .width((heightItem * 2) + spacing16)
            .clearFocusOnKeyboardDismiss(),
        textStyle = TextStyle(
            fontSize = 40.sp,
            textAlign = TextAlign.Center
        ),
        shape = RoundedCornerShape(spacing8),
        outputTransformation = outPutTransformation,
        contentPadding = contentPadding,
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
                ImeAction.Done -> {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            }
        }
    )
}