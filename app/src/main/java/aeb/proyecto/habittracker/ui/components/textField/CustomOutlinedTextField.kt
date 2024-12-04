package aeb.proyecto.habittracker.ui.components.textField

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.theme.borderTextField
import aeb.proyecto.habittracker.ui.theme.containerTextFieldColor
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedTextField(
    rememberTextFieldState: TextFieldState = rememberTextFieldState(),
    @StringRes label:Int,
    modifier: Modifier = Modifier,
    isNeeded:Boolean = false
){

    val wasFilled = remember { mutableStateOf(false) }
    val isError = remember { mutableStateOf(false) }

    if(rememberTextFieldState.text.toString().isNotEmpty())
        wasFilled.value = true

    isError.value = rememberTextFieldState.text.toString().isEmpty() && wasFilled.value && isNeeded

    OutlinedTextField(
        state = rememberTextFieldState,
        labelPosition = TextFieldLabelPosition.Above(),
        label = {LabelSmallText(stringResource(label))},
        lineLimits = TextFieldLineLimits.SingleLine,
        isError = isError.value,
        modifier = modifier.height(60.dp),
        contentPadding = PaddingValues( horizontal = spacing8),
        colors = OutlinedTextFieldDefaults.colors(
            errorLabelColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = containerTextFieldColor,
            unfocusedContainerColor = containerTextFieldColor,
            errorContainerColor = containerTextFieldColor,
            focusedBorderColor = borderTextField,
            unfocusedBorderColor = borderTextField
        ),
        shape = RoundedCornerShape(spacing8)
    )

    // Espacio para el supportingText
    if (isError.value) {
        Spacer(modifier = Modifier.height(4.dp))
        LabelSmallText(
            stringResource(R.string.add_habit_screen_no_name),
            modifier = Modifier.fillMaxWidth().padding(horizontal = spacing8),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.error
        )
    }

}

@Preview
@Composable
fun CustomOutlinedTextFieldPreview(){
    CustomOutlinedTextField(label = R.string.topbar_habit)
}