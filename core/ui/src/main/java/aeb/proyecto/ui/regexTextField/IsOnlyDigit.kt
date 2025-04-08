package aeb.proyecto.ui.regexTextField

import aeb.proyecto.room.model.classes.TIPO_UNIDAD
import aeb.proyecto.room.model.classes.UnitHabit
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

val onlyDigits = "-?[0-9]+(\\\\.[0-9]+)?".toRegex()
val onlyDecimal = """^\d+(\.\d{0,3})?$""".toRegex()

@Composable
fun IsOnlyDigit(textFieldState: TextFieldState, unit: UnitHabit){
    val regex = when(unit.unitType){
        TIPO_UNIDAD.FRECUENCIA,TIPO_UNIDAD.CANTIDAD -> {onlyDecimal}
        TIPO_UNIDAD.TIEMPO -> {onlyDigits}
    }

    LaunchedEffect(textFieldState.text) {
        if (!textFieldState.text.toString().matches(regex)
            && textFieldState.text.toString().isNotEmpty()) {

            textFieldState.edit {
                delete(textFieldState.text.length - 1, textFieldState.text.length)
            }
        }
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