package aeb.proyecto.habit.components.common.bottomSheet.configureHabit.utils

import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.listTime
import aeb.proyecto.room.utils.convertFromSeconds
import androidx.compose.foundation.text.input.TextFieldState
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Calculates remaining fulfillment threshold with strict rounding controls.
 */
fun timesLeft(goal: BigDecimal, goalDone: BigDecimal): BigDecimal {
    return goal
        .minus(goalDone)
        .setScale(3, RoundingMode.HALF_UP)
        .stripTrailingZeros()
}

/**
 * Derives a secondary incremental milestone (50% mark) based on unit-specific constraints.
 */
fun halfTimesLeft(timesLeft: BigDecimal, unit: UnitHabit): BigDecimal {
    val half = timesLeft.divide(BigDecimal(2), 10, RoundingMode.HALF_UP)

    return if (unit in listTime) {
        half.setScale(0, RoundingMode.HALF_UP) // Enforce integer-only for time units
    } else {
        half.setScale(3, RoundingMode.HALF_UP).stripTrailingZeros()
    }
}

/**
 * Transforms decimal value into a human-readable HH:mm string representation.
 */
fun passToHour(value:BigDecimal, unit: UnitHabit):String{
    val date = convertFromSeconds(value,unit)
    return date.first + ":" + date.second
}

/**
 * Sanitizes quantity-based text inputs, ensuring positive values and decimal scale constraints.
 */
fun isValidInput(text: String): Boolean {
    return try {
        val value = text.toBigDecimal()

        // No permitir 0, valores negativos ni ceros "enmascarados"
        value > BigDecimal.ZERO && value.stripTrailingZeros().scale() <= 3
    } catch (e: NumberFormatException) {
        false
    }
}

/**
 * Validates dual-input hour/minute fields, preventing invalid time signatures.
 */
fun isHourInputValid(
    firstTextFieldState: TextFieldState,
    secondTextFieldState: TextFieldState
): Boolean {
    val first = firstTextFieldState.text.toString().toIntOrNull() ?: 0
    val second = secondTextFieldState.text.toString().toIntOrNull() ?: 0

    // Enforce 60-second minute rule and non-zero duration constraint
    if (second !in 0..59) return false
    if (first == 0 && second == 0) return false

    return true
}
