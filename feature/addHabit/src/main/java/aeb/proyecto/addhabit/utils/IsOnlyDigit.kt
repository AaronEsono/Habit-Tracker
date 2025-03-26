package aeb.proyecto.addhabit.utils

import aeb.proyecto.addhabit.constants.onlyDigits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun IsOnlyDigit(textFieldState: TextFieldState){

    LaunchedEffect(textFieldState.text) {
        if (!textFieldState.text.toString().matches(onlyDigits)
            && textFieldState.text.toString().isNotEmpty()) {

            textFieldState.edit {
                delete(textFieldState.text.length - 1, textFieldState.text.length)
            }
        }
    }

}