package aeb.proyecto.habittracker.ui.components.textField

import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedTextFieldPasswordLogin(
    @StringRes label:Int,
    @StringRes labelError:Int,
    modifier: Modifier = Modifier,
    rememberTextFieldState: TextFieldState = rememberTextFieldState(),
    imeAction: ImeAction,
    wasFilled: MutableState<Boolean>
){

    if (rememberTextFieldState.text.toString().isNotEmpty()) wasFilled.value = true

    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    val isError = rememberTextFieldState.text.toString().isEmpty() && wasFilled.value

    OutlinedSecureTextField(
        modifier = modifier,
        state = rememberTextFieldState,
        label = { LabelMediumText(stringResource(label)) },
        textObfuscationMode = if (passwordHidden) TextObfuscationMode.RevealLastTyped else TextObfuscationMode.Visible,
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                val visibilityIcon = if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                val description = if (passwordHidden) "Show password" else "Hide password"
                Icon(imageVector = visibilityIcon, contentDescription = description
                , tint = if (isError) ColorsTheme.colorError else ColorsTheme.themeText)
            }
        },
        isError = isError,
        contentPadding = PaddingValues(horizontal = spacing8),
        colors = OutlinedTextFieldDefaults.colors(
            errorLabelColor = ColorsTheme.colorError,
            focusedContainerColor = ColorsTheme.containerTextFieldColor,
            unfocusedContainerColor = ColorsTheme.containerTextFieldColor,
            errorContainerColor = ColorsTheme.containerTextFieldColor,
            unfocusedTextColor = ColorsTheme.themeText,
            focusedTextColor = ColorsTheme.themeText,
            errorTextColor = ColorsTheme.colorError,
            unfocusedLabelColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            focusedBorderColor = ColorsTheme.borderTextField,
            unfocusedBorderColor = ColorsTheme.borderTextField,
            errorBorderColor = ColorsTheme.colorError,
            cursorColor = ColorsTheme.themeText
        ),
        shape = RoundedCornerShape(spacing8),
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
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