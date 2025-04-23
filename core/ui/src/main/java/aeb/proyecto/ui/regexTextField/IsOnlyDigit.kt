package aeb.proyecto.ui.regexTextField

import aeb.proyecto.room.model.classes.TIPO_UNIDAD
import aeb.proyecto.room.model.classes.UnitHabit
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

val onlyDigits = "-?[0-9]+(\\\\.[0-9]+)?".toRegex()
val onlyDecimal = """^\d+(\.\d{0,3})?$""".toRegex()
val onlyZeroTo59 = "([1-5]?\\d)".toRegex()


@Composable
fun IsOnlyDigit(textFieldState: TextFieldState, unit: UnitHabit){
    val regex = when(unit.unitType){
        TIPO_UNIDAD.FRECUENCIA,TIPO_UNIDAD.CANTIDAD -> {onlyDecimal}
        TIPO_UNIDAD.TIEMPO -> {onlyDigits}
    }

    when(regex){
        onlyDecimal -> IsOnlyDecimal(textFieldState)
        onlyDigits -> IsOnlyDigit(textFieldState)
    }
}

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

@Composable
fun IsOnlyDecimal(textFieldState: TextFieldState) {
    LaunchedEffect(textFieldState.text) {
        val text = textFieldState.text.toString()

        when {
            text == "." -> {
                textFieldState.edit {
                    replace(0, text.length, "0.")
                }
            }
            !text.matches(onlyDecimal) && text.isNotEmpty() -> {
                textFieldState.edit {
                    delete(text.length - 1, text.length)
                }
            }
        }
    }
}

@Composable
fun IsOnlyZeroTo59(textFieldState: TextFieldState) {
    LaunchedEffect(textFieldState.text) {
        val text = textFieldState.text.toString()

        val value = text.toIntOrNull()
        if (value != null) {
            if (value in 6..9 && text.length == 1) {
                textFieldState.edit {
                    replace(0, text.length, "0$text")
                }
            } else if (value > 59 || text.length >= 3) {
                textFieldState.edit {
                    delete(text.length - 1, text.length)
                }
            }
        } else if (text.isNotEmpty()) {
            textFieldState.edit {
                delete(text.length - 1, text.length)
            }
        }
    }
}
