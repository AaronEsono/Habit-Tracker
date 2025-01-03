package aeb.proyecto.habittracker.ui.components.textField

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.utils.ColorsTheme
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun OutLinedTextFieldLogin(
    rememberTextFieldState: TextFieldState = rememberTextFieldState(),
    @StringRes label: Int,
    @StringRes labelError: Int = R.string.add_habit_screen_no_name,
    modifier: Modifier = Modifier
){

    val wasFilled = remember { mutableStateOf(false) }

    if (rememberTextFieldState.text.toString().isNotEmpty()) wasFilled.value = true
    val isError = rememberTextFieldState.text.toString().isEmpty() && wasFilled.value

    OutlinedTextField(
        //labelPosition = TextFieldLabelPosition.Above(),
        state = rememberTextFieldState,
        label = { LabelSmallText(stringResource(label)) },
        lineLimits = TextFieldLineLimits.SingleLine,
        isError = isError,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = spacing8),
        colors = OutlinedTextFieldDefaults.colors(
            errorLabelColor = ColorsTheme.colorError,
            focusedContainerColor = ColorsTheme.containerTextFieldColor,
            unfocusedContainerColor = ColorsTheme.containerTextFieldColor,
            errorContainerColor = ColorsTheme.containerTextFieldColor,
            focusedBorderColor = ColorsTheme.borderTextField,
            unfocusedBorderColor = ColorsTheme.borderTextField,
            unfocusedTextColor = ColorsTheme.themeText,
            focusedTextColor = ColorsTheme.themeText,
            errorTextColor = ColorsTheme.colorError,
            errorBorderColor = ColorsTheme.colorError,
            unfocusedLabelColor = Color.Transparent,
            focusedLabelColor = Color.Transparent
        ),
        shape = RoundedCornerShape(spacing8),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )

    // Espacio para el supportingText
    if (isError) {
        Spacer(modifier = Modifier.height(4.dp))
        LabelSmallText(
            stringResource(labelError),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(horizontal = spacing8),
            textAlign = TextAlign.Left,
            color = ColorsTheme.colorError
        )
    }
}