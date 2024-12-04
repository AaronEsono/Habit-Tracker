package aeb.proyecto.habittracker.ui.components.textField

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomOutlinedTextField(
    text: MutableState<String> = mutableStateOf("")
){

    OutlinedTextField(
        state = rememberTextFieldState(),
        label = { Text("Label") },
        lineLimits = TextFieldLineLimits.SingleLine,
    )

}


@Preview
@Composable
fun CustomOutlinedTextFieldPreview(){
    CustomOutlinedTextField()
}