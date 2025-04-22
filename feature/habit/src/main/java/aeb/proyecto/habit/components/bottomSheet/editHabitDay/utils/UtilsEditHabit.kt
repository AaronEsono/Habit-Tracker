package aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils

import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.listTime
import java.math.BigDecimal
import java.math.RoundingMode

fun timesLeft(goal: BigDecimal, goalDone: BigDecimal): BigDecimal {
    return goal
        .minus(goalDone)
        .setScale(3, RoundingMode.HALF_UP)
        .stripTrailingZeros()
}

fun halfTimesLeft(timesLeft: BigDecimal, unit: UnitHabit): BigDecimal {
    val half = timesLeft.divide(BigDecimal(2), 10, RoundingMode.HALF_UP)

    return if (unit in listTime) {
        half.setScale(0, RoundingMode.HALF_UP) // sin decimales
    } else {
        half.setScale(3, RoundingMode.HALF_UP).stripTrailingZeros()
    }
}

fun isValidInput(text: String): Boolean {
    return try {
        val value = text.toBigDecimal()

        // No permitir 0, valores negativos ni ceros "enmascarados"
        value > BigDecimal.ZERO && value.stripTrailingZeros().scale() <= 3
    } catch (e: NumberFormatException) {
        false
    }
}