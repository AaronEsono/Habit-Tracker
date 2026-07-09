package aeb.proyecto.ui.regexTextField

import aeb.proyecto.room.model.classes.UnitType
import aeb.proyecto.room.model.classes.UnitHabit
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

// ==========================================================================================
// REGULAR EXPRESSION PATTERNS
// ==========================================================================================

/** Matches standard integer strings, allowing optional negative prefixes and float fractions. */
val onlyDigits = "-?[0-9]+(\\\\.[0-9]+)?".toRegex()

/** Matches strictly positive decimal numbers containing up to 3 fractional digit columns max. */
val onlyDecimal = """^\d+(\.\d{0,3})?$""".toRegex()

/** Matches localized single or double-digit clock sequences ranging from 00 to 59 indices. */
val onlyZeroTo59 = "([1-5]?\\d)".toRegex()

// ==========================================================================================
// COMPOSABLE SANITIZATION ENGINE HOOKS
// ==========================================================================================

/**
 * High-level polymorphic orchestrator routing input text fields toward the appropriate
 * regex sanitizer pattern based on the semantic active [UnitHabit] structure.
 */
@Composable
fun IsOnlyDigit(textFieldState: TextFieldState, unit: UnitHabit){
    val regex = when(unit.unitType){
        UnitType.FREQUENCY,UnitType.QUANTITY -> {onlyDecimal}
        UnitType.TIME -> {onlyDigits}
    }

    when(regex){
        onlyDecimal -> IsOnlyDecimal(textFieldState)
        onlyDigits -> IsOnlyDigit(textFieldState)
    }
}

/**
 * Validates text entry fields in real time, slicing away non-numeric digits
 * immediately to maintain a sterile integer mathematical sequence.
 */
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

/**
 * Advanced floating-point input filter. Automatically formats leading decimal breaks
 * (turning "." into "0.") and blocks supplementary fractional numbers past 3 digital levels.
 */
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

/**
 * Enforces a rigorous temporal clock boundary mapping seconds or minutes from 0 up to 59.
 * Automatically prepends structural zeroes on ambiguous unit entries (e.g., turning "7" into "07")
 * to align with standard metric representations.
 */
@Composable
fun IsOnlyZeroTo59(textFieldState: TextFieldState) {
    LaunchedEffect(textFieldState.text) {
        val text = textFieldState.text.toString()

        val value = text.toIntOrNull()
        if (value != null) {
            if (value in 6..9 && text.length == 1) {
                // Instantly normalize high single digits into proper twin clock tokens
                textFieldState.edit {
                    replace(0, text.length, "0$text")
                }
            } else if (value!in 0..59) {
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

/**
 * Restricts input ranges strictly to a centesimal matrix (integers between 0 and 99 inclusive).
 * Drops trailing overflow mutations exceeding two text columns.
 */
@Composable
fun IsOnlyZeroTo99(textFieldState: TextFieldState) {
    LaunchedEffect(textFieldState.text) {
        val text = textFieldState.text.toString()

        val value = text.toIntOrNull()
        if (value != null) {
            if (value!in 0..99) {
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

/**
 * Imposes a strictly positive tracking sequence ranging bounded from 1 up to 99 indices.
 * Drops any numeric character that forces the domain parameter to zero or overflows 2 structural columns.
 */
@Composable
fun OneTo99(textFieldState: TextFieldState) {
    LaunchedEffect(textFieldState.text) {
        val text = textFieldState.text.toString()

        val value = text.toIntOrNull()
        if (value != null) {
            if (value !in 1..99) {
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