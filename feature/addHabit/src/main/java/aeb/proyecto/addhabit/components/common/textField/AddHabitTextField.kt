package aeb.proyecto.addhabit.components.common.textField

import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.textField.CustomTextField
import aeb.proyecto.ui.textField.utils.clearFocusOnKeyboardDismiss
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

/**
 * A highly-customized, state-driven text input field component built on top of the modern foundation API.
 * Leverages [TextFieldState] to manage characters asynchronously within an isolated atomic buffer,
 * embedding advanced hardware IME focus actions and custom design palettes.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param textFieldState The asynchronous backing controller pipeline managing character updates within the input field.
 * @param label Optional structural composable slot injected to draw text descriptions near or attached to the frame.
 * @param containerColor The design [Color] token allocated to paint the layout backdrop canvas layer.
 * @param contentPadding Custom internal boundary spacings defining the touch-target cushioning inside the text tray.
 * @param leadingIcon Optional symbolic composable slot pinned to the absolute starting margin of the text track.
 * @param keyboardType The behavioral configuration constraints mapped over virtual keyboards (e.g., Number, Text, Email).
 * @param trailingIcon Optional symbolic composable slot pinned to the absolute trailing margin of the text track.
 * @param imeAction The software action trigger key type mounted onto the execution button of virtual keyboards (Next, Done).
 * @param outPutTransformation Toolchain pipeline allowing post-processing text masking operations without mutating state.
 * @param placeholder Optional descriptive helper label drawn inside the input workspace when string content remains empty.
 * @param labelPosition Structural positional strategy defining the layout behavior of the attached label anchor.
 * @param focusManager The parent directional coordinator pipeline tracking active viewport input nodes.
 */
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

            // Seamless boundary concealment keeping borders perfectly consisten
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
            // Contextual routing matching the software keyboard action execution path
            when(imeAction){
                ImeAction.Done -> focusManager.clearFocus()
                ImeAction.Next -> focusManager.moveFocus(FocusDirection.Right)
            }
        }
    )

}

/**
 * A state-aware trailing contextual action button.
 * Monitors [TextFieldState] string buffers to append an operational clear handle whenever text fields carry active input.
 *
 * @param textFieldState The target text state controller pipeline bound to execute transactional wipe commands.
 */
@Composable
fun TrailingIcon(textFieldState: TextFieldState){
    // Evaluate the raw string representation inside the state buffer to compute node visibility states
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